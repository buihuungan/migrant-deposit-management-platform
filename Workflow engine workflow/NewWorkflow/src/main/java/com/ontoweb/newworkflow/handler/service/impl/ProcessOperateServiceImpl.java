package com.ontoweb.newworkflow.handler.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.*;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.query.TaskQuery;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.StartProcessInfo;
import com.ontoweb.newworkflow.core.service.TaskService;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import com.ontoweb.newworkflow.handler.VO.*;
import com.ontoweb.newworkflow.handler.dao.*;
import com.ontoweb.newworkflow.handler.service.DiagramService;
import com.ontoweb.newworkflow.handler.service.ProcessOperateService;
import com.ontoweb.newworkflow.handler.service.feign.EduService;
import com.ontoweb.newworkflow.handler.utils.CommonConstant;
import com.ontoweb.newworkflow.handler.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/9/27 下午4:12
 * @description：
 * @modified By：
 * @version: $
 */
@Slf4j
@Service
public class ProcessOperateServiceImpl implements ProcessOperateService {
    @Autowired
    private ProcessService processService;
    @Autowired
    private GetFormOnlineId getFormOnlineId;
    @Autowired
    private ProcessDefinitionDao processDefinitionDao;
    @Autowired
    private ProcessInstanceDao processInstanceDao;
    @Autowired
    private HistoryProcessInstanceDao historyProcessInstanceDao;
    @Autowired
    private HistoryTaskDao historyTaskDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private DiagramService diagramService;
    @Autowired
    private EduService eduService;
    @Autowired
    private BlobDao blobDao;
    @Autowired
    private BlobTempDao blobTempDao;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public JSONObject start(Long processId) {

        JSONObject returnJson = new JSONObject();

        //从前端中获取登录用户
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        //获取业务ID，将会关联流程个任务
        String businessId = UUID.randomUUID().toString().replace("-", "");

        StartProcessInfo startProcessInfo = new StartProcessInfo();
        startProcessInfo.setPromoter(loginUser);
        startProcessInfo.setBusinessId(businessId);
        ProcessInstance processInstance = processService.startProcessById(processId, startProcessInfo);
        processInstanceDao.save(processInstance);

        if (processInstance == null)
            throw  new RuntimeException(CommonConstant.START_PROCESS_FAILURE);

        String allId = getFormOnlineId.getAllId(processInstance.getId(), processInstance.getCurrentNode());
        String[] allIdList = allId.split("-");
        StartProcessVO startProcessVO = new StartProcessVO();
        startProcessVO.setProcessInstanceId(processInstance.getId());
        startProcessVO.setProcessId(processId);
        startProcessVO.setCurrentTask(processInstance.getCurrentTask());
        startProcessVO.setBusinessId(processInstance.getBusinessId());
        startProcessVO.setFormDesignerId(allIdList.length == 1 ? allIdList[0] : "");
        startProcessVO.setOnlineTableId(allIdList.length == 2 ? allIdList[1] : "");
        startProcessVO.setOnlineDataId(allIdList.length == 3 ? allIdList[2] : "");

        returnJson.put("startProcessVO", startProcessVO);

        // 第一个节点的任务id
        Long taskId = getFirstTaskId(processInstance.getId());
        returnJson.put("fistTaskId", taskId);

        return returnJson;
    }

    @Override
    public List<ProcessListVO> getProcessList(String categoryId,String category) {
        List<ProcessDefinition> list = processDefinitionDao.getProcessList(categoryId);

        //判断 是否是只展示待部署里面的 流程
        if (category.equals("1")){
            List<Blob> blobList = blobDao.findblobAll();
            List<BlobTemp> blobTempList = blobTempDao.findUfloTempAll();

            List<Long> processIdList = blobList.stream()
                    .filter(blob -> blobTempList.stream()
                            .anyMatch(blobTemp -> blobTemp.getName().equals(blob.getName())))
                    .map(Blob::getProcessId)
                    .collect(Collectors.toList());

            list = list.stream()
                    .filter(processDefinition -> processIdList.stream()
                            .anyMatch(id -> id == processDefinition.getId()))
                    .collect(Collectors.toList());
        }

        List<ProcessListVO> returnList = new ArrayList<>();
        for (ProcessDefinition processDefinition : list) {
            ProcessListVO processListVO = new ProcessListVO();
            processListVO.setProcessId(processDefinition.getId());
            processListVO.setCreateDate(processDefinition.getCreateDate());
            processListVO.setDescription(processDefinition.getDescription());
            processListVO.setEffectDate(processDefinition.getEffectDate());
            processListVO.setKey(processDefinition.getKey());
            processListVO.setName(processDefinition.getName());
            processListVO.setVersion(processDefinition.getVersion());
            processListVO.setCategoryId(processDefinition.getCategoryId());
            returnList.add(processListVO);
        }

        //按日期从大到小
        returnList.sort(Comparator.comparing(ProcessListVO::getCreateDate).reversed());

        return returnList;
    }

    @Override
    public List<HistoryProcessInstanceVO> cancelProcess(JSONObject jsonObject) {
        // 返回的结果
        List<HistoryProcessInstanceVO> list = new ArrayList<>();
        // 登录用户
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();

        // 获得所有已拒绝的流程
        List<HistoryProcessInstance> cancelProcess = historyProcessInstanceDao.getCancelProcess();
        List<Long> hisProcessInstanceId = new ArrayList<>();
        for (HistoryProcessInstance historyProcessInstance : cancelProcess) {
            hisProcessInstanceId.add(historyProcessInstance.getId());
        }
        // 如果没有则直接返回
        if (hisProcessInstanceId.size() == 0)
            return list;

        // 检查是否有其他的参与者
        Set<Long> resultHistoryProcessInstanceIds = new HashSet<>();
        List<HistoryTask> historyTasks = historyTaskDao.getProcessAssignee(hisProcessInstanceId);
        for (HistoryTask historyTask : historyTasks) {
            if (historyTask.getAssignee().equals(loginUser)) {
                resultHistoryProcessInstanceIds.add(historyTask.getHistoryProcessInstanceId());
            }
        }

        if(resultHistoryProcessInstanceIds.size() == 0){
            return list;
        }

        List<HistoryProcessInstance> returnProcessInstance = historyProcessInstanceDao.findAllByIdIn(resultHistoryProcessInstanceIds, jsonObject.getLong("processId"), jsonObject.getDate("startTime"), jsonObject.getDate("endTime"));

        for (HistoryProcessInstance historyProcessInstance : returnProcessInstance) {
            HistoryProcessInstanceVO historyProcessInstanceVO = new HistoryProcessInstanceVO();
            ProcessDefinition processDefinition = processService.getProcessById(historyProcessInstance.getProcessId());
            if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                    historyProcessInstanceVO.setId(historyProcessInstance.getId());
                    historyProcessInstanceVO.setCreateDate(historyProcessInstance.getCreateDate());
                    historyProcessInstanceVO.setEndDate(historyProcessInstance.getEndDate());
                    historyProcessInstanceVO.setPromoter(historyProcessInstance.getPromoter());
                    historyProcessInstanceVO.setProcessName(processDefinition.getName());
                    historyProcessInstanceVO.setProcessInstanceId(historyProcessInstance.getProcessInstanceId());
                    historyProcessInstanceVO.setState("cancel");
                    list.add(historyProcessInstanceVO);
                }
            }else{
                historyProcessInstanceVO.setId(historyProcessInstance.getId());
                historyProcessInstanceVO.setCreateDate(historyProcessInstance.getCreateDate());
                historyProcessInstanceVO.setEndDate(historyProcessInstance.getEndDate());
                historyProcessInstanceVO.setPromoter(historyProcessInstance.getPromoter());
                historyProcessInstanceVO.setProcessName(processDefinition.getName());
                historyProcessInstanceVO.setProcessInstanceId(historyProcessInstance.getProcessInstanceId());
                historyProcessInstanceVO.setState("cancel");
                list.add(historyProcessInstanceVO);
            }
        }
        return list;
    }

    @Override
    public List<ProcessInstanceVO> inProgressProcessInstance(JSONObject jsonObject) {
        // 返回结果
        List<ProcessInstanceVO> list = new ArrayList<>();
        // 登录用户
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        // 正在进行中的流程实例
        List<ProcessInstance> processInstances = processInstanceDao.findAll(jsonObject.getLong("processId"), jsonObject.getDate("startTime"), jsonObject.getDate("endTime"));
        List<Long> processInstanceIds = new ArrayList<>();
        Map<Long, ProcessInstance> processInstanceMap = new HashMap<>();
        for (ProcessInstance processInstance : processInstances) {
            processInstanceIds.add(processInstance.getId());
            processInstanceMap.put(processInstance.getId(), processInstance);
        }

        if (processInstanceIds.size() == 0)
            return list;
        // 参与者相关的流程实例
        List<Task> tasks = taskDao.findAllByProcessInstanceId(processInstanceIds);
        Set<Long> processInstanceIdsSet = new HashSet<>();
        for (Task task : tasks) {
            log.info("taskAssign:{}" + task.getAssignee());
            if (task.getAssignee() == null)
                continue;
            if (task.getAssignee().equals(loginUser)) {
                processInstanceIdsSet.add(task.getProcessInstanceId());
            }
        }

        // 获得正在进行中的流程
        for (Long l : processInstanceIdsSet) {
            if (processInstanceMap.containsKey(l)) {
                ProcessInstanceVO processInstanceVO = new ProcessInstanceVO();
                ProcessInstance processInstance = processInstanceMap.get(l);
                ProcessDefinition processDefinition = processService.getProcessById(processInstance.getProcessId());
                if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                    if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                        processInstanceVO.setId(processInstance.getId());
                        processInstanceVO.setCreateDate(processInstance.getCreateDate());
                        processInstanceVO.setPromoter(processInstance.getPromoter());
                        processInstanceVO.setProcessName(processDefinition.getName());
                        processInstanceVO.setCurrentTask(processInstance.getCurrentTask());
                        processInstanceVO.setProcessInstanceId(processInstance.getId());
                        processInstanceVO.setState("instance");
                        list.add(processInstanceVO);
                    }
                }else{
                    processInstanceVO.setId(processInstance.getId());
                    processInstanceVO.setCreateDate(processInstance.getCreateDate());
                    processInstanceVO.setPromoter(processInstance.getPromoter());
                    processInstanceVO.setProcessName(processDefinition.getName());
                    processInstanceVO.setCurrentTask(processInstance.getCurrentTask());
                    processInstanceVO.setProcessInstanceId(processInstance.getId());
                    processInstanceVO.setState("instance");
                    list.add(processInstanceVO);
                }
            }
        }

        return list;
    }

    @Override
    public List<HistoryProcessInstanceVO> completeProcessInstance(JSONObject jsonObject) {
        // 用作返回的list
        List<HistoryProcessInstanceVO> list = new ArrayList<>();
        // 登录用户
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        List<HistoryProcessInstance> completeProcessInstances = historyProcessInstanceDao.getCompleteProcessInstances(jsonObject.getLong("processId"), jsonObject.getDate("startTime"), jsonObject.getDate("endTime"));

        List<Long> historyProcessInstanceIds = new ArrayList<>();
        Map<Long, HistoryProcessInstance> historyProcessInstanceHashMap = new HashMap<>();
        for (HistoryProcessInstance historyProcessInstance : completeProcessInstances) {
            historyProcessInstanceIds.add(historyProcessInstance.getId());
            historyProcessInstanceHashMap.put(historyProcessInstance.getId(), historyProcessInstance);
        }

        if (historyProcessInstanceIds.size() == 0)
            return list;

        // 参与者相关的流程实例
        List<HistoryTask> historyTasks = historyTaskDao.getProcessAssignee(historyProcessInstanceIds);
        Set<Long> historyProcessInstanceIdsSet = new HashSet<>();
        for (HistoryTask historyTask : historyTasks) {
            if (historyTask.getAssignee().equals(loginUser)) {
                historyProcessInstanceIdsSet.add(historyTask.getHistoryProcessInstanceId());
            }
        }

        // 获得已完成的流程
        for (Long l : historyProcessInstanceIdsSet) {
            if (historyProcessInstanceHashMap.containsKey(l)) {
                HistoryProcessInstanceVO historyProcessInstanceVO = new HistoryProcessInstanceVO();
                HistoryProcessInstance historyProcessInstance = historyProcessInstanceHashMap.get(l);
                ProcessDefinition processDefinition =  processService.getProcessById(historyProcessInstance.getProcessId());
                if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                    if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                        historyProcessInstanceVO.setId(historyProcessInstance.getId());
                        historyProcessInstanceVO.setCreateDate(historyProcessInstance.getCreateDate());
                        historyProcessInstanceVO.setEndDate(historyProcessInstance.getEndDate());
                        historyProcessInstanceVO.setPromoter(historyProcessInstance.getPromoter());
                        historyProcessInstanceVO.setProcessName(processDefinition.getName());
                        historyProcessInstanceVO.setState("complete");
                        historyProcessInstanceVO.setProcessInstanceId(historyProcessInstance.getProcessInstanceId());
                        list.add(historyProcessInstanceVO);
                    }
                }else{
                    historyProcessInstanceVO.setId(historyProcessInstance.getId());
                    historyProcessInstanceVO.setCreateDate(historyProcessInstance.getCreateDate());
                    historyProcessInstanceVO.setEndDate(historyProcessInstance.getEndDate());
                    historyProcessInstanceVO.setPromoter(historyProcessInstance.getPromoter());
                    historyProcessInstanceVO.setProcessName(processDefinition.getName());
                    historyProcessInstanceVO.setProcessInstanceId(historyProcessInstance.getProcessInstanceId());
                    historyProcessInstanceVO.setState("complete");
                    list.add(historyProcessInstanceVO);
                }
            }
        }

        return list;
    }

    @Override
    public JSONObject startBy(ProcessVo processVo,String s) {

        JSONObject returnJson = new JSONObject();

        //从前端中获取登录用户
        Long processId = processVo.getProcessId();
        String teachingMode = processVo.getTeachingMode();
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        String planId = processVo.getPlanId();
        String lessonId = processVo.getLessonId();
        //获取业务ID，将会关联流程个任务

        // 这里不是很理解，businessId是可以关联我自己业务的还是？
        String businessId = UUID.randomUUID().toString().replace("-", "");
        StartProcessInfo startProcessInfo = new StartProcessInfo();

        // 以下 对我而言 实际都是 个人用户？ 如果是 有审批流程呢？

        if ("1".equals(teachingMode)) {
            startProcessInfo.setPromoter(loginUser);
            // 演示 写死id
            startProcessInfo.setPromoter("1437275060307296257");
        }else {
            Result<?> result = eduService.getGroupId(loginUser, planId, lessonId);
            String groupId = result.getResult().toString();
            if (groupId==null){
                log.error("该同学没有被分组");
                returnJson.put("error","-1");
                return returnJson;
            }
            startProcessInfo.setPromoter(groupId);
        }
        startProcessInfo.setBusinessId(businessId);
        //
        ProcessInstance processInstance = processService.startProcessById(processId, startProcessInfo);
        processInstance.setExampleId(s);
        processInstanceDao.save(processInstance);

        if (processInstance == null)
            throw  new RuntimeException(CommonConstant.START_PROCESS_FAILURE);

        String allId = getFormOnlineId.getAllId(processInstance.getId(), processInstance.getCurrentNode());
        String[] allIdList = allId.split("-");
        // StartProcessVO 字段含义？
        StartProcessVO startProcessVO = new StartProcessVO();
        startProcessVO.setProcessInstanceId(processInstance.getId());
        startProcessVO.setProcessId(processId);
        startProcessVO.setCurrentTask(processInstance.getCurrentTask());
        startProcessVO.setBusinessId(processInstance.getBusinessId());
        startProcessVO.setFormDesignerId(allIdList.length >= 1 ? allIdList[0] : "");
        startProcessVO.setOnlineTableId(allIdList.length >= 2 ? allIdList[1] : "");
        startProcessVO.setOnlineDataId(allIdList.length >= 3 ? allIdList[2] : "");
//        startProcessVO.setFormDesignerId(allIdList[0]);
//        startProcessVO.setOnlineTableId(allIdList[1]);
//        startProcessVO.setOnlineDataId(allIdList[2]);

        returnJson.put("startProcessVO", startProcessVO);

        // 第一个节点的任务id
        Long taskId = getFirstTaskId(processInstance.getId());
        returnJson.put("fistTaskId", taskId);

        JSONObject jsonObject = diagramService.drawDiagram(processInstance.getId());
        returnJson.put("ware", jsonObject);


        return returnJson;
    }

    @Override
    @Transactional()
    public Boolean deleteProcessInstanceFirst(JSONObject jsonObject) {
        try {
            Long processInstanceId = jsonObject.getLong("processInstanceId");
            //删除 任务节点 和 历史任务节点记录
            taskDao.deleteTaskByProcessInstanceId(processInstanceId);
            historyTaskDao.deleteHistoryTaskByProcessInstanceId(processInstanceId);
            //删除 流程实例 和 历史 流程实例
            processInstanceDao.deleteProcessInstanceById(processInstanceId);
            historyProcessInstanceDao.deleteHistoryProcessInstanceByProcessInstanceId(processInstanceId);
        }catch (Exception e){
            e.printStackTrace();
            // 手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;
    }

    public Long getFirstTaskId(Long processInstanceId) {
        TaskQuery query = taskService.createTaskQuery();
        query.addTaskState(TaskState.Created);
        query.addTaskState(TaskState.InProgress);
        query.addTaskState(TaskState.Ready);
        query.addTaskState(TaskState.Suspended);
        query.addTaskState(TaskState.Reserved);
        query.processInstanceId(processInstanceId);
        query.addOrderDesc("createDate");

        return query.list().get(0).getId();
    }

}

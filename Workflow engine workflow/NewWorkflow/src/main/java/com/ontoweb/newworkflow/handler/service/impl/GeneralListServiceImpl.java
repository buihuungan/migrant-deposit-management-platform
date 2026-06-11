package com.ontoweb.newworkflow.handler.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskParticipator;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import com.ontoweb.newworkflow.handler.VO.ProcessAllVO;
import com.ontoweb.newworkflow.handler.dao.*;
import com.ontoweb.newworkflow.handler.service.GeneralListService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GeneralListServiceImpl implements GeneralListService {
    @Autowired
    private ProcessService processService;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private TaskParticipatorDao taskParticipatorDao;
    @Autowired
    private HistoryProcessInstanceDao historyProcessInstanceDao;
    @Autowired
    private HistoryTaskDao historyTaskDao;
    @Autowired
    private ProcessInstanceDao processInstanceDao;



    @Override
    public List<ProcessAllVO> getClaimTaskList(JSONObject jsonObject) {
        // 返回的结果
        List<ProcessAllVO> listReturn = new ArrayList<>();
        // 登录用户
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        // 当前登录用户可以领取的任务id有哪些，通过task_paticipator来确认
        List<TaskParticipator> taskParticipators = taskParticipatorDao.findTaskParticipatorsByUser(loginUser);
        List<Long> ids = new ArrayList<>();
        for (TaskParticipator taskParticipator : taskParticipators) {
            ids.add(taskParticipator.getTaskId());
        }
        // 拿到所有当前用户的所有带领取任务
        if (ids.size() == 0)
            return listReturn;
        List<Task> tasks = taskDao.getTakes(ids, jsonObject.getLong("processId"), jsonObject.getString("taskName"), jsonObject.getDate("startTime"), jsonObject.getDate("endTime"));

        //处理数据之后返回给前端
        for (Task task : tasks) {
            ProcessAllVO processAllVO = new ProcessAllVO();
            ProcessDefinition processDefinition = processService.getProcessById(task.getProcessId());
            long processInstanceId = task.getProcessInstanceId();

            if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                    processAllVO.setTaskId(task.getId());
                    processAllVO.setProcessId(task.getProcessId());
                    processAllVO.setProcessInstanceId(processInstanceId);
                    processAllVO.setProcessHisInstanceId(processInstanceId+1);
                    processAllVO.setNodeName(task.getNodeName());
                    processAllVO.setProcessName(processDefinition.getName());
                    processAllVO.setCreateDate(task.getCreateDate());
                    processAllVO.setState(task.getState().toString());
                    processAllVO.setType(task.getType().toString());
                    listReturn.add(processAllVO);
                }
            }else{
                processAllVO.setTaskId(task.getId());
                processAllVO.setProcessId(task.getProcessId());
                processAllVO.setProcessInstanceId(processInstanceId);
                processAllVO.setProcessHisInstanceId(processInstanceId+1);
                processAllVO.setNodeName(task.getNodeName());
                processAllVO.setProcessName(processDefinition.getName());
                processAllVO.setCreateDate(task.getCreateDate());
                processAllVO.setState(task.getState().toString());
                processAllVO.setType(task.getType().toString());
                listReturn.add(processAllVO);
            }
        }

        return listReturn;
    }

    @Override
    public List<ProcessAllVO> getPendingTakes(JSONObject jsonObject) {
        // 返回的结果
        List<ProcessAllVO> listReturn = new ArrayList<>();
        // 登录用户
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        // 需要加入别的搜索条件
        List<Task> list = taskDao.getPendingTask(loginUser, jsonObject.getLong("processId"), jsonObject.getString("taskName"), jsonObject.getDate("startTime"), jsonObject.getDate("endTime"));

        //处理数据之后返回给前端
        for (Task task : list) {
            String prevTaskName = task.getPrevTask();
            long processInstanceId = task.getProcessInstanceId();
            //允许两秒的误差
            Calendar calendar = Calendar.getInstance();
            Date endDate = task.getCreateDate();
            calendar.setTime(endDate);
            calendar.add(Calendar.SECOND,-2);
            Date stratDate = calendar.getTime();
            List<Task> prevWithdrawTaskList = taskDao.findPrevWithdrawTask(processInstanceId, stratDate, endDate, prevTaskName);
            ProcessDefinition processDefinition = processService.getProcessById(task.getProcessId());

            ProcessAllVO processAllVO = new ProcessAllVO();
            processAllVO.setFlag(0);
            if (prevWithdrawTaskList.size() > 0) {
                processAllVO.setFlag(1);
            }

            if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                    processAllVO.setTaskId(task.getId());
                    processAllVO.setProcessId(task.getProcessId());
                    processAllVO.setProcessInstanceId(processInstanceId);
                    processAllVO.setProcessHisInstanceId(processInstanceId+1);
                    processAllVO.setNodeName(task.getNodeName());
                    processAllVO.setProcessName(processDefinition.getName());
                    processAllVO.setCreateDate(task.getCreateDate());
                    processAllVO.setState(task.getState().toString());
                    processAllVO.setType(task.getType().toString());
                    listReturn.add(processAllVO);
                }
            }else{
                processAllVO.setTaskId(task.getId());
                processAllVO.setProcessId(task.getProcessId());
                processAllVO.setProcessInstanceId(processInstanceId);
                processAllVO.setProcessHisInstanceId(processInstanceId+1);
                processAllVO.setNodeName(task.getNodeName());
                processAllVO.setProcessName(processDefinition.getName());
                processAllVO.setCreateDate(task.getCreateDate());
                processAllVO.setState(task.getState().toString());
                processAllVO.setType(task.getType().toString());
                listReturn.add(processAllVO);
            }
        }

        return listReturn;
    }

    @Override
    public List<ProcessAllVO> cancelProcess(JSONObject jsonObject) {
        // 返回的结果
        List<ProcessAllVO> listReturn = new ArrayList<>();
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
            return listReturn;

        // 检查是否参与
        Set<Long> resultHistoryProcessInstanceIds = new HashSet<>();
        List<HistoryTask> historyTasks = historyTaskDao.getProcessAssignee(hisProcessInstanceId);
        for (HistoryTask historyTask : historyTasks) {
            if (historyTask.getAssignee().equals(loginUser)) {
                resultHistoryProcessInstanceIds.add(historyTask.getHistoryProcessInstanceId());
            }
        }
        //如果没用参与直接返回
        if(resultHistoryProcessInstanceIds.size() == 0){
            return listReturn;
        }

        List<HistoryProcessInstance> returnProcessInstance = historyProcessInstanceDao.findAllByIdIn(resultHistoryProcessInstanceIds, jsonObject.getLong("processId"), jsonObject.getDate("startTime"), jsonObject.getDate("endTime"));

        for (HistoryProcessInstance historyProcessInstance : returnProcessInstance) {
            Long processInstanceId =  historyProcessInstance.getProcessInstanceId();
            ProcessDefinition processDefinition = processService.getProcessById(historyProcessInstance.getProcessId());
            ProcessAllVO processAllVO = new ProcessAllVO();
            if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                    processAllVO.setProcessId(historyProcessInstance.getProcessId());
                    processAllVO.setProcessInstanceId(processInstanceId);
                    processAllVO.setProcessHisInstanceId(processInstanceId+1);
                    processAllVO.setProcessName(processDefinition.getName());
                    processAllVO.setCreateDate(historyProcessInstance.getCreateDate());
                    processAllVO.setEndDate(historyProcessInstance.getEndDate());
                    listReturn.add(processAllVO);
                }
            }else{
                processAllVO.setProcessId(historyProcessInstance.getProcessId());
                processAllVO.setProcessInstanceId(processInstanceId);
                processAllVO.setProcessHisInstanceId(processInstanceId+1);
                processAllVO.setProcessName(processDefinition.getName());
                processAllVO.setCreateDate(historyProcessInstance.getCreateDate());
                processAllVO.setEndDate(historyProcessInstance.getEndDate());
                listReturn.add(processAllVO);
            }
        }
        return listReturn;
    }

    @Override
    public List<ProcessAllVO> inProgressProcessInstance(JSONObject jsonObject) {
        // 返回结果
        List<ProcessAllVO> listReturn = new ArrayList<>();
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
            return listReturn;
        // 参与者相关的流程实例
        List<Task> tasks = taskDao.findAllByProcessInstanceId(processInstanceIds);
        Set<Long> processInstanceIdsSet = new HashSet<>();
        for (Task task : tasks) {
            if (task.getAssignee() == null)
                continue;
            if (task.getAssignee().equals(loginUser)) {
                processInstanceIdsSet.add(task.getProcessInstanceId());
            }
        }

        // 获得正在进行中的流程
        for (Long l : processInstanceIdsSet) {
            if (processInstanceMap.containsKey(l)) {
                ProcessAllVO processAllVO = new ProcessAllVO();
                ProcessInstance processInstance = processInstanceMap.get(l);
                ProcessDefinition processDefinition = processService.getProcessById(processInstance.getProcessId());
                Long processInstanceId =  processInstance.getId();
                if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                    if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                        processAllVO.setProcessId(processInstance.getProcessId());
                        processAllVO.setProcessInstanceId(processInstanceId);
                        processAllVO.setProcessHisInstanceId(processInstanceId+1);
                        processAllVO.setProcessName(processDefinition.getName());
                        processAllVO.setCreateDate(processInstance.getCreateDate());
                        processAllVO.setNodeName(processInstance.getCurrentNode());
                        listReturn.add(processAllVO);
                    }
                }else{
                    processAllVO.setProcessId(processInstance.getProcessId());
                    processAllVO.setProcessInstanceId(processInstanceId);
                    processAllVO.setProcessHisInstanceId(processInstanceId+1);
                    processAllVO.setProcessName(processDefinition.getName());
                    processAllVO.setCreateDate(processInstance.getCreateDate());
                    processAllVO.setNodeName(processInstance.getCurrentNode());
                    listReturn.add(processAllVO);
                }
            }
        }

        return listReturn;
    }

    @Override
    public List<ProcessAllVO> completeProcessInstance(JSONObject jsonObject) {
        // 返回结果
        List<ProcessAllVO> listReturn = new ArrayList<>();
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
            return listReturn;

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
                ProcessAllVO processAllVO = new ProcessAllVO();
                HistoryProcessInstance historyProcessInstance = historyProcessInstanceHashMap.get(l);
                ProcessDefinition processDefinition =  processService.getProcessById(historyProcessInstance.getProcessId());
                Long processInstanceId =  historyProcessInstance.getProcessInstanceId();
                if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                    if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                        processAllVO.setProcessId(historyProcessInstance.getProcessId());
                        processAllVO.setProcessInstanceId(processInstanceId);
                        processAllVO.setProcessHisInstanceId(processInstanceId+1);
                        processAllVO.setProcessName(processDefinition.getName());
                        processAllVO.setCreateDate(historyProcessInstance.getCreateDate());
                        processAllVO.setEndDate(historyProcessInstance.getEndDate());
                        listReturn.add(processAllVO);
                    }
                }else{
                    processAllVO.setProcessId(historyProcessInstance.getProcessId());
                    processAllVO.setProcessInstanceId(processInstanceId);
                    processAllVO.setProcessHisInstanceId(processInstanceId+1);
                    processAllVO.setProcessName(processDefinition.getName());
                    processAllVO.setCreateDate(historyProcessInstance.getCreateDate());
                    processAllVO.setEndDate(historyProcessInstance.getEndDate());
                    listReturn.add(processAllVO);
                }
            }
        }

        return listReturn;
    }
}

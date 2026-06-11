package com.ontoweb.newworkflow.handler.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskParticipator;
import com.ontoweb.newworkflow.core.process.handler.*;
import com.ontoweb.newworkflow.core.process.listener.TaskListener;
import com.ontoweb.newworkflow.core.process.node.FormTemplateProvider;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.TaskService;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import com.ontoweb.newworkflow.handler.VO.TaskVO;
import com.ontoweb.newworkflow.handler.constant.RedisKey;
import com.ontoweb.newworkflow.handler.entity.ProcessMapping;
import com.ontoweb.newworkflow.handler.mapper.ProcessMappingMapper;
import com.ontoweb.newworkflow.handler.assignee.AssigneeProviderListServletHandlerImpl;
import com.ontoweb.newworkflow.handler.dao.*;
import com.ontoweb.newworkflow.handler.service.DiagramService;
import com.ontoweb.newworkflow.handler.service.TaskOperateService;
import com.ontoweb.newworkflow.handler.service.feign.OnlineService;
import com.ontoweb.newworkflow.handler.utils.RedisUtils;
import com.ontoweb.newworkflow.handler.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/9 下午2:42
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class TaskOperateServiceImpl implements TaskOperateService{
    @Autowired
    private TaskService taskService;
    @Autowired
    private AssigneeProviderListServletHandlerImpl assigneeProviderListServletHandler;
    @Autowired
    private TaskParticipatorDao taskParticipatorDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private HistoryTaskDao historyTaskDao;
    @Autowired
    private ProcessInstanceDao processInstanceDao;
    @Autowired
    private GetFormOnlineId getFormOnlineId;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ProcessService processService;
    private String onlineDataIdUse;
    @Autowired
    private DiagramService diagramService;
    @Autowired
    private BlobDao blobDao;
    @Autowired
    private OnlineService onlineService;
    @Autowired
    private ProcessMappingMapper processMappingMapper;
    @Autowired
    private RedisUtils redis;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public JSONObject completeTask(JSONObject jsonObject) {
        // 将值从json串中取出来，主要包含三个部分，taskId, onlineTableId, onlineDataId
        Long taskId = jsonObject.getLong("taskId");
        String onlineTableId = jsonObject.getString("onlineTableId");
        String onlineDataId = jsonObject.getString("onlineDataId");
        this.onlineDataIdUse = onlineDataId;
        // 开始一个任务
        taskService.start(taskId);

        Task task = taskService.getTask(taskId);
        // 缓存节点参数
        redis.set(RedisKey.PROCESSINSTANCE + task.getProcessInstanceId(),jsonObject);

        log.info("****************Before_taskUrl:{}****************",task.getUrl());
        this.saveCurrentTaskIds(jsonObject);

        JSONObject resultJson = new JSONObject();
        resultJson.put("result", true);

        // 完成任务,需要判断下一个节点是不是路由决策节点，如果是的话需要获取表单中的变量，并写入数据库中
        byte[] blobValue = blobDao.findblobProcessId(task.getProcessId()).getBlobValue();
        JSONObject processByteMap = JSONObject.parseObject(blobValue, JSONObject.class);
        List<JSONObject> nodeList = JSON.parseArray(processByteMap.getString("nodeList"), JSONObject.class);
        List<JSONObject> lineList = JSON.parseArray(processByteMap.getString("lineList"), JSONObject.class);

        //获取当前节点
        JSONObject nowNode = nodeList.stream()
                .filter(jsonObjectNode -> jsonObjectNode.getString("name").equals(task.getNodeName()))
                .findFirst().orElse(null);

        //获取当前节点的流出的线
        JSONObject nowLine = lineList.stream()
                .filter(jsonObjectLine -> jsonObjectLine.getString("from").equals(nowNode.getString("id")))
                .findFirst().orElse(null);

        //获取下一个节点
        JSONObject nextNode = nodeList.stream()
                .filter(jsonObjectNode -> jsonObjectNode.getString("id").equals(nowLine.getString("to")))
                .findFirst().orElse(null);

        if (nextNode.getString("type").equals("decision") && nextNode.getString("decision_type").equals("Expression")) {
            // 变量直接从前端传入给后端，有可能包含多个变量
            Result<?> res = onlineService.getForm(onlineTableId, onlineDataId);
            Map result = (Map) res.getResult();
            Map varibales = new HashMap();
            List<JSONObject> expressionList = JSON.parseArray(nextNode.getString("if_expression"), JSONObject.class);
            for (JSONObject expressionJson : expressionList) {
                String varName = expressionJson.getString("formId");
                varibales.put(varName,result.get(varName));
            }
            // test
            //varibales.put("day_time", 1);
            log.info("varibales:{}",varibales);
            taskService.complete(taskId, varibales);
        }
        // 正常流程
        else {
            taskService.complete(taskId);
        }

        return resultJson;
    }


    @Override
    public Boolean withdraw(JSONObject jsonObject) {
        try {
            Long taskId = jsonObject.getLong("taskId");
            this.saveCurrentTaskIds(jsonObject);
            // 首先需要判断能不能返回上一节点
            Task task = taskService.getTask(taskId);
            if (taskService.canWithdraw(task)) {
                taskService.withdraw(taskId);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean cancelTask(JSONObject jsonObject) {
        try {
            this.saveCurrentTaskIds(jsonObject);
            taskService.cancelTask(jsonObject.getLong("taskId"));

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }

    private void saveCurrentTaskIds(JSONObject jsonObject) {
        Long taskId = jsonObject.getLong("taskId");
        String onlineTableId = jsonObject.getString("onlineTableId");
        String onlineDataId = jsonObject.getString("onlineDataId");
        Task task = taskService.getTask(taskId);

        // 当前节点的form、online信息
        String currentTaskAllId = task.getUrl();
        String[] currentTaskAllIdList = currentTaskAllId.split("-");
        // 如果长度只有1就说明只包含了formId，并没有包含online的两个id
        if (currentTaskAllIdList.length == 1) {
            String url = currentTaskAllId.replace("-","") + "-" + onlineTableId + "-" + onlineDataId;
            task.setUrl(url);
            taskDao.save(task);
        }
    }

    @Override
    public JSONObject getAssignee(JSONObject requestJsonObject) {
        return assigneeProviderListServletHandler.getAssignee(requestJsonObject);
    }

    @Override
    public List<TaskVO> getClaimTaskList(JSONObject jsonObject) {
        List<TaskVO> listReturn = new ArrayList<>();

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
            TaskVO taskVO = new TaskVO();
            ProcessDefinition processDefinition = processService.getProcessById(task.getProcessId());

            if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                    taskVO.setId(task.getId());
                    taskVO.setNodeName(task.getNodeName());
                    taskVO.setDescription(task.getDescription());
                    taskVO.setAssignee(task.getAssignee());
                    taskVO.setCreateDate(task.getCreateDate());
                    taskVO.setProcessId(task.getProcessId());
                    taskVO.setState(task.getState().toString());
                    taskVO.setType(task.getType().toString());
                    taskVO.setProcessName(processDefinition.getName());
                    listReturn.add(taskVO);
                }
            }else{
                taskVO.setId(task.getId());
                taskVO.setNodeName(task.getNodeName());
                taskVO.setDescription(task.getDescription());
                taskVO.setAssignee(task.getAssignee());
                taskVO.setCreateDate(task.getCreateDate());
                taskVO.setProcessId(task.getProcessId());
                taskVO.setState(task.getState().toString());
                taskVO.setType(task.getType().toString());
                taskVO.setProcessName(processDefinition.getName());
                listReturn.add(taskVO);
            }
        }

        return listReturn;
    }

    @Override
    public Boolean claim(Long taskId) {
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        try {
            taskService.claim(taskId, loginUser);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<TaskVO> getPendingTakes(JSONObject jsonObject) {
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        // 需要加入别的搜索条件
        List<Task> list = taskDao.getPendingTask(loginUser, jsonObject.getLong("processId"), jsonObject.getString("taskName"), jsonObject.getDate("startTime"), jsonObject.getDate("endTime"));

        List<TaskVO> listReturn = new ArrayList<>();
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

            TaskVO taskVO = new TaskVO();
            taskVO.setFlag(0);
            if (prevWithdrawTaskList.size() > 0) {
                taskVO.setFlag(1);
            }

            if (StringUtils.isNotEmpty(jsonObject.getString("categoryId"))){
                if (processDefinition.getCategoryId().equals(jsonObject.getString("categoryId"))){
                    taskVO.setId(task.getId());
                    taskVO.setNodeName(task.getNodeName());
                    taskVO.setDescription(task.getDescription());
                    taskVO.setAssignee(task.getAssignee());
                    taskVO.setCreateDate(endDate);
                    taskVO.setProcessId(task.getProcessId());
                    taskVO.setState(task.getState().toString());
                    taskVO.setType(task.getType().toString());
                    taskVO.setProcessInstanceId(processInstanceId);
                    taskVO.setProcessName(processDefinition.getName());
                    listReturn.add(taskVO);
                }
            }else{
                taskVO.setId(task.getId());
                taskVO.setNodeName(task.getNodeName());
                taskVO.setDescription(task.getDescription());
                taskVO.setAssignee(task.getAssignee());
                taskVO.setCreateDate(endDate);
                taskVO.setProcessId(task.getProcessId());
                taskVO.setState(task.getState().toString());
                taskVO.setType(task.getType().toString());
                taskVO.setProcessInstanceId(processInstanceId);
                taskVO.setProcessName(processDefinition.getName());
                listReturn.add(taskVO);
            }
        }

        return listReturn;
    }

    @Override
    public Set<String> getHandlerLists(String handlerType) {
        // 先获取整个系统中所有的bean，再根据输入的参数进行筛选
        Map<String,Set<String>> handerMap=new HashMap<String,Set<String>>();
        handerMap.put(NodeEventHandler.class.getSimpleName(), applicationContext.getBeansOfType(NodeEventHandler.class).keySet());
        handerMap.put(ProcessEventHandler.class.getSimpleName(), applicationContext.getBeansOfType(ProcessEventHandler.class).keySet());
        handerMap.put(DecisionHandler.class.getSimpleName(), applicationContext.getBeansOfType(DecisionHandler.class).keySet());
        handerMap.put(AssignmentHandler.class.getSimpleName(), applicationContext.getBeansOfType(AssignmentHandler.class).keySet());
        handerMap.put(ConditionHandler.class.getSimpleName(), applicationContext.getBeansOfType(ConditionHandler.class).keySet());
        handerMap.put(ActionHandler.class.getSimpleName(), applicationContext.getBeansOfType(ActionHandler.class).keySet());
        handerMap.put(ForeachHandler.class.getSimpleName(), applicationContext.getBeansOfType(ForeachHandler.class).keySet());
        handerMap.put(ReminderHandler.class.getSimpleName(), applicationContext.getBeansOfType(ReminderHandler.class).keySet());
        handerMap.put(CountersignHandler.class.getSimpleName(), applicationContext.getBeansOfType(CountersignHandler.class).keySet());
        handerMap.put(TaskListener.class.getSimpleName(), applicationContext.getBeansOfType(TaskListener.class).keySet());
        Set<String> set=new HashSet<String>();
        for(FormTemplateProvider provider:applicationContext.getBeansOfType(FormTemplateProvider.class).values()){
            set.add(provider.getFormTemplate());
        }
        handerMap.put(FormTemplateProvider.class.getSimpleName(), set);

        String loginUser=EnvironmentUtils.getEnvironment().getLoginUser();
        if(loginUser==null){
            throw new IllegalArgumentException("Please login first.");
        }

        if(StringUtils.isEmpty(handlerType)){
            throw new IllegalArgumentException("Parameter handler can not be null");
        }

        return handerMap.get(handlerType);
    }

    @Override
    public JSONObject handle(JSONObject jsonObject) {
        JSONObject json = new JSONObject();
        List<HistoryTask> historyTasks = historyTaskDao.findByProcessInstanceId(jsonObject.getLong("processInstanceId"));
        // 过去任务的表单id，onlineid
        List<String> historyIdsList = new ArrayList<>();
        for (HistoryTask historyTask : historyTasks) {
            historyIdsList.add(historyTask.getUrl());
        }
        json.put("oldIdsList", historyIdsList);

        Task task = taskService.getTask(jsonObject.getLong("taskId"));
        json.put("nowIds", task.getUrl());

        return json;
    }

    @Override
    public JSONArray nodePower(JSONObject jsonObject) {

        Long taskId = jsonObject.getLong("taskId");
        Long processId = jsonObject.getLong("processId");
        Long processInstanceId = jsonObject.getLong("processInstanceId");
        Task task = taskService.getTask(taskId);

        //获取原始Json
        byte[] blobValue = blobDao.findblobProcessId(processId).getBlobValue();
        JSONObject processByteMap = JSONObject.parseObject(blobValue, JSONObject.class);
        List<JSONObject> nodeList = JSON.parseArray(processByteMap.getString("nodeList"), JSONObject.class);

        //获取当前节点
        JSONObject nowNode = nodeList.stream()
                .filter(jsonObjectNode -> jsonObjectNode.getString("name").equals(task.getNodeName()))
                .findFirst().orElse(null);

        JSONArray jsonArray = nowNode.getJSONArray("button_permission");

        return jsonArray;
    }
}

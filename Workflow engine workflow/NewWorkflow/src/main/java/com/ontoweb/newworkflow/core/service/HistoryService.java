package com.ontoweb.newworkflow.core.service;

import com.ontoweb.newworkflow.core.model.HistoryActivity;
import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.HistoryVariable;
import com.ontoweb.newworkflow.core.query.HistoryProcessInstanceQuery;
import com.ontoweb.newworkflow.core.query.HistoryProcessVariableQuery;
import com.ontoweb.newworkflow.core.query.HistoryTaskQuery;

import java.util.List;

/**
 * @author     ：Ethan yan
 * @date       ：Created in 2021/8/2 上午9:55
 * @description：
 * @modified By：
 * @version:     $
 */public interface HistoryService {
    public static final String BEAN_ID="uflo.historyService";
    /**
     * 根据流程实例ID，返回当前实例产生的所有历史节点的集合
     * @param processInstanceId 流程实例ID
     * @return 返回HistoryActivity集合
     */
    List<HistoryActivity> getHistoryActivitysByProcesssInstanceId(long processInstanceId);
    /**
     * 根据历史流程实例ID，返回当前历史流程实例产生的所有历史节点的集合
     * @param historyProcessInstanceId 历史流程实例ID
     * @return 返回HistoryActivity集合
     */
    List<HistoryActivity> getHistoryActivitysByHistoryProcesssInstanceId(long historyProcessInstanceId);
    /**
     * 根据流程模版ID，返回所有的历史流程实例集合
     * @param processId 流程模版ID
     * @return 返回所有的历史流程实例集合
     */
    List<HistoryProcessInstance> getHistoryProcessInstances(long processId);
    /**
     * 根据流程实例ID，返回的对应的历史流程实例
     * @param processInstanceId 流程实例ID
     * @return 返回的对应的历史流程实例
     */
    HistoryProcessInstance getHistoryProcessInstance(long processInstanceId);
    /**
     * 根据流程实例ID，返回对应的历史任务集合
     * @param processInstanceId 流程实例ID
     * @return 返回历史任务集合
     */
    List<HistoryTask> getHistoryTasks(long processInstanceId);
    /**
     * 根据任务ID，返回对应的历史任务
     * @param taskId 任务ID
     * @return 返回历史任务
     */
    HistoryTask getHistoryTask(long taskId);
    /**
     * @return 返回创建的历史任务查询对象
     */
    HistoryTaskQuery createHistoryTaskQuery();
    /**
     * @return 返回创建创建的历史流程实例查询对象
     */
    HistoryProcessInstanceQuery createHistoryProcessInstanceQuery();
    /**
     * @return 返回创建的历史流程实例查询对象
     */
    HistoryProcessVariableQuery createHistoryProcessVariableQuery();
    /**
     * 根据历史流程实例ID，返回所有的历史流程变量
     * @param historyProcessInstanceId 历史流程实例ID
     * @return 返回所有的历史流程变量
     */
    List<HistoryVariable> getHistoryVariables(long historyProcessInstanceId);
    /**
     * 根据历史流程实例ID和流程变量名，返回对应的历史流程变量对象
     * @param historyProcessInstanceId 历史流程实例ID
     * @param key 流程变量名字
     * @return 返回对应的历史流程变量对象
     */
    HistoryVariable getHistoryVariable(long historyProcessInstanceId,String key);
}

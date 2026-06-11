package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.service.TaskOpinion;
import com.ontoweb.newworkflow.core.service.TaskService;

import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:44
 * @description：回滚任务
 * @modified By：
 * @version: $
 */
public class RollbackTaskCommand implements Command<Object> {
    private Map<String, Object> variables;
    private Task task;
    private TaskOpinion opinion;
    private String targetNodeName;
    public RollbackTaskCommand(Task task,String targetNodeName,Map<String, Object> variables,TaskOpinion opinion){
        this.variables=variables;
        this.opinion=opinion;
        this.task=task;
        this.targetNodeName=targetNodeName;
    }

    @Override
    public Object execute(Context context) {
        TaskService taskService=context.getTaskService();
        taskService.forward(task, targetNodeName, variables,opinion, TaskState.Rollback);
        return null;
    }
}

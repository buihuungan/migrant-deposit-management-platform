package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.service.TaskOpinion;
import com.ontoweb.newworkflow.core.service.TaskService;

import java.util.List;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:33
 * @description：批量开启和结束任务
 * @modified By：
 * @version: $
 */
public class BatchStartAndCompleteTasksCommand implements Command<Object> {
    private List<Long> taskIds;
    private Map<String,Object> variables;
    private TaskOpinion opinion;
    public BatchStartAndCompleteTasksCommand(List<Long> taskIds,Map<String,Object> variables,TaskOpinion opinion){
        this.taskIds=taskIds;
        this.variables=variables;
        this.opinion=opinion;
    }
    @Override
    public Object execute(Context context) {
        TaskService taskService=context.getTaskService();
        for(long taskId:taskIds){
            taskService.start(taskId);
            taskService.complete(taskId, variables,opinion);
        }
        return null;
    }
}

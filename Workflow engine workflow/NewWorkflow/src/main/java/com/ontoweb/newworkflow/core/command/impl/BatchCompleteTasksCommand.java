package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.service.TaskOpinion;

import java.util.List;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:32
 * @description：批量完成任务
 * @modified By：
 * @version: $
 */
public class BatchCompleteTasksCommand implements Command<Object> {
    private List<Long> taskIds;
    private Map<String,Object> variables;
    private TaskOpinion opinion;
    public BatchCompleteTasksCommand(List<Long> taskIds,Map<String,Object> variables,TaskOpinion opinion){
        this.taskIds=taskIds;
        this.variables=variables;
        this.opinion=opinion;
    }
    @Override
    public Object execute(Context context) {
        for(long taskId:taskIds){
            context.getTaskService().complete(taskId, variables,opinion);
        }
        return null;
    }
}

package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:33
 * @description：批量开启任务
 * @modified By：
 * @version: $
 */
public class BatchStartTasksCommand  implements Command<Object> {
    private List<Long> taskIds;
    public BatchStartTasksCommand(List<Long> taskIds){
        this.taskIds=taskIds;
    }
    @Override
    public Object execute(Context context) {
        for(long taskId:taskIds){
            context.getTaskService().start(taskId);
        }
        return null;
    }
}

package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:39
 * @description：修改任务优先级
 * @modified By：
 * @version: $
 */
public class ChangeTaskPriorityCommand implements Command<Task> {
    private Task task;
    private String priority;
    public ChangeTaskPriorityCommand(Task task,String priority){
        this.task=task;
        this.priority=priority;
    }
    @Override
    public Task execute(Context context) {
        task.setPriority(priority);
        context.getSession().update(task);
        return task;
    }
}

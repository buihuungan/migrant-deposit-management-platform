package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:50
 * @description：挂起任务
 * @modified By：
 * @version: $
 */
public class SuspendTaskCommand implements Command<Task> {
    private Task task;
    public SuspendTaskCommand(Task task){
        this.task=task;
    }

    @Override
    public Task execute(Context context) {
        TaskState state=task.getState();
        if(!state.equals(TaskState.Ready) && !state.equals(TaskState.InProgress) && !state.equals(TaskState.Reserved)){
            throw new IllegalStateException("Only state is ready,reserved or inProgress task can be suspend!");
        }
        task.setPrevState(state);
        task.setState(TaskState.Suspended);
        context.getSession().update(task);
        return null;
    }
}

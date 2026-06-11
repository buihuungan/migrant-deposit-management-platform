package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:43
 * @description：重新开始任务
 * @modified By：
 * @version: $
 */
public class ResumeTaskCommand implements Command<Task> {
    private Task task;
    public ResumeTaskCommand(Task task){
        this.task=task;
    }


    @Override
    public Task execute(Context context) {
        TaskState state=task.getState();
        if(!state.equals(TaskState.Suspended)){
            throw new IllegalStateException("Only state is suspended task can be resume!");
        }
        task.setState(task.getPrevState());
        task.setPrevState(state);
        context.getSession().update(task);
        return null;
    }
}

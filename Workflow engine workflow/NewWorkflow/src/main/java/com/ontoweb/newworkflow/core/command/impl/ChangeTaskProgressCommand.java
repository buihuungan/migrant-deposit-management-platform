package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:39
 * @description：修改任务进度
 * @modified By：
 * @version: $
 */
public class ChangeTaskProgressCommand implements Command<Task> {
    private Task task;
    private int progress;
    public ChangeTaskProgressCommand(Task task,int progress){
        this.task=task;
        this.progress=progress;
    }
    @Override
    public Task execute(Context context) {
        task.setProgress(progress);
        context.getSession().update(task);
        return task;
    }
}

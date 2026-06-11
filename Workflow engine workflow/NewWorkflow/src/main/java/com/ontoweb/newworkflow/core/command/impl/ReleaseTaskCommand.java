package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:42
 * @description：释放任务
 * @modified By：
 * @version: $
 */
public class ReleaseTaskCommand implements Command<Task> {
    private Task task;
    public ReleaseTaskCommand(Task task){
        this.task=task;
    }

    @Override
    public Task execute(Context context) {
        if(StringUtils.isEmpty(task.getAssignee())){
            throw new IllegalArgumentException("Current task ["+task.getTaskName()+"] no owner,can not be release!");
        }
        task.setState(TaskState.Ready);
        task.setAssignee(null);
        context.getSession().update(task);
        return task;
    }
}

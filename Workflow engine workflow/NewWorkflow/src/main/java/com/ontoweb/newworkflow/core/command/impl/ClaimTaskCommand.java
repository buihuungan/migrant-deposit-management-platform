package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:40
 * @description：认领任务
 * @modified By：
 * @version: $
 */
public class ClaimTaskCommand implements Command<Task> {
    private Task task;
    private String user;
    public ClaimTaskCommand(Task task,String user){
        this.task=task;
        this.user=user;
    }
    @Override
    public Task execute(Context context) {
        if(!task.getType().equals(TaskType.Participative)){
            throw new IllegalStateException("Current task ["+task.getTaskName()+"] is not a participative task!");
        }
        if(StringUtils.isNotEmpty(task.getAssignee())){
            throw new IllegalStateException("Current task ["+task.getTaskName()+"] has belonged to ["+task.getAssignee()+"].");
        }
        if(StringUtils.isBlank(user)){
            throw new IllegalStateException("User can not be null when task claim.");
        }
        task.setState(TaskState.Reserved);
        task.setAssignee(user);
        context.getSession().update(task);
        return task;
    }
}

package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:36
 * @description：修改任务指定者
 * @modified By：
 * @version: $
 */
public class ChangeTaskAssigneeCommand implements Command<Object> {
    private long taskId;
    private String username;
    public ChangeTaskAssigneeCommand(long taskId,String username){
        this.taskId=taskId;
        this.username=username;
    }
    @Override
    public Object execute(Context context) {
        Task task=context.getTaskService().getTask(taskId);
        if(StringUtils.isNotEmpty(task.getOwner())){
            task.setAssignee(username);
            context.getSession().update(task);
        }else{
            throw new IllegalArgumentException("Task "+taskId+" is not the owner,So can not change task assignee.");
        }
        return null;
    }
}

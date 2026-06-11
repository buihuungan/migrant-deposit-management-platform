package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.reminder.TaskReminder;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:50
 * @description：删除任务提醒
 * @modified By：
 * @version: $
 */
public class DeleteTaskReminderCommand implements Command<Object> {
    private long taskReminderId;
    public DeleteTaskReminderCommand(long taskReminderId){
        this.taskReminderId=taskReminderId;
    }
    @Override
    public Object execute(Context context) {
        context.getSession().createQuery("delete "+ TaskReminder.class.getName()+" where id=:id").setLong("id", taskReminderId).executeUpdate();
        return null;
    }
}

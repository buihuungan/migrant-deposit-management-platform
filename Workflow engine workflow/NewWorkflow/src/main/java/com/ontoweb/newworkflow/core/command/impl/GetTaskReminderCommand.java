package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.reminder.TaskReminder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:09
 * @description：获取任务提示
 * @modified By：
 * @version: $
 */
public class GetTaskReminderCommand implements Command<List<TaskReminder>> {
    private long taskId;
    public GetTaskReminderCommand(long taskId){
        this.taskId=taskId;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<TaskReminder> execute(Context context) {
        Criteria criteria=context.getSession().createCriteria(TaskReminder.class);
        if(taskId>0){
            criteria.add(Restrictions.eq("taskId",taskId));
        }
        return criteria.list();
    }
}

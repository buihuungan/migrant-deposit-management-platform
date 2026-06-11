package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskParticipator;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import com.ontoweb.newworkflow.core.service.SchedulerService;
import org.hibernate.Session;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:49
 * @description：删除任务
 * @modified By：
 * @version: $
 */
public class DeleteTaskCommand implements Command<Task> {
    private Task task;
    public DeleteTaskCommand(Task task){
        this.task=task;
    }
    @Override
    public Task execute(Context context) {
        Session session=context.getSession();
        if(task.getType().equals(TaskType.Participative)){
            session.createQuery("delete "+ TaskParticipator.class.getName()+" where taskId=:taskId").setLong("taskId", task.getId()).executeUpdate();
        }
        String hql="delete "+ HistoryTask.class.getName()+" where taskId=:taskId";
        session.createQuery(hql).setLong("taskId", task.getId()).executeUpdate();
        session.delete(task);
        SchedulerService schedulerService=(SchedulerService)context.getApplicationContext().getBean(SchedulerService.BEAN_ID);
        schedulerService.removeReminderJob(task);
        return task;
    }
}

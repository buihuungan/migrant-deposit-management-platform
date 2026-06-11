package com.ontoweb.newworkflow.core.service.impl.job;

import com.ontoweb.newworkflow.core.model.task.reminder.TaskReminder;
import com.ontoweb.newworkflow.core.service.SchedulerService;
import com.ontoweb.newworkflow.core.service.TaskService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:21
 * @description：
 * @modified By：
 * @version: $
 */
public class ScanReminderJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScanReminderJobDetail detail=(ScanReminderJobDetail)context.getJobDetail();
        TaskService taskService=detail.getTaskService();
        List<Long> reminderTaskList=detail.getReminderTaskList();
        List<TaskReminder> reminders=taskService.getAllTaskReminders();
        SchedulerService schedulerService=detail.getSchedulerService();
        List<Long> list=new ArrayList<Long>();
        for(TaskReminder reminder:reminders){
            long taskId=reminder.getTaskId();
            list.add(reminder.getId());
            if(reminderTaskList.contains(taskId)){
                continue;
            }
            schedulerService.addReminderJob(reminder,null,null);
            reminderTaskList.add(reminder.getId());
        }
        List<Long> removeList=new ArrayList<Long>();
        for(long reminderId:reminderTaskList){
            if(list.contains(reminderId)){
                continue;
            }
            schedulerService.deleteJob(reminderId);
            removeList.add(reminderId);
        }
        for(Long reminderId:removeList){
            reminderTaskList.remove(reminderId);
        }
    }
}

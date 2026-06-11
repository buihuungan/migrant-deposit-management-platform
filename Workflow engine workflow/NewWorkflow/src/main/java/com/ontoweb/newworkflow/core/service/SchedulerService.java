package com.ontoweb.newworkflow.core.service;

import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.reminder.TaskReminder;
import com.ontoweb.newworkflow.core.process.node.reminder.CalendarInfo;
import org.quartz.Calendar;
import org.quartz.Scheduler;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午2:21
 * @description：计划相关服务，调度器
 * @modified By：
 * @version: $
 */
public interface SchedulerService {
    public static final String BEAN_ID="uflo.schedulerService";
    Scheduler getScheduler();
    void addReminderJob(TaskReminder reminder, ProcessInstance processInstance, Task task);
    void removeReminderJob(Task task);
    Calendar buildCalendar(List<CalendarInfo> infos);
    void resetScheduer();
    void deleteJob(long reminderId);
}

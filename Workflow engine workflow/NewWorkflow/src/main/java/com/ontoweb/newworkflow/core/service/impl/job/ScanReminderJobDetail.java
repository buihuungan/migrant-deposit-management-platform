package com.ontoweb.newworkflow.core.service.impl.job;

import com.ontoweb.newworkflow.core.service.SchedulerService;
import com.ontoweb.newworkflow.core.service.TaskService;
import org.quartz.impl.JobDetailImpl;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:21
 * @description：
 * @modified By：
 * @version: $
 */
public class ScanReminderJobDetail extends JobDetailImpl {
    private static final long serialVersionUID = -3903960284542345231L;
    private TaskService taskService;
    private SchedulerService schedulerService;
    private List<Long> reminderTaskList;
    public TaskService getTaskService() {
        return taskService;
    }
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    public List<Long> getReminderTaskList() {
        return reminderTaskList;
    }
    public void setReminderTaskList(List<Long> reminderTaskList) {
        this.reminderTaskList = reminderTaskList;
    }
    public SchedulerService getSchedulerService() {
        return schedulerService;
    }
    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }
}

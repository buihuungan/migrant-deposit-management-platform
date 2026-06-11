package com.ontoweb.newworkflow.core.service.impl.job;

import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.process.handler.ReminderHandler;
import org.quartz.impl.JobDetailImpl;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:20
 * @description：
 * @modified By：
 * @version: $
 */
public class ReminderJobDetail extends JobDetailImpl {
    private static final long serialVersionUID = -5400934958181696022L;
    private ProcessInstance processInstance;
    private Task task;
    private ReminderHandler reminderHandlerBean;
    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public ReminderHandler getReminderHandlerBean() {
        return reminderHandlerBean;
    }

    public void setReminderHandlerBean(ReminderHandler reminderHandlerBean) {
        this.reminderHandlerBean = reminderHandlerBean;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}

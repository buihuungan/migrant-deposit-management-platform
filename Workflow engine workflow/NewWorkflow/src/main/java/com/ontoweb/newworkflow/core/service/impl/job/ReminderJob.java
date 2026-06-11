package com.ontoweb.newworkflow.core.service.impl.job;

import com.ontoweb.newworkflow.core.process.handler.ReminderHandler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:20
 * @description：
 * @modified By：
 * @version: $
 */
public class ReminderJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ReminderJobDetail jobDetail=(ReminderJobDetail)context.getJobDetail();
        ReminderHandler handler=jobDetail.getReminderHandlerBean();
        handler.execute(jobDetail.getProcessInstance(), jobDetail.getTask());
    }
}

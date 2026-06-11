package com.ontoweb.newworkflow.core.service.impl;

import com.ontoweb.newworkflow.core.heartbeat.InstanceDetection;
import com.ontoweb.newworkflow.core.service.SchedulerService;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:16
 * @description：
 * @modified By：
 * @version: $
 */
public class JobSchedulerServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationContext context= EnvironmentUtils.getEnvironment().getApplicationContext();
        SchedulerService schedulerService=(SchedulerService)context.getBean(SchedulerService.BEAN_ID);
        Scheduler scheduler=schedulerService.getScheduler();
        if(scheduler!=null){
            try {
                if(!scheduler.isShutdown()){
                    scheduler.shutdown(false);
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        InstanceDetection detection=(InstanceDetection)context.getBean(InstanceDetection.BEAN_ID);
        scheduler=detection.getScheduler();
        if(scheduler!=null){
            try {
                if(!scheduler.isShutdown()){
                    scheduler.shutdown(false);
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.ontoweb.newworkflow.core.heartbeat;

import com.ontoweb.newworkflow.core.service.SchedulerService;
import org.hibernate.SessionFactory;
import org.quartz.impl.JobDetailImpl;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:23
 * @description：
 * @modified By：
 * @version: $
 */
public class DetectionJobDetail extends JobDetailImpl {
    private static final long serialVersionUID = 422572385273663713L;
    private SessionFactory sessionFactory;
    private String currentInstanceName;
    private String[] jobInstanceNames;
    private SchedulerService schedulerService;
    public DetectionJobDetail(SessionFactory sessionFactory,String currentInstanceName, String[] jobInstanceNames,SchedulerService schedulerService) {
        this.sessionFactory = sessionFactory;
        this.currentInstanceName = currentInstanceName;
        this.jobInstanceNames = jobInstanceNames;
        this.schedulerService=schedulerService;
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public String getCurrentInstanceName() {
        return currentInstanceName;
    }
    public String[] getJobInstanceNames() {
        return jobInstanceNames;
    }
    public SchedulerService getSchedulerService() {
        return schedulerService;
    }
}

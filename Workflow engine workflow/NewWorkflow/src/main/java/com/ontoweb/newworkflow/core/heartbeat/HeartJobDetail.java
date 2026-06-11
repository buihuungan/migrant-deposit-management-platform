package com.ontoweb.newworkflow.core.heartbeat;

import org.hibernate.SessionFactory;
import org.quartz.impl.JobDetailImpl;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:25
 * @description：
 * @modified By：
 * @version: $
 */
public class HeartJobDetail extends JobDetailImpl {
    private static final long serialVersionUID = 1653542848201958245L;
    private SessionFactory sessionFactory;
    private String currentInstanceName;
    public HeartJobDetail(SessionFactory sessionFactory,String currentInstanceName) {
        this.sessionFactory = sessionFactory;
        this.currentInstanceName = currentInstanceName;
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    public String getCurrentInstanceName() {
        return currentInstanceName;
    }
    public void setCurrentInstanceName(String currentInstanceName) {
        this.currentInstanceName = currentInstanceName;
    }
}

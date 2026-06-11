package com.ontoweb.newworkflow.core.env.impl;

import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.env.EnvironmentProvider;
import com.ontoweb.newworkflow.core.expr.ExpressionContext;
import com.ontoweb.newworkflow.core.service.IdentityService;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.TaskService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午8:51
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class ContextImpl implements ApplicationContextAware, Context {
//    @Autowired
    private SessionFactory sessionFactory;

    private ApplicationContext applicationContext;

    @Autowired
    private CommandService commandService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private ExpressionContext expressionContext;
    private IdentityService identityService;
    private TaskService taskService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Session getSession(){
        return entityManager.unwrap(org.hibernate.Session.class);
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CommandService getCommandService() {
        return commandService;
    }

    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Override
    public ProcessService getProcessService() {
        return processService;
    }

    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    @Override
    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }

    @Override
    public IdentityService getIdentityService() {
        return identityService;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public TaskService getTaskService() {
        return taskService;
    }
}

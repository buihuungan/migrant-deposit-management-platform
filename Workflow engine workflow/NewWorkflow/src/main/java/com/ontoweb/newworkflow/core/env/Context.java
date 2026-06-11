package com.ontoweb.newworkflow.core.env;

import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.expr.ExpressionContext;
import com.ontoweb.newworkflow.core.service.IdentityService;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.TaskService;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:13
 * @description：上下文
 * @modified By：
 * @version: $
 */
public interface Context {
    Session getSession();
    ApplicationContext getApplicationContext();
    CommandService getCommandService();
    ProcessService getProcessService();
    ExpressionContext getExpressionContext();
    IdentityService getIdentityService();
    TaskService getTaskService();
}

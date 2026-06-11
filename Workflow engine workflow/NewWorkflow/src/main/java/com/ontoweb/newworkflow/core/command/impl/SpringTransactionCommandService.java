package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.env.EnvironmentProvider;
import com.ontoweb.newworkflow.core.env.impl.ContextImpl;
import com.ontoweb.newworkflow.core.expr.ExpressionContext;
import com.ontoweb.newworkflow.core.service.IdentityService;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.TaskService;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:48
 * @description：spring事务相关
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class SpringTransactionCommandService implements CommandService{

    @Autowired
    private ContextImpl context;
    @Autowired
    private TransactionTemplate transactionTemplate;

    private int springPropagationBehaviour = TransactionDefinition.PROPAGATION_REQUIRED;
    private int newSpringPropagationBehaviour = TransactionDefinition.PROPAGATION_REQUIRES_NEW;

    @Override
    public <T> T executeCommand(final Command<T> command) {
//        template = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(springPropagationBehaviour);
        return transactionTemplate.execute(new TransactionCallback<T>() {
            @Override
            public T doInTransaction(TransactionStatus status) {
//                try {
                log.info("context:{}" + context);
                    return command.execute(context);
//                } catch (Exception e) {
//                    status.setRollbackOnly();
//                    log.error("exception:{}" + e.getMessage());
//                    return null;
//                }
            }

        });
    }

    @Override
    public <T> T executeCommandInNewTransaction(final Command<T> command) {
//        TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(newSpringPropagationBehaviour);
        return transactionTemplate.execute(new TransactionCallback<T>() {
            @Override
            public T doInTransaction(TransactionStatus status) {
                return command.execute(context);
            }

        });
    }
}

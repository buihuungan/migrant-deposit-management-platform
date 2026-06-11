package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:05
 * @description：获取流程实例
 * @modified By：
 * @version: $
 */
@Service
public class GetProcessInstanceCommand{
    @PersistenceContext
    private EntityManager entityManager;

    public ProcessInstance getProcessInstanceCommand(long processInstanceId) {

        return entityManager.find(ProcessInstance.class, processInstanceId);
    }
}

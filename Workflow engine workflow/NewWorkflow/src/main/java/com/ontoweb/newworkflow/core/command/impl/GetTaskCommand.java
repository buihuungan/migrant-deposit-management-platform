package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:07
 * @description：获取任务信息
 * @modified By：
 * @version: $
 */
@Service
public class GetTaskCommand {
    @PersistenceContext
    private EntityManager entityManager;


    public Task getTaskCommand(long taskId) {
        Task task = entityManager.find(Task.class, taskId);

        return task;
    }
}

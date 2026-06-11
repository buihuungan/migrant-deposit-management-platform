package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.handler.dao.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:50
 * @description：开启任务
 * @modified By：
 * @version: $
 */
@Service
public class StartTaskCommand{
    @Autowired
    private TaskDao taskDao;

    public Task startTaskCommand(Task task) {
        if(!task.getState().equals(TaskState.Created) && !task.getState().equals(TaskState.Reserved)){
            throw new IllegalStateException("Only state is Created or Reserved task can be start!");
        }
        task.setState(TaskState.InProgress);
        taskDao.save(task);

        return task;
    }
}

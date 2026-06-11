package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.service.TaskOpinion;
import com.ontoweb.newworkflow.core.service.TaskService;
import com.ontoweb.newworkflow.handler.utils.SpringBootBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:51
 * @description：撤销任务
 * @modified By：
 * @version: $
 */
public class WithdrawTaskCommand implements Command<Object> {

    private Map<String, Object> variables;
    private Task task;
    private TaskOpinion opinion;
    public WithdrawTaskCommand(Task task,Map<String, Object> variables,TaskOpinion opinion){
        this.variables=variables;
        this.opinion=opinion;
        this.task=task;
    }

    @Override
    public Object execute(Context context) {
        TaskService taskService = SpringBootBeanUtil.getBean(TaskService.class);
        if(!taskService.canWithdraw(task)){
            throw new IllegalArgumentException("Task "+task.getTaskName()+" can not forward to "+task.getPrevTask()+" node.");
        }
        taskService.forward(task, task.getPrevTask(), variables,opinion, TaskState.Withdraw);
        return null;
    }
}

package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.process.listener.TaskListener;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.process.node.TaskNode;
import com.ontoweb.newworkflow.core.process.node.reminder.DueDefinition;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.SchedulerService;
import com.ontoweb.newworkflow.core.service.TaskOpinion;
import com.ontoweb.newworkflow.handler.dao.TaskDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:41
 * @description：完成任务
 * @modified By：
 * @version: $
 */
@Slf4j
@Service
public class CompleteTaskCommand{
    @Autowired
    private SaveProcessInstanceVariablesCommand saveProcessInstanceVariablesCommand;
    @Autowired
    private SaveHistoryTaskCommand saveHistoryTaskCommand;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private ProcessService processService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private Context context;

    public Task completeTaskCommand(Task task,String flowName,TaskOpinion opinion,Map<String,Object> variables) {
        TaskState state=task.getState();
        if(!state.equals(TaskState.InProgress)
                && !state.equals(TaskState.Forwarded)
                && !state.equals(TaskState.Rollback)
                && !state.equals(TaskState.Withdraw)){
            throw new IllegalStateException("Please change task state to inProgress first!");
        }
        if(state.equals(TaskState.InProgress)){
            task.setProgress(100);
        }
        task.setState(TaskState.Completed);
        task.setEndDate(new Date());
        // Session session=context.getSession();
        if(opinion!=null){
            task.setOpinion(opinion.getOpinion());
        }
        taskDao.save(task);
		/*
		if(task.getType().equals(TaskType.Participative)){
			session.createQuery("delete "+TaskParticipator.class.getName()+" where taskId=:taskId").setLong("taskId", task.getId()).executeUpdate();
		}
		session.delete(task);
		*/
        ProcessInstance processInstance=processService.getProcessInstanceById(task.getProcessInstanceId());
        ProcessDefinition process=processService.getProcessById(task.getProcessId());

        Node node=process.getNode(task.getNodeName());
        if(node instanceof TaskNode){
            TaskNode taskNode=(TaskNode)node;
            DueDefinition dueDefinition=taskNode.getCustomDueDefinition(processInstance, task, context.getApplicationContext());
            if(dueDefinition==null){
                dueDefinition=taskNode.getDueDefinition();
            }
            if(dueDefinition!=null){
                schedulerService.removeReminderJob(task);
            }
            String taskListenerBean=taskNode.getTaskListenerBean();
            if(StringUtils.isNotEmpty(taskListenerBean)){
                TaskListener taskListener=(TaskListener)context.getApplicationContext().getBean(taskListenerBean);
                taskListener.onTaskComplete(context, task);
            }
        }
        saveHistoryTaskCommand.saveHistoryTaskCommand(task,processInstance);

        if(variables!=null && variables.size()>0){
            context.getExpressionContext().addContextVariables(processInstance, variables);
            saveProcessInstanceVariablesCommand.saveProcessInstanceVariablesCommand(processInstance, variables);
        }
        node.leave(context,processInstance,flowName);
        return task;
    }
}

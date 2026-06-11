package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import com.ontoweb.newworkflow.core.process.listener.GlobalTaskListener;
import com.ontoweb.newworkflow.core.process.listener.TaskListener;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.process.node.TaskNode;
import com.ontoweb.newworkflow.core.query.TaskQuery;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.util.Collection;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:30
 * @description：添加会签任务
 * @modified By：
 * @version: $
 */
public class AddCountersignCommand implements Command<Task> {
    private Task task;
    private String username;
    public AddCountersignCommand(Task task,String username){
        this.task=task;
        this.username=username;
    }
    @Override
    public Task execute(Context context) {
        if(!TaskType.Countersign.equals(task.getType())){
            throw new IllegalArgumentException("Task "+task.getId()+" is not a countersign task.");
        }
        Session session=context.getSession();
        TaskQuery query=context.getTaskService().createTaskQuery();
        query.processInstanceId(task.getProcessInstanceId());
        query.nodeName(task.getNodeName());
        int count=0;
        for(Task t:query.list()){
            count=t.getCountersignCount();
            t.setCountersignCount(count+1);
            session.update(t);
        }

        ProcessDefinition pd=context.getProcessService().getProcessById(task.getProcessId());
        Node node=pd.getNode(task.getNodeName());
        if(node!=null && (node instanceof TaskNode)) {
            TaskNode taskNode=(TaskNode)node;
            String taskListenerBean=taskNode.getTaskListenerBean();
            if(StringUtils.isNotEmpty(taskListenerBean)){
                TaskListener taskListener=(TaskListener)context.getApplicationContext().getBean(taskListenerBean);
                taskListener.onTaskCreate(context, task);
            }
        }

        Collection<GlobalTaskListener> coll=context.getApplicationContext().getBeansOfType(GlobalTaskListener.class).values();
        for(GlobalTaskListener listener:coll){
            listener.onTaskCreate(context, task);
        }

        Task newTask=new Task();
        newTask.setAssignee(username);
        newTask.setBusinessId(task.getBusinessId());
        newTask.setCountersignCount(count+1);
        newTask.setCreateDate(new Date());
        newTask.setDateUnit(task.getDateUnit());
        newTask.setDescription(task.getDescription());
        newTask.setDuedate(task.getDuedate());
        newTask.setId(IDGenerator.getInstance().nextId());
        newTask.setNodeName(task.getNodeName());
        newTask.setOwner(task.getOwner());
        newTask.setPrevTask(task.getPrevTask());
        newTask.setProcessId(task.getProcessId());
        newTask.setProcessInstanceId(task.getProcessInstanceId());
        newTask.setState(TaskState.Created);
        newTask.setRootProcessInstanceId(task.getRootProcessInstanceId());
        newTask.setTaskName(task.getTaskName());
        newTask.setType(task.getType());
        newTask.setUrl(task.getUrl());
        newTask.setSubject(task.getSubject());
        newTask.setPriority(task.getPriority());
        session.save(newTask);

        return newTask;
    }
}

package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryActivity;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskParticipator;
import com.ontoweb.newworkflow.core.model.variable.Variable;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.process.node.TaskNode;
import com.ontoweb.newworkflow.core.service.SchedulerService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:46
 * @description：删除流程实例
 * @modified By：
 * @version: $
 */
public class DeleteProcessInstanceCommand implements Command<Object> {
    private ProcessInstance processInstance;
    public DeleteProcessInstanceCommand(ProcessInstance processInstance){
        this.processInstance=processInstance;
    }
    @Override
    public Object execute(Context context) {
        ProcessDefinition process=context.getProcessService().getProcessById(processInstance.getProcessId());
        deleteProcessInstance(context,processInstance,process);
        return null;
    }
    @SuppressWarnings("unchecked")
    public Object deleteProcessInstance(Context context,ProcessInstance pi,ProcessDefinition process) {
        Session session=context.getSession();
        List<ProcessInstance> instances=session.createQuery("from "+ProcessInstance.class.getName()+" where parentId=:parentId").setLong("parentId",pi.getId()).list();
        for(ProcessInstance instance:instances){
            ProcessDefinition subProcess=context.getProcessService().getProcessById(instance.getProcessId());
            deleteProcessInstance(context,instance,subProcess);
            session.createQuery("delete "+ Variable.class.getName()+" where processInstanceId=:pIId").setLong("pIId",instance.getId()).executeUpdate();
        }
        session.createQuery("delete "+Variable.class.getName()+" where processInstanceId=:processInstanceId or rootProcessInstanceId=:rootId")
                .setLong("processInstanceId",pi.getId()).setLong("rootId",pi.getRootId()).executeUpdate();


        String currentNodeName=pi.getCurrentNode();
        if(StringUtils.isNotBlank(currentNodeName)){
            Node currentNode=process.getNode(currentNodeName);
            if(currentNode!=null && !(currentNode instanceof TaskNode)){
                currentNode.cancel(context, pi);
            }
        }

        session.createQuery("delete "+ HistoryTask.class.getName()+" where processInstanceId=:processInstanceId or rootProcessInstanceId=:rootId")
                .setLong("processInstanceId", pi.getId()).setLong("rootId",pi.getRootId()).executeUpdate();
        session.createQuery("delete "+ HistoryActivity.class.getName()+" where processInstanceId=:processInstanceId or rootProcessInstanceId=:rootId")
                .setLong("processInstanceId", pi.getId()).setLong("rootId",pi.getRootId()).executeUpdate();

        Collection<Task> tasks=session.createCriteria(Task.class).add(Restrictions.eq("processInstanceId", pi.getId())).list();
        SchedulerService schedulerService=(SchedulerService)context.getApplicationContext().getBean(SchedulerService.BEAN_ID);
        for(Task task:tasks){
            schedulerService.removeReminderJob(task);
            session.createQuery("delete "+ TaskParticipator.class.getName()+" where taskId=:taskId").setLong("taskId", task.getId()).executeUpdate();
            session.delete(task);
        }
        session.delete(pi);
        context.getExpressionContext().removeContext(pi);
        return null;
    }
}

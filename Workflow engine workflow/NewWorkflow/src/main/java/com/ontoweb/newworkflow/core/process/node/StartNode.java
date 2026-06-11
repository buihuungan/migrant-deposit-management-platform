package com.ontoweb.newworkflow.core.process.node;

import com.ontoweb.newworkflow.core.command.impl.SaveHistoryTaskCommand;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.CancelTaskInterceptor;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import com.ontoweb.newworkflow.core.process.security.ComponentAuthority;
import com.ontoweb.newworkflow.core.service.StartProcessInfo;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import com.ontoweb.newworkflow.handler.utils.SpringBootBeanUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午2:40
 * @description：开始节点
 * @modified By：
 * @version: $
 */
@Data
public class StartNode extends Node{

    private static final long serialVersionUID = 3876654522900406746L;
    private String taskName;
    private String url;
    private String formTemplate;
    private List<FormElement> formElements;
    private List<ComponentAuthority> componentAuthorities;
    private SaveHistoryTaskCommand saveHistoryTaskCommand;

    public StartNode(){
        saveHistoryTaskCommand = SpringBootBeanUtil.getBean(SaveHistoryTaskCommand.class);
    }

    @Override
    public boolean enter(Context context, ProcessInstance processInstance) {
        StartProcessInfo startProcessInfo=(StartProcessInfo)processInstance.getMetadata(StartProcessInfo.KEY);
        Task task=new Task();
        task.setId(IDGenerator.getInstance().nextId());
        task.setNodeName(getName());
        if(StringUtils.isEmpty(taskName)){
            taskName=getName();
        }
        task.setRootProcessInstanceId(processInstance.getRootId());
        task.setTaskName(context.getExpressionContext().evalString(processInstance, taskName));
        task.setDescription(getDescription());
        task.setCreateDate(new Date());
        task.setProcessId(getProcessId());
        task.setProcessInstanceId(processInstance.getId());
        task.setPrevTask(processInstance.getCurrentTask());
        task.setBusinessId(processInstance.getBusinessId());
        String targetUrl=getUrl();
        if(StringUtils.isEmpty(targetUrl)){
            targetUrl=getFormTemplate();
        }
        task.setUrl(context.getExpressionContext().evalString(processInstance, targetUrl));
        task.setType(TaskType.Normal);
        if(startProcessInfo!=null){
            task.setOwner(startProcessInfo.getPromoter());
            task.setSubject(startProcessInfo.getSubject());
            task.setAssignee(startProcessInfo.getPromoter());
            if(startProcessInfo.isCompleteStartTask()){
                task.setState(TaskState.Completed);
                task.setOpinion(startProcessInfo.getCompleteStartTaskOpinion());
                saveHistoryTaskCommand.saveHistoryTaskCommand(task,processInstance);
                return true;
            }
        }else{
            task.setOwner(processInstance.getPromoter());
            task.setAssignee(processInstance.getPromoter());
            task.setSubject(processInstance.getSubject());
        }
        task.setState(TaskState.Created);
        context.getSession().save(task);
        return false;
    }
    @Override
    public String leave(Context context,ProcessInstance processInstance,String flowName) {
        return leaveNode(context, processInstance, flowName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void cancel(Context context, ProcessInstance processInstance) {
        Session session=context.getSession();
        Collection<Task> tasks=session.createCriteria(Task.class).add(Restrictions.eq("processInstanceId", processInstance.getId())).list();
        cancelTasks(context, processInstance, tasks);
    }

    private void cancelTasks(Context context, ProcessInstance processInstance, Collection<Task> tasks) {
        Session session=context.getSession();
        Collection<CancelTaskInterceptor> interceptors=context.getApplicationContext().getBeansOfType(CancelTaskInterceptor.class).values();
        for(Task task:tasks){
            for(CancelTaskInterceptor interceptor:interceptors){
                interceptor.intercept(context, task);
            }
            session.delete(task);
            task.setState(TaskState.Canceled);
            saveHistoryTaskCommand.saveHistoryTaskCommand(task,processInstance);
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.Start;
    }
}

package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.*;
import com.ontoweb.newworkflow.core.model.variable.Variable;
import com.ontoweb.newworkflow.core.query.ProcessInstanceQuery;
import com.ontoweb.newworkflow.core.service.ProcessService;
import org.hibernate.Session;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:43
 * @description：删除流程定义
 * @modified By：
 * @version: $
 */
public class DeleteProcessDefinitionCommand implements Command<Object> {
    private ProcessDefinition processDefinition;
    public DeleteProcessDefinitionCommand(ProcessDefinition processDefinition){
        this.processDefinition=processDefinition;
    }
    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Context context) {
        ProcessService processService=context.getProcessService();
        ProcessInstanceQuery query=context.getProcessService().createProcessInstanceQuery();
        query.processId(processDefinition.getId());
        Session session=context.getSession();
        for(ProcessInstance pi:query.list()){
            processService.deleteProcessInstance(pi);
            session.createQuery("delete "+ Variable.class.getName()+" where processInstanceId=:processInstanceId").setLong("processInstanceId", pi.getId()).executeUpdate();
        }

        List<HistoryProcessInstance> hisInstances=session.createQuery("from "+HistoryProcessInstance.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).list();
        for(HistoryProcessInstance instance:hisInstances){
            session.createQuery("delete "+ HistoryVariable.class.getName()+" where historyProcessInstanceId=:historyProcessInstanceId").setLong("historyProcessInstanceId", instance.getId()).executeUpdate();
        }

        session.createQuery("delete "+ Blob.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).executeUpdate();
        session.createQuery("delete "+HistoryProcessInstance.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).executeUpdate();
        session.createQuery("delete "+HistoryTask.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).executeUpdate();
        session.createQuery("delete "+HistoryActivity.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).executeUpdate();
        session.delete(processDefinition);
        return null;
    }
}

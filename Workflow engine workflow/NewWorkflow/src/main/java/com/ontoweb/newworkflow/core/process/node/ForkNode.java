package com.ontoweb.newworkflow.core.process.node;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessInstanceState;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlowImpl;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午4:10
 * @description：
 * @modified By：
 * @version: $
 */
public class ForkNode extends Node{
    private static final long serialVersionUID = -1654058426879715199L;

    @Override
    public boolean enter(Context context, ProcessInstance processInstance) {
        return true;
    }

    @Override
    public String leave(Context context, ProcessInstance processInstance, String flowName) {
        Session session=context.getSession();
        List<SequenceFlowImpl> flows=new ArrayList<SequenceFlowImpl>();
        for(SequenceFlowImpl flow:getSequenceFlows()){
            if(!flow.canExecute(context, processInstance)){
                continue;
            }
            flows.add(flow);
        }
        for(SequenceFlowImpl flow:flows){
            ProcessInstance subProcessInstance=new ProcessInstance();
            subProcessInstance.setId(IDGenerator.getInstance().nextId());
            subProcessInstance.setProcessId(getProcessId());
            subProcessInstance.setParentId(processInstance.getId());
            subProcessInstance.setCreateDate(new Date());
            subProcessInstance.setState(ProcessInstanceState.Start);
            subProcessInstance.setRootId(processInstance.getRootId());
            subProcessInstance.setParallelInstanceCount(flows.size());
            subProcessInstance.setPromoter(processInstance.getPromoter());
            subProcessInstance.setHistoryProcessInstanceId(processInstance.getHistoryProcessInstanceId());
            subProcessInstance.setCurrentTask(processInstance.getCurrentTask());
            subProcessInstance.setBusinessId(processInstance.getBusinessId());
            subProcessInstance.setTag(processInstance.getTag());
            subProcessInstance.setSubject(processInstance.getSubject());
            session.save(subProcessInstance);
            context.getExpressionContext().createContext(subProcessInstance, null);
            flow.execute(context, subProcessInstance);
            flowName=flow.getName();
        }
        return flowName;
    }
    @Override
    public void cancel(Context context, ProcessInstance processInstance) {
    }

    @Override
    public NodeType getType() {
        return NodeType.Fork;
    }
}

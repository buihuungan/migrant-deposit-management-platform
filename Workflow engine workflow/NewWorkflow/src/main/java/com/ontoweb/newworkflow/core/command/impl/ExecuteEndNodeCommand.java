package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessInstanceState;
import com.ontoweb.newworkflow.core.process.node.EndNode;
import org.hibernate.Session;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:51
 * @description：执行结束节点
 * @modified By：
 * @version: $
 */
public class ExecuteEndNodeCommand implements Command<Object> {
    private EndNode endNode;
    private ProcessInstance processInstance;
    public ExecuteEndNodeCommand(EndNode endNode,ProcessInstance processInstance){
        this.endNode=endNode;
        this.processInstance=processInstance;
    }
    @Override
    public Object execute(Context context) {
        Session session=context.getSession();
        processInstance.setState(ProcessInstanceState.End);
        session.delete(processInstance);
        if(endNode.isTerminate()){
            removeProcessInstances(processInstance,session);
        }
        return null;
    }

    private void removeProcessInstances(ProcessInstance pi,Session session){
        long pid=pi.getParentId();
        if(pid>0){
            ProcessInstance parent=(ProcessInstance)session.get(ProcessInstance.class, pid);
            parent.setState(ProcessInstanceState.End);
            session.delete(parent);
            removeProcessInstances(parent,session);
        }
    }
}

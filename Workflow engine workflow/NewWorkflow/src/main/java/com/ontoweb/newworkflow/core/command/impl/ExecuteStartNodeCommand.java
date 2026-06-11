package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.node.StartNode;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:52
 * @description：执行开始节点
 * @modified By：
 * @version: $
 */
public class ExecuteStartNodeCommand implements Command<Object> {
    private StartNode startNode;
    private ProcessInstance processInstance;
    public ExecuteStartNodeCommand(StartNode startNode,ProcessInstance processInstance){
        this.startNode=startNode;
        this.processInstance=processInstance;
    }

    @Override
    public Object execute(Context context) {
        startNode.getSequenceFlows().get(0).execute(context,processInstance);
        return null;
    }
}

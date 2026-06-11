package com.ontoweb.newworkflow.core.process.node;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.handler.ActionHandler;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:06
 * @description：
 * @modified By：
 * @version: $
 */
public class ActionNode extends Node {
    private static final long serialVersionUID = -3522477911199203025L;
    private String handlerBean;
    @Override
    public boolean enter(Context context, ProcessInstance processInstance) {
        return true;
    }

    @Override
    public String leave(Context context, ProcessInstance processInstance,String flowName) {
        ActionHandler handler=(ActionHandler)context.getApplicationContext().getBean(handlerBean);
        handler.handle(processInstance, context);
        return leaveNode(context, processInstance, flowName);
    }

    @Override
    public void cancel(Context context, ProcessInstance processInstance) {
    }

    @Override
    public NodeType getType() {
        return NodeType.Action;
    }

    public String getHandlerBean() {
        return handlerBean;
    }

    public void setHandlerBean(String handlerBean) {
        this.handlerBean = handlerBean;
    }
}

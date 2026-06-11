package com.ontoweb.newworkflow.core.process.node;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlowImpl;
import com.ontoweb.newworkflow.core.process.handler.DecisionHandler;
import org.springframework.util.StringUtils;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午8:45
 * @description：
 * @modified By：
 * @version: $
 */
public class DecisionNode extends Node {
    private static final long serialVersionUID = -6950253419921784972L;
    private String expression;
    private String handlerBean;
    private DecisionType decisionType;
    @Override
    public boolean enter(Context context, ProcessInstance processInstance) {
        return true;
    }

    @Override
    public String leave(Context context, ProcessInstance processInstance,String flowName) {
        if(decisionType.equals(DecisionType.Handler)){
            DecisionHandler handler=(DecisionHandler)context.getApplicationContext().getBean(handlerBean);
            flowName=handler.handle(context, processInstance);
        }else{
            Object obj=context.getExpressionContext().eval(processInstance, expression);

            if(obj instanceof String){
                flowName=(String)obj;
            }else{
                throw new IllegalArgumentException("Expression ["+expression+"] value type is not a String.");
            }
        }
        if(StringUtils.isEmpty(flowName)){
            throw new IllegalArgumentException("DecisionNode must be specify handlerBean or expression at least one");
        }
        SequenceFlowImpl flow=getFlow(flowName);
        if(flow==null){
            throw new IllegalArgumentException("Sequence flow ["+flowName+"] is not exist!");
        }
        flow.execute(context, processInstance);
        return flow.getName();
    }

    @Override
    public void cancel(Context context, ProcessInstance processInstance) {
    }

    @Override
    public NodeType getType() {
        return NodeType.Decision;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getHandlerBean() {
        return handlerBean;
    }

    public void setHandlerBean(String handlerBean) {
        this.handlerBean = handlerBean;
    }

    public DecisionType getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(DecisionType decisionType) {
        this.decisionType = decisionType;
    }
}

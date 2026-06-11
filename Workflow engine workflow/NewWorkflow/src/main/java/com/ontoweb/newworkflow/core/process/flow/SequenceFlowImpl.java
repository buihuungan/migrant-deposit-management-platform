package com.ontoweb.newworkflow.core.process.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ontoweb.newworkflow.core.diagram.SequenceFlowDiagram;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.handler.ConditionHandler;
import com.ontoweb.newworkflow.core.process.node.JoinNode;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.process.node.StartNode;
import com.ontoweb.newworkflow.core.process.node.TaskNode;
import com.ontoweb.newworkflow.core.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午11:15
 * @description：序列实现类
 * @modified By：
 * @version: $
 */
@Slf4j
public class SequenceFlowImpl implements SequenceFlow,java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String toNode;
    private ConditionType conditionType;
    private String expression;
    private String handlerBean;
    private long processId;
    private String g;
    private String label;
    @JsonIgnore
    private SequenceFlowDiagram diagram;
    public void execute(Context context, ProcessInstance processInstance){
        ProcessService processService=context.getProcessService();
        ProcessDefinition process=processService.getProcessById(processInstance.getProcessId());

        //执行离开节点事件---
        String sourceNodeName=processInstance.getCurrentNode();
        if(StringUtils.isNotEmpty(sourceNodeName)){
            Node source=process.getNode(processInstance.getCurrentNode());
            source.doLeaveEventHandler(context, processInstance);
            //保存节点结束历史
            source.completeActivityHistory(context, processInstance,getName());
        }


        //重设流程实例当前所在节点名称
        log.info("toNode:{}",toNode);
        Node target=process.getNode(toNode);


        if(!(target instanceof JoinNode)){
            processInstance.setCurrentNode(target.getName());

            //保存节点进入历史
            target.createActivityHistory(context, processInstance);

            //执行节点进入事件
            target.doEnterEventHandler(context, processInstance);
        }
        //执行节点进入动作
        boolean doLeave=target.enter(context,processInstance);
        if(target instanceof TaskNode || target instanceof StartNode){
            log.info("target.getName():{}",target.getName());
            processInstance.setCurrentTask(target.getName());
        }
        if(doLeave){
            target.leave(context, processInstance, null);
        }
    }

    public boolean canExecute(Context context,ProcessInstance processInstance){
        boolean result=true;
        if(conditionType==null)return result;
        if(conditionType.equals(ConditionType.Expression) && StringUtils.isNotEmpty(expression)){
            Object obj=context.getExpressionContext().eval(processInstance, expression);
            if(obj==null)return false;
            if(obj instanceof Boolean){
                result=(Boolean)obj;
            }else{
                throw new IllegalArgumentException("Expression ["+expression+"] value type is not a Boolean.");
            }
        }else if(conditionType.equals(ConditionType.Handler) && StringUtils.isNotEmpty(handlerBean)){
            ConditionHandler handler=(ConditionHandler)context.getApplicationContext().getBean(handlerBean);
            result=handler.handle(context, processInstance, this);
        }
        return result;
    }

    @Override
    public String getToNode() {
        return toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getHandlerBean() {
        return handlerBean;
    }

    public void setHandlerBean(String handlerBean) {
        this.handlerBean = handlerBean;
    }

    @Override
    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getProcessId() {
        return processId;
    }

    public void setProcessId(long processId) {
        this.processId = processId;
    }

    public SequenceFlowDiagram getDiagram() {
        return diagram;
    }

    public void setDiagram(SequenceFlowDiagram diagram) {
        this.diagram = diagram;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }
}

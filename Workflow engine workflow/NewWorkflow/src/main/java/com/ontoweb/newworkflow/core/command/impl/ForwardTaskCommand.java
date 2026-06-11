package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlowImpl;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.TaskOpinion;
import com.ontoweb.newworkflow.core.service.TaskService;
import com.ontoweb.newworkflow.handler.utils.SpringBootBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:53
 * @description：跳转节点
 * @modified By：
 * @version: $
 */
public class ForwardTaskCommand implements Command<Task> {
    private Task task;
    private String jumpTargetNodeName;
    private Map<String,Object> variables;
    private TaskOpinion opinion;
    private TaskState state;
    public ForwardTaskCommand(Task task,String jumpTargetNodeName,Map<String,Object> variables,TaskOpinion opinion,TaskState state){
        this.task=task;
        this.opinion=opinion;
        this.jumpTargetNodeName=jumpTargetNodeName;
        this.variables=variables;
        this.state=state;
    }
    @Override
    public Task execute(Context context) {
        ProcessService processService=context.getProcessService();
        ProcessDefinition processDefinition=processService.getProcessById(task.getProcessId());
        Node targetNode=processDefinition.getNode(jumpTargetNodeName);
        if(targetNode==null){
            throw new IllegalArgumentException("Target node "+jumpTargetNodeName+" is not exist!");
        }
        Node taskNode=processDefinition.getNode(task.getNodeName());
        String targetFlowName=null;
        List<SequenceFlowImpl> flows=taskNode.getSequenceFlows();
        for(SequenceFlowImpl flow:flows){
            if(flow.getToNode().equals(jumpTargetNodeName)){
                targetFlowName=flow.getName();
                break;
            }
        }
        if(targetFlowName==null){
            targetFlowName= TaskService.TEMP_FLOW_NAME_PREFIX+ UUID.randomUUID().toString();
            SequenceFlowImpl newFlow=new SequenceFlowImpl();
            newFlow.setToNode(jumpTargetNodeName);
            newFlow.setName(targetFlowName);
            newFlow.setProcessId(task.getProcessId());
            flows.add(newFlow);
        }
        ProcessInstance processInstance=processService.getProcessInstanceById(task.getProcessInstanceId());
        SaveProcessInstanceVariablesCommand saveProcessInstanceVariablesCommand = SpringBootBeanUtil.getBean(SaveProcessInstanceVariablesCommand.class);
        if(variables!=null && variables.size()>0){
            context.getExpressionContext().addContextVariables(processInstance, variables);
            saveProcessInstanceVariablesCommand.saveProcessInstanceVariablesCommand(processInstance, variables);
        }
        if(opinion!=null){
            task.setOpinion(opinion.getOpinion());
        }
        task.setEndDate(new Date());
        task.setState(state);
        context.getSession().update(task);
        SaveHistoryTaskCommand saveHistoryTaskCommand = SpringBootBeanUtil.getBean(SaveHistoryTaskCommand.class);
        saveHistoryTaskCommand.saveHistoryTaskCommand(task,processInstance);
        taskNode.leave(context, processInstance, targetFlowName);
        return task;
    }
}

package com.ontoweb.newworkflow.core.command.impl.jump;

import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlow;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlowImpl;
import com.ontoweb.newworkflow.core.process.node.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:39
 * @description：跳转节点制作
 * @modified By：
 * @version: $
 */
public class JumpNodeBuilder {
    private ProcessDefinition process;
    private List<SequenceFlow> flowStore=new ArrayList<SequenceFlow>();
    private Map<String,JumpNode> maps=new HashMap<String,JumpNode>();
    public JumpNodeBuilder(ProcessDefinition process){
        this.process=process;
    }
    public Map<String,JumpNode> buildSimulationTasks(){
        StartNode node=process.getStartNode();
        JumpNode jumpNode=new JumpNode(node.getName());
        jumpNode.setTask(true);
        jumpNode.setLevel(1);
        jumpNode.setLabel(node.getLabel());
        maps.put(jumpNode.getName(), jumpNode);
        simulation(node,jumpNode);
        return maps;
    }

    private void simulation(Node parentNode, JumpNode jumpNode){
        List<SequenceFlowImpl> flows=parentNode.getSequenceFlows();
        if(flows==null || flows.size()==0)return;
        for(SequenceFlow flow:flows){
            if(flowStore.contains(flow)){
                continue;
            }else{
                flowStore.add(flow);
            }
            String to=flow.getToNode();
            Node toNode=process.getNode(to);
            JumpNode nextJumpNode=new JumpNode(toNode.getName());
            nextJumpNode.setLabel(toNode.getLabel());
            if(jumpNode.getParent().size()>0){
                nextJumpNode.getParent().addAll(jumpNode.getParent());
            }
            if(parentNode instanceof ForkNode || parentNode instanceof ForeachNode){
                nextJumpNode.setLevel(jumpNode.getLevel()+1);
                nextJumpNode.addParent(parentNode.getName()+"-"+flow.getName());
            }else if(parentNode instanceof JoinNode){
                nextJumpNode.decreaseParent();
                nextJumpNode.setLevel(jumpNode.getLevel()-1);
            }else{
                nextJumpNode.setLevel(jumpNode.getLevel());
            }
            if(!maps.containsKey(nextJumpNode.getName()) && !(toNode instanceof ForkNode) && !(toNode instanceof JoinNode) && !(toNode instanceof ForeachNode) && !(toNode instanceof DecisionNode)){
                maps.put(nextJumpNode.getName(), nextJumpNode);
            }
            if(toNode instanceof TaskNode){
                nextJumpNode.setTask(true);
            }
            simulation(toNode,nextJumpNode);
        }
    }
}

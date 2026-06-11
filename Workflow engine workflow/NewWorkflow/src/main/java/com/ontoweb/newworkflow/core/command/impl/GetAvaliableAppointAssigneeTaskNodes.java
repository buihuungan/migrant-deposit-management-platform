package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlowImpl;
import com.ontoweb.newworkflow.core.process.node.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:55
 * @description：获取有效的任务处理人
 * @modified By：
 * @version: $
 */
public class GetAvaliableAppointAssigneeTaskNodes implements Command<List<String>> {
    public Task task;
    public GetAvaliableAppointAssigneeTaskNodes(Task task){
        this.task=task;
    }

    @Override
    public List<String> execute(Context context) {
        ProcessDefinition pd=context.getProcessService().getProcessById(task.getProcessId());
        Node node=pd.getNode(task.getNodeName());
        List<String> nodes=new ArrayList<String>();
        getAvliableNodes(node,nodes,pd);
        return nodes;
    }
    private void getAvliableNodes(Node node,List<String> nodes,ProcessDefinition pd){
        List<SequenceFlowImpl> flows=node.getSequenceFlows();
        if(flows==null || flows.size()==0)return;
        for(SequenceFlowImpl flow:flows){
            Node toNode=pd.getNode(flow.getToNode());
            if(toNode instanceof StartNode ||
                    toNode instanceof JoinNode ||
                    toNode instanceof ForeachNode ||
                    toNode instanceof SubprocessNode ||
                    toNode instanceof ForkNode){
                continue;
            }
            if(toNode instanceof TaskNode){
                TaskNode taskNode=(TaskNode)toNode;
                if(taskNode.isAllowSpecifyAssignee()){
                    nodes.add(toNode.getName());
                }
            }else{
                getAvliableNodes(toNode,nodes,pd);
            }
        }
    }
}

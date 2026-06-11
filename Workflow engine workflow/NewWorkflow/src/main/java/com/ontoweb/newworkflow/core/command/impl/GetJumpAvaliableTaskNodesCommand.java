package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.command.impl.jump.JumpNode;
import com.ontoweb.newworkflow.core.command.impl.jump.JumpNodeBuilder;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:01
 * @description：获取可以跳转的节点
 * @modified By：
 * @version: $
 */
public class GetJumpAvaliableTaskNodesCommand implements Command<List<JumpNode>> {
    private Task task;
    public GetJumpAvaliableTaskNodesCommand(Task task){
        this.task=task;
    }

    @Override
    public List<JumpNode> execute(Context context) {
        ProcessDefinition process=context.getProcessService().getProcessById(task.getProcessId());
        JumpNodeBuilder builder=new JumpNodeBuilder(process);
        Map<String,JumpNode> map=builder.buildSimulationTasks();
        JumpNode node=map.get(task.getNodeName());
        List<JumpNode> list=new ArrayList<JumpNode>();
        int level=node.getLevel();
        for(JumpNode jumpNode:map.values()){
            if(jumpNode.getLevel()==level && parentEquals(node.getParent(),jumpNode.getParent()) && !jumpNode.getName().equals(node.getName())){
                list.add(jumpNode);
            }
        }
        return list;
    }

    private boolean parentEquals(List<String> a,List<String> b){
        if(a.size()!=b.size())return false;
        for(int i=0;i<a.size();i++){
            String name=a.get(i);
            if(!name.equals(b.get(i))){
                return false;
            }
        }
        return true;
    }
}

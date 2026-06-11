package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.command.impl.jump.JumpNode;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.process.node.StartNode;
import com.ontoweb.newworkflow.core.process.node.TaskNode;
import com.ontoweb.newworkflow.core.service.TaskService;
import com.ontoweb.newworkflow.handler.utils.SpringBootBeanUtil;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:35
 * @description：返回上一级任务
 * @modified By：
 * @version: $
 */
public class CanWithdrawDecisionCommand implements Command<Boolean> {
    public Task task;
    public CanWithdrawDecisionCommand(Task task){
        this.task=task;
    }
    @Override
    public Boolean execute(Context context) {
        ProcessDefinition pd=context.getProcessService().getProcessById(task.getProcessId());
        String prevTaskName=task.getPrevTask();
        if(StringUtils.isEmpty(prevTaskName)){
            return false;
        }
        if(prevTaskName.equals(task.getNodeName())){
            return false;
        }
        TaskService taskService = SpringBootBeanUtil.getBean(TaskService.class);
        List<JumpNode> nodes = taskService.getAvaliableRollbackTaskNodes(task);
        boolean canJump=false;
        for(JumpNode jumpNode:nodes){
            if(jumpNode.getName().equals(prevTaskName)){
                canJump=true;
                break;
            }
        }
        if(!canJump){
            return false;
        }

        Node currentNode=pd.getNode(task.getNodeName());
        Node node=pd.getNode(prevTaskName);
        if(node != null && (node instanceof TaskNode || node instanceof StartNode)){
            if(node instanceof StartNode && currentNode instanceof StartNode){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }
}

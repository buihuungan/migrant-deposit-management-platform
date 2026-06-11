package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.process.node.TaskNode;
import com.ontoweb.newworkflow.core.service.ProcessService;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:07
 * @description：获取任务节点指派者
 * @modified By：
 * @version: $
 */
public class GetTaskNodeAssigneesCommand implements Command<List<String>> {
    private long taskId;
    private String taskNodeName;
    public GetTaskNodeAssigneesCommand(long taskId,String taskNodeName){
        this.taskId=taskId;
        this.taskNodeName=taskNodeName;
    }

    @Override
    public List<String> execute(Context context) {
        ProcessService processService=context.getProcessService();
        Task task=context.getTaskService().getTask(taskId);
        ProcessInstance processInstance=processService.getProcessInstanceById(task.getProcessInstanceId());
        ProcessDefinition pd=processService.getProcessById(processInstance.getProcessId());
        TaskNode node=(TaskNode)pd.getNode(taskNodeName);
        return node.getAssigneeUsers(context, processInstance);
    }
}

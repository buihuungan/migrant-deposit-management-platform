package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import com.ontoweb.newworkflow.core.query.TaskQuery;
import org.hibernate.Session;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:42
 * @description：删除会签任务
 * @modified By：
 * @version: $
 */
public class DeleteCountersignCommand implements Command<Object> {
    private Task task;
    public DeleteCountersignCommand(Task task){
        this.task=task;
    }
    @Override
    public Object execute(Context context) {
        if(!TaskType.Countersign.equals(task.getType())){
            throw new IllegalArgumentException("Task "+task.getId()+" is not a countersign task.");
        }
        Session session=context.getSession();
        TaskQuery query=context.getTaskService().createTaskQuery();
        query.processInstanceId(task.getProcessInstanceId());
        query.nodeName(task.getNodeName());
        int count=0;
        for(Task t:query.list()){
            count=t.getCountersignCount();
            t.setCountersignCount(count-1);
            session.update(t);
        }
        session.delete(task);
        return null;
    }
}

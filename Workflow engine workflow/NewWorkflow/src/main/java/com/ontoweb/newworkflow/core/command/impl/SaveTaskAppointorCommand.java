package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskAppointor;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:47
 * @description：保存任务指派者
 * @modified By：
 * @version: $
 */
public class SaveTaskAppointorCommand implements Command<Object> {
    private Task task;
    private String taskNodeName;
    private String[] assignees;
    public SaveTaskAppointorCommand(Task task,String taskNodeName,String[] assignees){
        this.task=task;
        this.taskNodeName=taskNodeName;
        this.assignees=assignees;
    }


    @Override
    public Object execute(Context context) {
        Session session=context.getSession();
        Query query=session.createQuery("delete "+ TaskAppointor.class.getName()+" where taskNodeName=:nodeName and processInstanceId=:processInstanceId");
        query.setString("nodeName",taskNodeName).setLong("processInstanceId", task.getRootProcessInstanceId()).executeUpdate();
        for(String assignee:assignees){
            TaskAppointor appointor=new TaskAppointor();
            appointor.setId(IDGenerator.getInstance().nextId());
            appointor.setAppointor(task.getAssignee());
            appointor.setOwner(assignee);
            appointor.setAppointorNode(task.getNodeName());
            appointor.setProcessInstanceId(task.getRootProcessInstanceId());
            appointor.setTaskNodeName(taskNodeName);
            session.save(appointor);
        }
        return null;
    }
}

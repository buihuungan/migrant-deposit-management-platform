package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskParticipator;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import com.ontoweb.newworkflow.core.service.SchedulerService;
import org.hibernate.Session;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:48
 * @description：根据节点名称删除任务
 * @modified By：
 * @version: $
 */
public class DeleteTaskByNodeCommand implements Command<Integer> {
    private long processInstanceId;
    private String nodeName;
    public DeleteTaskByNodeCommand(long processInstanceId,String nodeName){
        this.processInstanceId=processInstanceId;
        this.nodeName=nodeName;
    }
    @Override
    @SuppressWarnings("unchecked")
    public Integer execute(Context context) {
        Session session=context.getSession();
        String hql="from "+ Task.class.getName()+" where nodeName=:nodeName and (processInstanceId=:processInstanceId or rootProcessInstanceId=:rootProcessInstanceId)";
        List<Task> list=session.createQuery(hql)
                .setString("nodeName", nodeName)
                .setLong("processInstanceId", processInstanceId)
                .setLong("rootProcessInstanceId", processInstanceId)
                .list();
        SchedulerService schedulerService=(SchedulerService)context.getApplicationContext().getBean(SchedulerService.BEAN_ID);
        for(Task task:list){
            if(task.getType().equals(TaskType.Participative)){
                session.createQuery("delete "+ TaskParticipator.class.getName()+" where taskId=:taskId").setLong("taskId", task.getId()).executeUpdate();
            }
            hql="delete "+ HistoryTask.class.getName()+" where taskId=:taskId";
            session.createQuery(hql).setLong("taskId", task.getId()).executeUpdate();
            session.delete(task);
            schedulerService.removeReminderJob(task);
        }
        return list.size();
    }
}

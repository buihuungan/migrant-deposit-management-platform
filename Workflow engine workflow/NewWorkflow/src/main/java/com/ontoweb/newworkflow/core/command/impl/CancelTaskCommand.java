package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.SchedulerService;
import com.ontoweb.newworkflow.core.service.TaskOpinion;
import com.ontoweb.newworkflow.handler.dao.HistoryProcessInstanceDao;
import com.ontoweb.newworkflow.handler.dao.ProcessInstanceDao;
import com.ontoweb.newworkflow.handler.utils.SpringBootBeanUtil;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:34
 * @description：取消任务
 * @modified By：
 * @version: $
 */
public class CancelTaskCommand implements Command<Task> {

    private Task task;
    private TaskOpinion opinion;
    public CancelTaskCommand(Task task,TaskOpinion opinion){
        this.task=task;
        this.opinion=opinion;
    }
    @Override
    public Task execute(Context context) {
        Session session=context.getSession();
        HistoryProcessInstanceDao historyProcessInstanceDao = SpringBootBeanUtil.getBean(HistoryProcessInstanceDao.class);
        task.setState(TaskState.Canceled);
        if(opinion!=null){
            task.setOpinion(opinion.getOpinion());
        }
        task.setEndDate(new Date());
        session.update(task);
        ProcessService processService=context.getProcessService();
        ProcessInstance processInstance=processService.getProcessInstanceById(task.getProcessInstanceId());
        SaveHistoryTaskCommand saveHistoryTaskCommand = SpringBootBeanUtil.getBean(SaveHistoryTaskCommand.class);
        saveHistoryTaskCommand.saveHistoryTaskCommand(task,processInstance);
        SchedulerService schedulerService = SpringBootBeanUtil.getBean(SchedulerService.class);
        schedulerService.removeReminderJob(task);

        // 新加的功能：取消任务之后删除process_instance中的记录，并修改his_process_instance中的记录值
        // 修改
        long hisProcessInstanceId = processInstance.getHistoryProcessInstanceId();
        HistoryProcessInstance his = session.find(HistoryProcessInstance.class, hisProcessInstanceId);
        //HistoryProcessInstance his = historyProcessInstanceDao.getOne((int) hisProcessInstanceId);
        his.setEndDate(new Date());
        his.setTag("cancel");
        session.update(his);

        //删除
        session.delete(processInstance);

        return task;
    }
}

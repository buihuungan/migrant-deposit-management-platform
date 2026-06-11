package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import com.ontoweb.newworkflow.handler.dao.HistoryTaskDao;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:46
 * @description：保存历史任务
 * @modified By：
 * @version: $
 */
@Service
public class SaveHistoryTaskCommand{
    @Autowired
    private HistoryTaskDao historyTaskDao;

    public HistoryTask saveHistoryTaskCommand(Task task,ProcessInstance processInstance) {
        HistoryTask hisTask=new HistoryTask();
        hisTask.setId(IDGenerator.getInstance().nextId());
        hisTask.setDescription(task.getDescription());
        hisTask.setProcessId(task.getProcessId());
        hisTask.setHistoryProcessInstanceId(processInstance.getHistoryProcessInstanceId());
        hisTask.setCreateDate(task.getCreateDate());
        hisTask.setProcessInstanceId(processInstance.getRootId());
        hisTask.setRootProcessInstanceId(processInstance.getRootId());
        hisTask.setTaskName(task.getTaskName());
        hisTask.setAssignee(task.getAssignee());
        hisTask.setNodeName(task.getNodeName());
        hisTask.setState(task.getState());
        String loginUser= EnvironmentUtils.getEnvironment().getLoginUser();
        if(loginUser!=null && loginUser.equals(task.getAssignee())){
            hisTask.setEndDate(new Date());
            hisTask.setOpinion(task.getOpinion());
        }
        hisTask.setCreateDate(task.getCreateDate());
        hisTask.setBusinessId(task.getBusinessId());
        hisTask.setDuedate(task.getDuedate());
        hisTask.setOwner(task.getOwner());
        hisTask.setType(task.getType());
        hisTask.setUrl(task.getUrl());
        hisTask.setTaskId(task.getId());
        hisTask.setSubject(task.getSubject());
        historyTaskDao.save(hisTask);

        return hisTask;
    }
}

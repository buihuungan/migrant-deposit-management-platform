package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import org.hibernate.criterion.Restrictions;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:59
 * @description：获取历史任务
 * @modified By：
 * @version: $
 */
public class GetHistoryTaskCommand implements Command<HistoryTask> {
    private long taskId;
    public GetHistoryTaskCommand(long taskId){
        this.taskId=taskId;
    }
    public HistoryTask execute(Context context) {
        return (HistoryTask)context.getSession().createCriteria(HistoryTask.class).add(Restrictions.eq("taskId", taskId)).uniqueResult();
    }
}

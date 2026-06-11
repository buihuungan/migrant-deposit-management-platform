package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:03
 * @description：获取历史任务列表
 * @modified By：
 * @version: $
 */
public class GetListHistoryTasksCommand implements Command<List<HistoryTask>> {
    private long processInstanceId;

    public GetListHistoryTasksCommand(long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HistoryTask> execute(Context context) {
        return context.getSession().createCriteria(HistoryTask.class)
                .add(Restrictions.eq("processInstanceId", processInstanceId))
                .addOrder(Order.desc("endDate")).list();
    }
}

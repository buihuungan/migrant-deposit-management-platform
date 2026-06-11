package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryActivity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:58
 * @description：获取历史活动
 * @modified By：
 * @version: $
 */
public class GetHistoryActivitiyCommand implements Command<List<HistoryActivity>> {
    private long instanceId;
    private boolean isProcessInstanceId;
    public GetHistoryActivitiyCommand(long instanceId,boolean isProcessInstanceId){
        this.instanceId=instanceId;
        this.isProcessInstanceId=isProcessInstanceId;
    }
    @SuppressWarnings("unchecked")
    public List<HistoryActivity> execute(Context context) {
        Criteria criteria=context.getSession().createCriteria(HistoryActivity.class);
        if(isProcessInstanceId){
            criteria.add(Restrictions.eq("rootProcessInstanceId", instanceId));
        }else{
            criteria.add(Restrictions.eq("historyProcessInstanceId", instanceId));
        }
        criteria.addOrder(Order.desc("endDate"));
        return criteria.list();
    }
}

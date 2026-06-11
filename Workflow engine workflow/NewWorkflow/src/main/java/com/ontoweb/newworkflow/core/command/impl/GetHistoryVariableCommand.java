package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryVariable;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:00
 * @description：获取历史变量
 * @modified By：
 * @version: $
 */
public class GetHistoryVariableCommand implements Command<HistoryVariable> {
    private long historyProcessInstanceId;
    private String key;
    public GetHistoryVariableCommand(long historyProcessInstanceId,String key){
        this.historyProcessInstanceId=historyProcessInstanceId;
        this.key=key;
    }

    @Override
    public HistoryVariable execute(Context context) {
        Criteria criteria=context.getSession().createCriteria(HistoryVariable.class);
        criteria.add(Restrictions.eq("historyProcessInstanceId", historyProcessInstanceId));
        criteria.add(Restrictions.eq("key", key));
        HistoryVariable hisVar=(HistoryVariable)criteria.uniqueResult();
        if(hisVar!=null){
            hisVar.init(context);
        }
        return hisVar;
    }
}

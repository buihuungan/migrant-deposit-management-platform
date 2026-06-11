package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryVariable;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:00
 * @description：获取所有历史变量
 * @modified By：
 * @version: $
 */
public class GetHistoryVariablesCommand  implements Command<List<HistoryVariable>> {
    private long historyProcessInstanceId;
    public GetHistoryVariablesCommand(long historyProcessInstanceId){
        this.historyProcessInstanceId=historyProcessInstanceId;
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<HistoryVariable> execute(Context context) {
        Criteria criteria=context.getSession().createCriteria(HistoryVariable.class);
        criteria.add(Restrictions.eq("historyProcessInstanceId", historyProcessInstanceId));
        List<HistoryVariable> vars=criteria.list();
        for(HistoryVariable var:vars){
            var.init(context);
        }
        return vars;
    }
}

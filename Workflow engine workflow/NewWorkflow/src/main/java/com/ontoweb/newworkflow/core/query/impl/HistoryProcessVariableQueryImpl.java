package com.ontoweb.newworkflow.core.query.impl;

import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.QueryCountCommand;
import com.ontoweb.newworkflow.core.command.impl.QueryListCommand;
import com.ontoweb.newworkflow.core.model.HistoryVariable;
import com.ontoweb.newworkflow.core.query.HistoryProcessVariableQuery;
import com.ontoweb.newworkflow.core.query.QueryJob;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:50
 * @description：
 * @modified By：
 * @version: $
 */
public class HistoryProcessVariableQueryImpl implements QueryJob, HistoryProcessVariableQuery {
    private long historyProcessInstanceId;
    private String key;
    private int firstResult;
    private int maxResults;
    private List<String> ascOrders=new ArrayList<String>();
    private List<String> descOrders=new ArrayList<String>();
    private CommandService commandService;

    public HistoryProcessVariableQueryImpl(CommandService commandService){
        this.commandService=commandService;
    }

    @Override
    public Criteria getCriteria(Session session, boolean queryCount) {
        Criteria criteria=session.createCriteria(HistoryVariable.class);
        buildCriteria(criteria,queryCount);
        return criteria;
    }

    private void buildCriteria(Criteria criteria,boolean queryCount){
        if(!queryCount && firstResult>0){
            criteria.setFirstResult(firstResult);
        }
        if(!queryCount && maxResults>0){
            criteria.setMaxResults(maxResults);
        }
        if(historyProcessInstanceId>0){
            criteria.add(Restrictions.eq("historyProcessInstanceId",historyProcessInstanceId));
        }
        if(StringUtils.isNotEmpty(key)){
            criteria.add(Restrictions.eq("key", key));
        }

        if(!queryCount){
            for(String ascProperty:ascOrders){
                criteria.addOrder(Order.asc(ascProperty));
            }
            for(String descProperty:descOrders){
                criteria.addOrder(Order.desc(descProperty));
            }
        }
    }


    @Override
    public List<HistoryVariable> list() {
        return commandService.executeCommand(new QueryListCommand<List<HistoryVariable>>(this));
    }

    @Override
    public int count() {
        return commandService.executeCommand(new QueryCountCommand(this));
    }

    @Override
    public HistoryProcessVariableQuery historyProcessInstanceId(long historyProcessInstanceId) {
        this.historyProcessInstanceId=historyProcessInstanceId;
        return this;
    }

    @Override
    public HistoryProcessVariableQuery key(String key) {
        this.key=key;
        return this;
    }

    @Override
    public HistoryProcessVariableQuery page(int firstResult, int maxResults) {
        this.firstResult=firstResult;
        this.maxResults=maxResults;
        return this;
    }

    @Override
    public HistoryProcessVariableQuery addOrderAsc(String property) {
        ascOrders.add(property);
        return this;
    }

    @Override
    public HistoryProcessVariableQuery addOrderDesc(String property) {
        descOrders.add(property);
        return this;
    }
}

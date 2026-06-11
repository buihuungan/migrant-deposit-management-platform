package com.ontoweb.newworkflow.core.query.impl;

import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.QueryCountCommand;
import com.ontoweb.newworkflow.core.command.impl.QueryListCommand;
import com.ontoweb.newworkflow.core.model.variable.Variable;
import com.ontoweb.newworkflow.core.query.ProcessVariableQuery;
import com.ontoweb.newworkflow.core.query.QueryJob;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午3:27
 * @description：流程变量实现类
 * @modified By：
 * @version: $
 */
public class ProcessVariableQueryImpl implements ProcessVariableQuery, QueryJob {
    private long processInstanceId;
    private long rootProcessInstanceId;
    private String key;
    private int firstResult;
    private int maxResults;
    private List<String> ascOrders=new ArrayList<String>();
    private List<String> descOrders=new ArrayList<String>();
    private CommandService commandService;
    public ProcessVariableQueryImpl(CommandService commandService){
        this.commandService=commandService;
    }
    @Override
    public List<Variable> list() {
        return commandService.executeCommand(new QueryListCommand<List<Variable>>(this));
    }

    @Override
    public int count() {
        return commandService.executeCommand(new QueryCountCommand(this));
    }
    @Override
    public Criteria getCriteria(Session session, boolean queryCount) {
        Criteria criteria=session.createCriteria(Variable.class);
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
        if(processInstanceId>0){
            criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
        }
        if(rootProcessInstanceId>0){
            criteria.add(Restrictions.eq("rootProcessInstanceId", rootProcessInstanceId));
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


    public ProcessVariableQuery addOrderAsc(String property){
        ascOrders.add(property);
        return this;
    }

    public ProcessVariableQuery addOrderDesc(String property){
        descOrders.add(property);
        return this;
    }

    public ProcessVariableQuery processInstanceId(long processInstanceId) {
        this.processInstanceId=processInstanceId;
        return this;
    }

    public ProcessVariableQuery rootprocessInstanceId(long rootProcessInstanceId) {
        this.rootProcessInstanceId=rootProcessInstanceId;
        return this;
    }

    public ProcessVariableQuery key(String key) {
        this.key=key;
        return this;
    }

    public ProcessVariableQuery page(int firstResult, int maxResults) {
        this.firstResult=firstResult;
        this.maxResults=maxResults;
        return this;
    }
}

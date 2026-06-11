package com.ontoweb.newworkflow.core.query.impl;

import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.QueryCountCommand;
import com.ontoweb.newworkflow.core.command.impl.QueryListCommand;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.query.ProcessQuery;
import com.ontoweb.newworkflow.core.query.QueryJob;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:55
 * @description：
 * @modified By：
 * @version: $
 */
public class ProcessQueryImpl implements ProcessQuery, QueryJob {
    private long id;
    private String name;
    private String key;
    private String categoryId;
    private String subject;
    private int version;
    private int firstResult;
    private int maxResults;
    private Date createDateLessThen;
    private Date createDateLessThenOrEquals;
    private Date createDateGreaterThen;
    private Date createDateGreaterThenOrEquals;
    private List<String> ascOrders=new ArrayList<String>();
    private List<String> descOrders=new ArrayList<String>();
    private CommandService commandService;
    public ProcessQueryImpl(CommandService commandService){
        this.commandService=commandService;
    }

    @Override
    public Criteria getCriteria(Session session, boolean queryCount) {
        Criteria criteria=session.createCriteria(ProcessDefinition.class);
        buildCriteria(criteria,queryCount);
        return criteria;
    }

    @Override
    public List<ProcessDefinition> list() {
        return commandService.executeCommand(new QueryListCommand<List<ProcessDefinition>>(this));
    }

    @Override
    public int count() {
        return commandService.executeCommand(new QueryCountCommand(this));
    }

    private void buildCriteria(Criteria criteria,boolean queryCount){
        if(!queryCount && firstResult>0){
            criteria.setFirstResult(firstResult);
        }
        if(!queryCount && maxResults>0){
            criteria.setMaxResults(maxResults);
        }
        if(id>0){
            criteria.add(Restrictions.eq("id", id));
        }
        if(StringUtils.isNotEmpty(name)){
            criteria.add(Restrictions.like("name", name));
        }
        if(StringUtils.isNotEmpty(key)){
            criteria.add(Restrictions.like("key", key));
        }
        if(StringUtils.isNotEmpty(subject)){
            criteria.add(Restrictions.like("subject", subject));
        }
        if(createDateLessThen!=null){
            criteria.add(Restrictions.lt("createDate", createDateLessThen));
        }
        if(createDateGreaterThen!=null){
            criteria.add(Restrictions.gt("createDate", createDateGreaterThen));
        }
        if(createDateLessThenOrEquals!=null){
            criteria.add(Restrictions.le("createDate", createDateLessThenOrEquals));
        }
        if(createDateGreaterThenOrEquals!=null){
            criteria.add(Restrictions.ge("createDate", createDateGreaterThenOrEquals));
        }
        if(StringUtils.isNotEmpty(categoryId)){
            criteria.add(Restrictions.eq("categoryId", categoryId));
        }else{
            categoryId= EnvironmentUtils.getEnvironment().getCategoryId();
            if(StringUtils.isNotEmpty(categoryId)){
                criteria.add(Restrictions.eq("categoryId", categoryId));
            }
        }
        if(version>0){
            criteria.add(Restrictions.eq("version", Integer.valueOf(version)));
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
    public ProcessQuery createDateGreaterThen(Date date) {
        this.createDateGreaterThen=date;
        return this;
    }

    @Override
    public ProcessQuery createDateGreaterThenOrEquals(Date date) {
        this.createDateGreaterThenOrEquals=date;
        return this;
    }

    @Override
    public ProcessQuery createDateLessThen(Date date) {
        this.createDateLessThen=date;
        return this;
    }

    @Override
    public ProcessQuery createDateLessThenOrEquals(Date date) {
        this.createDateLessThenOrEquals=date;
        return this;
    }

    @Override
    public ProcessQuery addOrderAsc(String property){
        ascOrders.add(property);
        return this;
    }

    @Override
    public ProcessQuery addOrderDesc(String property){
        descOrders.add(property);
        return this;
    }

    @Override
    public ProcessQuery id(long id) {
        this.id=id;
        return this;
    }

    @Override
    public ProcessQuery categoryId(String categoryId) {
        this.categoryId=categoryId;
        return this;
    }

    @Override
    public ProcessQuery nameLike(String name) {
        this.name=name;
        return this;
    }

    @Override
    public ProcessQuery subjectLike(String subject) {
        this.subject=subject;
        return this;
    }

    @Override
    public ProcessQuery keyLike(String key) {
        this.key=key;
        return this;
    }

    @Override
    public ProcessQuery version(int version) {
        this.version=version;
        return this;
    }

    @Override
    public ProcessQuery page(int firstResult, int maxResults) {
        this.firstResult=firstResult;
        this.maxResults=maxResults;
        return this;
    }
}

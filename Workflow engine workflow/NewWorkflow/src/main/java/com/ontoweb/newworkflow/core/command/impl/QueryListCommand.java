package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.variable.BlobVariable;
import com.ontoweb.newworkflow.core.model.variable.TextVariable;
import com.ontoweb.newworkflow.core.query.QueryJob;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:41
 * @description：
 * @modified By：
 * @version: $
 */
public class QueryListCommand<T> implements Command<T> {

    private QueryJob job;
    public QueryListCommand(QueryJob job){
        this.job=job;
    }
    @SuppressWarnings("unchecked")
    public T execute(Context context) {
        Criteria criteria=job.getCriteria(context.getSession(),false);
        List<Object> list=criteria.list();
        for(Object obj:list){
            if(obj instanceof BlobVariable){
                ((BlobVariable)obj).initValue(context);
            }
            if(obj instanceof TextVariable){
                ((TextVariable)obj).initValue(context);
            }
        }
        return (T)list;
    }
}

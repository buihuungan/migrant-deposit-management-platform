package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.query.QueryJob;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:41
 * @description：集群部署时所用
 * @modified By：
 * @version: $
 */
public class QueryCountCommand implements Command<Integer> {
    private QueryJob job;
    public QueryCountCommand(QueryJob job){
        this.job=job;
    }
    @Override
    public Integer execute(Context context) {
        Criteria criteria=job.getCriteria(context.getSession(),true);
        Object obj=criteria.setProjection(Projections.rowCount()).uniqueResult();
        if(obj==null)return 0;
        if(obj instanceof Integer){
            return (Integer)obj;
        }else{
            return ((Long)obj).intValue();
        }
    }
}

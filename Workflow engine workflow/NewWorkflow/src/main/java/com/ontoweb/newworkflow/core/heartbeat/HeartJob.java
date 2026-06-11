package com.ontoweb.newworkflow.core.heartbeat;

import com.ontoweb.newworkflow.core.model.Heartbeat;
import org.hibernate.Query;
import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:24
 * @description：
 * @modified By：
 * @version: $
 */
public class HeartJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        HeartJobDetail detail=(HeartJobDetail)context.getJobDetail();
        String instanceName=detail.getCurrentInstanceName();
        Session session=detail.getSessionFactory().openSession();
        try{
            String hql="from "+Heartbeat.class.getName()+" b where b.instanceName=:instanceName order by b.date desc";
            Query query=session.createQuery(hql).setString("instanceName",instanceName);
            @SuppressWarnings("unchecked")
            List<Heartbeat> beats=query.list();
            Date now=new Date();
            Heartbeat beat=null;
            if(beats.size()>0){
                beat=beats.get(0);
            }else{
                beat=new Heartbeat();
                beat.setId(UUID.randomUUID().toString());
                beat.setInstanceName(instanceName);
            }
            beat.setDate(now);
            session.saveOrUpdate(beat);
        }catch(Exception ex){
            throw new JobExecutionException(ex);
        }finally{
            session.flush();
            session.close();
        }
    }
}

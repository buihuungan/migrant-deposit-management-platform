package com.ontoweb.newworkflow.core.command.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.impl.ProcessParser;
import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import com.ontoweb.newworkflow.handler.dao.ProcessDefinitionDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:04
 * @description：获取流程信息
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class GetProcessCommand{

    @Autowired
    private ProcessDefinitionDao processDefinitionDao;

    @PersistenceContext
    private EntityManager entityManager;


    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public ProcessDefinition GetProcessCommand(long processId,String processName,int version,String categoryId) {
        if(processId>0){
            ProcessDefinition p = (ProcessDefinition)entityManager.find(ProcessDefinition.class, processId);
            return parseProcess(p.getId(),p.getVersion(),p.getName());
        }else if(StringUtils.isNotEmpty(processName)){

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ProcessDefinition> query = criteriaBuilder.createQuery(ProcessDefinition.class);
            // 设置查询根，可以根据查询的类型设置不同的 就是 Form 语句 后面的 entity
            Root<ProcessDefinition> root = query.from(ProcessDefinition.class);
            Predicate restrictions = criteriaBuilder.conjunction();
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("name"), processName));
            query.orderBy(criteriaBuilder.desc(root.get("version")));

            //Criteria criteria=session.createCriteria(ProcessDefinition.class).add(Restrictions.eq("name", processName)).addOrder(Order.desc("version"));
            if(categoryId==null){
                categoryId= EnvironmentUtils.getEnvironment().getCategoryId();
            }
            if(StringUtils.isNotEmpty(categoryId)){
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("categoryId"), categoryId));
            }
            if(version>0){
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("version"), version));
                query.where(restrictions);
                TypedQuery<ProcessDefinition> queryResult = entityManager.createQuery(query);
                List<ProcessDefinition> processes = queryResult.getResultList();

                if(processes.size()>0){
                    ProcessDefinition p=processes.get(0);
                    return parseProcess(p.getId(),p.getVersion(),p.getName());
                }
            }else{
                TypedQuery<ProcessDefinition> queryResult = entityManager.createQuery(query);
                List<ProcessDefinition> processes = queryResult.getResultList();

                for(ProcessDefinition process:processes){
                    Date effectDate=process.getEffectDate();
                    if(effectDate==null){
                        return parseProcess(process.getId(),process.getVersion(),process.getName());
                    }else{
                        if((new Date()).getTime()>effectDate.getTime()){
                            return parseProcess(process.getId(),process.getVersion(),process.getName());
                        }
                    }
                }
            }
        }
        return null;
    }

    private ProcessDefinition parseProcess(long processId,int version,String processName){
        String hql="from "+Blob.class.getName()+" where processId=:processId and name=:name";

        Blob blob=(Blob)entityManager.createQuery(hql).setParameter("processId", processId).setParameter("name", processName).getSingleResult();

        // Blob blob=(Blob)session.createQuery(hql).setLong("processId",processId).setString("name",processName+ ProcessDeployer.PROCESS_EXTENSION_NAME).uniqueResult();
        try {

            String inputstring = new String(blob.getBlobValue(), "UTF-8");
            JSONObject inputJson = JSONObject.parseObject(inputstring);

            ProcessDefinition process= ProcessParser.parseProcess(inputJson,processId,true);
            process.setId(processId);
            process.setVersion(version);
            return process;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

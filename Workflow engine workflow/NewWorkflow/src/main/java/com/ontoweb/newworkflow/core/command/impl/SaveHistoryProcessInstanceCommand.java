package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessInstanceState;
import com.ontoweb.newworkflow.handler.dao.HistoryProcessInstanceDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:45
 * @description：保存历史流程实例
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class SaveHistoryProcessInstanceCommand{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private HistoryProcessInstanceDao historyProcessInstanceDao;

    public HistoryProcessInstance saveHistoryProcessInstanceCommand(ProcessInstance processInstance) {

        HistoryProcessInstance hisProcessInstance=null;
        if(processInstance.getState().equals(ProcessInstanceState.Start)){
            hisProcessInstance=new HistoryProcessInstance();
            hisProcessInstance.setId(processInstance.getHistoryProcessInstanceId());
            hisProcessInstance.setCreateDate(processInstance.getCreateDate());
            hisProcessInstance.setProcessId(processInstance.getProcessId());
            hisProcessInstance.setProcessInstanceId(processInstance.getId());
            hisProcessInstance.setTag(processInstance.getTag());
            hisProcessInstance.setBusinessId(processInstance.getBusinessId());
            hisProcessInstance.setPromoter(processInstance.getPromoter());
            hisProcessInstance.setSubject(processInstance.getSubject());
            historyProcessInstanceDao.save(hisProcessInstance);
        }
        if(processInstance.getState().equals(ProcessInstanceState.End)){
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<HistoryProcessInstance> query = criteriaBuilder.createQuery(HistoryProcessInstance.class);
            // 设置查询根，可以根据查询的类型设置不同的 就是 Form 语句 后面的 entity
            Root<HistoryProcessInstance> root = query.from(HistoryProcessInstance.class);
            Predicate restrictions = criteriaBuilder.conjunction();
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("processInstanceId"), processInstance.getId()));
            query.where(restrictions);
            TypedQuery<HistoryProcessInstance> queryResult = entityManager.createQuery(query);
            hisProcessInstance = queryResult.getSingleResult();

//            hisProcessInstance=(HistoryProcessInstance)session.createCriteria(HistoryProcessInstance.class).add(Restrictions.eq("processInstanceId",processInstance.getId())).uniqueResult();
//            log.info("new Date(){}" + new Date());
            hisProcessInstance.setEndDate(new Date());
            historyProcessInstanceDao.save(hisProcessInstance);
        }
        return hisProcessInstance;
    }
}

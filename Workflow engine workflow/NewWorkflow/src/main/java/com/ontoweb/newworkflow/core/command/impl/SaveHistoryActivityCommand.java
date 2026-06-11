package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.model.HistoryActivity;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import com.ontoweb.newworkflow.handler.dao.HistoryActivityDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @date ：Created in 2021/7/27 下午10:44
 * @description：保存历史活动
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class SaveHistoryActivityCommand{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private HistoryActivityDao historyActivityDao;

    @SuppressWarnings("unchecked")
    public HistoryActivity saveHistoryActivityCommand(ProcessInstance processInstance, Node node, boolean isEnd, String leaveFlowName) {
        /**
         * 首先尝试取没有结束日期的当前节点的历史节点，如果有，那么就简单的为其添加结束时间，如果没有则创建一个新的，且保持结束日期为空
         * 如果没有未结束的历史节点，那么就尝试按结束日期倒序排取最晚结束的历史结束，然后更新其结束日期即可。
         * */

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HistoryActivity> query = criteriaBuilder.createQuery(HistoryActivity.class);
        // 设置查询根，可以根据查询的类型设置不同的 就是 Form 语句 后面的 entity
        Root<HistoryActivity> root = query.from(HistoryActivity.class);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("processInstanceId"), processInstance.getId()), criteriaBuilder.isNull(root.get("endDate")), criteriaBuilder.equal(root.get("nodeName"), node.getName()));
        query.where(restrictions);
        log.info("node.getName():{}" + node.getName());
        TypedQuery<HistoryActivity> queryResult = entityManager.createQuery(query);
        List<HistoryActivity> historyActivities1 = queryResult.getResultList();
        HistoryActivity hisActivity = null;
        if (historyActivities1.size() > 0) {
            hisActivity = historyActivities1.get(0);
        }

        if(isEnd){
            if(hisActivity==null){

                restrictions = criteriaBuilder.conjunction();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("processInstanceId"), processInstance.getId()), criteriaBuilder.isNotNull(root.get("endDate")), criteriaBuilder.equal(root.get("nodeName"), node.getName()));
                query.orderBy(criteriaBuilder.desc(root.get("createDate")));
                query.where(restrictions);
                TypedQuery<HistoryActivity> queryResultEnd = entityManager.createQuery(query);

                List<HistoryActivity> historyActivities = queryResultEnd.getResultList();
                if(historyActivities.size()>0){
                    hisActivity=historyActivities.get(0);
                }
            }
            if(hisActivity==null){
                return null;
            }
            hisActivity.setEndDate(new Date());
            if(StringUtils.isNotEmpty(leaveFlowName)){
                hisActivity.setLeaveFlowName(leaveFlowName);
            }
        }else{
            if(hisActivity==null){
                hisActivity=new HistoryActivity();
                hisActivity.setCreateDate(new Date());
                hisActivity.setHistoryProcessInstanceId(processInstance.getHistoryProcessInstanceId());
                hisActivity.setNodeName(node.getName());
                hisActivity.setId(IDGenerator.getInstance().nextId());
                hisActivity.setDescription(node.getDescription());
                hisActivity.setProcessId(node.getProcessId());
                hisActivity.setRootProcessInstanceId(processInstance.getRootId());
                hisActivity.setProcessInstanceId(processInstance.getId());
                hisActivity.setLeaveFlowName(leaveFlowName);
            }
        }
        historyActivityDao.save(hisActivity);
        return hisActivity;
    }
}

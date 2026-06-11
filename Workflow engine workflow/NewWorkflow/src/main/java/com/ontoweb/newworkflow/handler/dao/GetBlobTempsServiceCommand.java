package com.ontoweb.newworkflow.handler.dao;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.BlobTemp;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/8 下午8:57
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class GetBlobTempsServiceCommand {
    @PersistenceContext
    private EntityManager entityManager;

    public List<BlobTemp> getList() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BlobTemp> query = criteriaBuilder.createQuery(BlobTemp.class);
        // 设置查询根，可以根据查询的类型设置不同的 就是 Form 语句 后面的 entity
        Root<BlobTemp> root = query.from(BlobTemp.class);
        Predicate restrictions = criteriaBuilder.conjunction();
//        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("name"), processName));
        query.orderBy(criteriaBuilder.desc(root.get("createTime")));
        query.where(restrictions);
        TypedQuery<BlobTemp> queryResult = entityManager.createQuery(query);
        List<BlobTemp> list = queryResult.getResultList();

        return list;
    }
}

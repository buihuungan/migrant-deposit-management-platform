package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.BlobTemp;
import com.ontoweb.newworkflow.core.model.task.Task;
import io.swagger.annotations.ApiOperation;
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
 * @date ：Created in 2021/10/8 下午10:08
 * @description：开启流程的时候获取formDesignerId、onlineTableId、onlineDataId
 * @modified By：
 * @version: $
 */
@Service
public class GetFormOnlineId {
    @PersistenceContext
    private EntityManager entityManager;

    public String getAllId(long processInstanceId, String currentNode) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = criteriaBuilder.createQuery(Task.class);
        // 设置查询根，可以根据查询的类型设置不同的 就是 Form 语句 后面的 entity
        Root<Task> root = query.from(Task.class);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("processInstanceId"), processInstanceId), criteriaBuilder.equal(root.get("taskName"), currentNode));
//        query.orderBy(criteriaBuilder.desc(root.get("createTime")));
        query.where(restrictions);
        TypedQuery<Task> queryResult = entityManager.createQuery(query);
        List<Task> tasks = queryResult.getResultList();
        // 去最新一条的结果
        Task task = tasks.get(tasks.size() - 1);

        return task.getUrl();
    }
}

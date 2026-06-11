package com.ontoweb.newworkflow.core.query;

import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午3:28
 * @description：查询任务
 * @modified By：
 * @version: $
 */
public interface QueryJob {
    Criteria getCriteria(Session session, boolean queryCount);
}

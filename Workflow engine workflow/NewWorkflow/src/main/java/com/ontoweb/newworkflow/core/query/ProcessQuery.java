package com.ontoweb.newworkflow.core.query;

import com.ontoweb.newworkflow.core.model.ProcessDefinition;

import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:19
 * @description：流程查询返回对象
 * @modified By：
 * @version: $
 */
public interface ProcessQuery extends Query<List<ProcessDefinition>> {
    ProcessQuery id(long id);
    ProcessQuery categoryId(String id);
    ProcessQuery createDateLessThen(Date date);
    ProcessQuery createDateLessThenOrEquals(Date date);
    ProcessQuery createDateGreaterThen(Date date);
    ProcessQuery createDateGreaterThenOrEquals(Date date);
    ProcessQuery nameLike(String name);
    ProcessQuery keyLike(String key);
    ProcessQuery subjectLike(String subject);
    ProcessQuery version(int version);
    ProcessQuery page(int firstResult, int maxResults);
    ProcessQuery addOrderAsc(String property);
    ProcessQuery addOrderDesc(String property);
}

package com.ontoweb.newworkflow.core.query;

import com.ontoweb.newworkflow.core.model.ProcessInstance;

import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:23
 * @description：流程实例查询结果
 * @modified By：
 * @version: $
 */
public interface ProcessInstanceQuery extends Query<List<ProcessInstance>>{
    ProcessInstanceQuery processId(long processId);

    ProcessInstanceQuery parentId(long parentId);

    ProcessInstanceQuery rootId(long rootId);

    ProcessInstanceQuery page(int firstResult, int maxResults);

    ProcessInstanceQuery businessId(String businessId);

    ProcessInstanceQuery promoter(String businessId);

    ProcessInstanceQuery addOrderAsc(String property);

    ProcessInstanceQuery addOrderDesc(String property);

    ProcessInstanceQuery createDateLessThen(Date date);

    ProcessInstanceQuery createDateLessThenOrEquals(Date date);

    ProcessInstanceQuery createDateGreaterThen(Date date);

    ProcessInstanceQuery createDateGreaterThenOrEquals(Date date);
}

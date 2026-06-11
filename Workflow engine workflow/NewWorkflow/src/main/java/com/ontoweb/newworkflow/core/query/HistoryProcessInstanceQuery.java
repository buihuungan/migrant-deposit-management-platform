package com.ontoweb.newworkflow.core.query;

import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;

import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:48
 * @description：
 * @modified By：
 * @version: $
 */
public interface HistoryProcessInstanceQuery extends Query<List<HistoryProcessInstance>> {
    HistoryProcessInstanceQuery processId(long processId);

    HistoryProcessInstanceQuery page(int firstResult, int maxResults);

    HistoryProcessInstanceQuery addOrderAsc(String property);

    HistoryProcessInstanceQuery addOrderDesc(String property);

    HistoryProcessInstanceQuery createDateLessThen(Date date);

    HistoryProcessInstanceQuery createDateLessThenOrEquals(Date date);

    HistoryProcessInstanceQuery createDateGreaterThen(Date date);

    HistoryProcessInstanceQuery createDateGreaterThenOrEquals(Date date);

    HistoryProcessInstanceQuery businessId(String businessId);

    HistoryProcessInstanceQuery tag(String businessId);

    HistoryProcessInstanceQuery promoter(String promoter);
}

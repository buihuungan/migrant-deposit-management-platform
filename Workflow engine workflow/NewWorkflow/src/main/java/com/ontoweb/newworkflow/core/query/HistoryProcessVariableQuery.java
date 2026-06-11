package com.ontoweb.newworkflow.core.query;

import com.ontoweb.newworkflow.core.model.HistoryVariable;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:48
 * @description：
 * @modified By：
 * @version: $
 */
public interface HistoryProcessVariableQuery extends Query<List<HistoryVariable>> {
    HistoryProcessVariableQuery historyProcessInstanceId(long historyProcessInstanceId);
    HistoryProcessVariableQuery key(String key);
    HistoryProcessVariableQuery page(int firstResult, int maxResults);
    HistoryProcessVariableQuery addOrderAsc(String property);
    HistoryProcessVariableQuery addOrderDesc(String property);
}

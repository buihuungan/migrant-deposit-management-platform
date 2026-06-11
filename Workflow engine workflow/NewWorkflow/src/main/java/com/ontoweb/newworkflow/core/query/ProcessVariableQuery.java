package com.ontoweb.newworkflow.core.query;

import com.ontoweb.newworkflow.core.model.variable.Variable;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:22
 * @description：流程变量查询结果
 * @modified By：
 * @version: $
 */
public interface ProcessVariableQuery extends Query<List<Variable>> {
    ProcessVariableQuery processInstanceId(long processInstanceId);
    ProcessVariableQuery rootprocessInstanceId(long rootProcessInstanceId);
    ProcessVariableQuery key(String key);
    ProcessVariableQuery page(int firstResult, int maxResults);
    ProcessVariableQuery addOrderAsc(String property);
    ProcessVariableQuery addOrderDesc(String property);
}

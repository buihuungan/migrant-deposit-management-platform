package com.ontoweb.newworkflow.core.expr;

import com.ontoweb.newworkflow.core.model.ProcessInstance;

import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午3:39
 * @description：
 * @modified By：
 * @version: $
 */
public interface ExpressionProvider {
    Map<String,Object> getData(ProcessInstance processInstance);
    boolean support(ProcessInstance processInstance);
}

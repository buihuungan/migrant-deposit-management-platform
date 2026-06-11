package com.ontoweb.newworkflow.core.process.flow;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午11:14
 * @description：流程序列
 * @modified By：
 * @version: $
 */
public interface SequenceFlow {

    String getToNode();

    String getExpression();

    String getHandlerBean();

    ConditionType getConditionType();

    String getName();

    String getLabel();
}

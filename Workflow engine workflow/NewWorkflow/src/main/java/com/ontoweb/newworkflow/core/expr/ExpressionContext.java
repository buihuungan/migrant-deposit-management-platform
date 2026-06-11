package com.ontoweb.newworkflow.core.expr;

import com.ontoweb.newworkflow.core.model.ProcessInstance;
import org.apache.commons.jexl2.MapContext;

import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:09
 * @description：表达式上下文
 * @modified By：
 * @version: $
 */
public interface ExpressionContext {
//    public static final String BEAN_ID="uflo.expressionContext";
    MapContext createContext(ProcessInstance processInstance, Map<String, Object> variables);
    void addContextVariables(ProcessInstance processInstance,Map<String, Object> variables);
    boolean removeContext(ProcessInstance processInstance);
    void moveContextToParent(ProcessInstance processInstance);
    void removeContextVariables(long processInstanceId,String key);
    Object eval(ProcessInstance processInstance, String expression);
    Object eval(long processInstanceId, String expression);
    String evalString(ProcessInstance processInstance,String str);
}

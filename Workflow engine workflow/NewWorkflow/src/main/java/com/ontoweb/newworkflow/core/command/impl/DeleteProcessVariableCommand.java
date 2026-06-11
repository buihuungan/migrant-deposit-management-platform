package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.variable.Variable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:47
 * @description：删除流程变量
 * @modified By：
 * @version: $
 */
public class DeleteProcessVariableCommand implements Command<Object> {
    private String key;
    private long processInstanceId;
    public DeleteProcessVariableCommand(String key,long processInstanceId){
        this.key=key;
        this.processInstanceId=processInstanceId;
    }
    @Override
    public Object execute(Context context) {
        String hql="delete "+ Variable.class.getName()+" where processInstanceId=:pid and key=:key";
        context.getSession().createQuery(hql).setLong("pid", processInstanceId).setString("key",key).executeUpdate();
        context.getExpressionContext().removeContextVariables(processInstanceId, key);
        return null;
    }
}

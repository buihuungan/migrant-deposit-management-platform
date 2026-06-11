package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.variable.BlobVariable;
import com.ontoweb.newworkflow.core.model.variable.TextVariable;
import com.ontoweb.newworkflow.core.model.variable.Variable;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:05
 * @description：获取流程实例变量
 * @modified By：
 * @version: $
 */
public class GetProcessInstanceVariableCommand implements Command<Variable> {
    private ProcessInstance processInstance;
    private String key;
    public GetProcessInstanceVariableCommand(String key,ProcessInstance processInstance){
        this.processInstance=processInstance;
        this.key=key;
    }

    @Override
    public Variable execute(Context context) {
        return getVariable(context,processInstance);
    }
    @SuppressWarnings("unchecked")
    private Variable getVariable(Context context,ProcessInstance pi) {
        Session session=context.getSession();
        Criteria criteria=session.createCriteria(Variable.class).add(Restrictions.eq("processInstanceId", pi.getId())).add(Restrictions.eq("key", key));;
        List<Variable> vars=criteria.list();
        if(vars.size()==0){
            if(pi.getParentId()>0){
                ProcessInstance parentInstance=(ProcessInstance)session.get(ProcessInstance.class,pi.getParentId());
                return getVariable(context, parentInstance);
            }else{
                return null;
            }
        }else{
            for(Variable var:vars){
                if(var instanceof BlobVariable){
                    ((BlobVariable)var).initValue(context);
                }
                if(var instanceof TextVariable){
                    ((TextVariable)var).initValue(context);
                }
            }
            return vars.get(0);
        }
    }
}

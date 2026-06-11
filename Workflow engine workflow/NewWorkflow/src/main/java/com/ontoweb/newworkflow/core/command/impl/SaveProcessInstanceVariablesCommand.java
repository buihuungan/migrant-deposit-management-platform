package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.variable.BlobVariable;
import com.ontoweb.newworkflow.core.model.variable.TextVariable;
import com.ontoweb.newworkflow.core.model.variable.Variable;
import com.ontoweb.newworkflow.core.query.ProcessVariableQuery;
import com.ontoweb.newworkflow.core.query.impl.ProcessVariableQueryImpl;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:46
 * @description：保存流程实例变量
 * @modified By：
 * @version: $
 */
@Service
public class SaveProcessInstanceVariablesCommand {

    @Autowired
    private Context context;

    public Object saveProcessInstanceVariablesCommand(ProcessInstance processInstance,Map<String,Object> variables) {
        Session session=context.getSession();
        for(String key:variables.keySet()){
            ProcessVariableQuery query=new ProcessVariableQueryImpl(context.getCommandService());
            query.processInstanceId(processInstance.getId());
            query.key(key);
            List<Variable> oldVars=query.list();
            for(Variable var:oldVars){
                session.delete(var);
                if(var instanceof TextVariable){
                    session.delete(((TextVariable)var).getBlob());
                }
                if(var instanceof BlobVariable){
                    session.delete(((BlobVariable)var).getBlob());
                }
            }
            Object obj=variables.get(key);
            if(obj==null){
                throw new IllegalArgumentException("Variable ["+key+"] value can not be null.");
            }
            Variable var=Variable.newVariable(obj, context);
            var.setId(IDGenerator.getInstance().nextId());
            var.setKey(key);
            var.setProcessInstanceId(processInstance.getId());
            var.setRootProcessInstanceId(processInstance.getRootId());
            context.getExpressionContext().addContextVariables(processInstance, variables);
            session.save(var);
        }
        return null;
    }
}

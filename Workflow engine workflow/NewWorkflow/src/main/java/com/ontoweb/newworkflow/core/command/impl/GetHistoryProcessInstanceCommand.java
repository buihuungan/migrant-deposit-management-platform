package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import org.hibernate.criterion.Restrictions;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:58
 * @description：获取历史流程实例
 * @modified By：
 * @version: $
 */
public class GetHistoryProcessInstanceCommand implements Command<HistoryProcessInstance> {
    private long processInstanceId;
    public GetHistoryProcessInstanceCommand(long processInstanceId){
        this.processInstanceId=processInstanceId;
    }
    public HistoryProcessInstance execute(Context context) {
        return (HistoryProcessInstance)context.getSession().createCriteria(HistoryProcessInstance.class).add(Restrictions.eq("processInstanceId", processInstanceId)).uniqueResult();
    }
}

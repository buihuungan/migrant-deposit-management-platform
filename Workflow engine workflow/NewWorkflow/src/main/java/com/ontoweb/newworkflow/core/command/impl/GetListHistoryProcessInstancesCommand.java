package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:02
 * @description：历史流程实例列表
 * @modified By：
 * @version: $
 */
public class GetListHistoryProcessInstancesCommand  implements Command<List<HistoryProcessInstance>> {
    private long processId;
    public GetListHistoryProcessInstancesCommand(long processId){
        this.processId=processId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HistoryProcessInstance> execute(Context context) {
        return context.getSession().createCriteria(HistoryProcessInstance.class).add(Restrictions.eq("processId", processId)).list();
    }
}

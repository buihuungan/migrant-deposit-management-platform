package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.TaskAppointor;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:06
 * @description：获取任务指定者列表
 * @modified By：
 * @version: $
 */
public class GetTaskAppointorCommand implements Command<List<TaskAppointor>> {
    private String taskNodeName;
    private long processInstanceId;
    public GetTaskAppointorCommand(String taskNodeName,long processInstanceId){
        this.taskNodeName=taskNodeName;
        this.processInstanceId=processInstanceId;
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskAppointor> execute(Context context) {
        Criteria criteria=context.getSession().createCriteria(TaskAppointor.class);
        criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
        criteria.add(Restrictions.eq("taskNodeName", taskNodeName));
        return criteria.list();
    }
}

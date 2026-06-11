package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.task.TaskParticipator;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:08
 * @description：获取任务参与列表
 * @modified By：
 * @version: $
 */
public class GetTaskParticipatorsCommand implements Command<List<TaskParticipator>> {
        private long taskId;
        public GetTaskParticipatorsCommand(long taskId){
        this.taskId=taskId;
        }
        @SuppressWarnings("unchecked")
        @Override
        public List<TaskParticipator> execute(Context context) {
                return context.getSession().createCriteria(TaskParticipator.class).add(Restrictions.eq("taskId", taskId)).list();
        }

}

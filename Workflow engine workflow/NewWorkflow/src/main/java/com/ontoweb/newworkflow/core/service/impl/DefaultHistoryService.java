package com.ontoweb.newworkflow.core.service.impl;

import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.*;
import com.ontoweb.newworkflow.core.model.HistoryActivity;
import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.HistoryVariable;
import com.ontoweb.newworkflow.core.query.HistoryProcessInstanceQuery;
import com.ontoweb.newworkflow.core.query.HistoryProcessVariableQuery;
import com.ontoweb.newworkflow.core.query.HistoryTaskQuery;
import com.ontoweb.newworkflow.core.query.impl.HistoryProcessInstanceQueryImpl;
import com.ontoweb.newworkflow.core.query.impl.HistoryProcessVariableQueryImpl;
import com.ontoweb.newworkflow.core.query.impl.HistoryTaskQueryImpl;
import com.ontoweb.newworkflow.core.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:06
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class DefaultHistoryService implements HistoryService {

    @Autowired
    private CommandService commandService;

    @Override
    public List<HistoryActivity> getHistoryActivitysByProcesssInstanceId(long processInstanceId) {
        return commandService.executeCommand(new GetHistoryActivitiyCommand(processInstanceId,true));
    }

    @Override
    public HistoryProcessVariableQuery createHistoryProcessVariableQuery() {
        return new HistoryProcessVariableQueryImpl(commandService);
    }

    @Override
    public HistoryVariable getHistoryVariable(long historyProcessInstanceId, String key) {
        return commandService.executeCommand(new GetHistoryVariableCommand(historyProcessInstanceId,key));
    }

    @Override
    public List<HistoryActivity> getHistoryActivitysByHistoryProcesssInstanceId(long historyProcessInstanceId) {
        return commandService.executeCommand(new GetHistoryActivitiyCommand(historyProcessInstanceId,false));
    }

    @Override
    public List<HistoryProcessInstance> getHistoryProcessInstances(long processId) {
        return commandService.executeCommand(new GetListHistoryProcessInstancesCommand(processId));
    }

    @Override
    public HistoryProcessInstance getHistoryProcessInstance(long processInstanceId) {
        return commandService.executeCommand(new GetHistoryProcessInstanceCommand(processInstanceId));
    }

    @Override
    public List<HistoryTask> getHistoryTasks(long processInstanceId) {
        return commandService.executeCommand(new GetListHistoryTasksCommand(processInstanceId));
    }

    @Override
    public HistoryProcessInstanceQuery createHistoryProcessInstanceQuery() {
        return new HistoryProcessInstanceQueryImpl(commandService);
    }

    @Override
    public HistoryTaskQuery createHistoryTaskQuery() {
        return new HistoryTaskQueryImpl(commandService);
    }

    @Override
    public List<HistoryVariable> getHistoryVariables(long historyProcessInstanceId) {
        return commandService.executeCommand(new GetHistoryVariablesCommand(historyProcessInstanceId));
    }

    @Override
    public HistoryTask getHistoryTask(long taskId) {
        return commandService.executeCommand(new GetHistoryTaskCommand(taskId));
    }
    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
    }
}

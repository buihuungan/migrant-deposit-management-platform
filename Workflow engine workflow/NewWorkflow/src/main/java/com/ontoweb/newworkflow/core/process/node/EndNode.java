package com.ontoweb.newworkflow.core.process.node;

import com.ontoweb.newworkflow.core.command.impl.SaveHistoryProcessInstanceCommand;
import com.ontoweb.newworkflow.core.command.impl.SaveProcessInstanceVariablesCommand;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.*;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskParticipator;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import com.ontoweb.newworkflow.core.model.variable.BlobVariable;
import com.ontoweb.newworkflow.core.model.variable.DateVariable;
import com.ontoweb.newworkflow.core.model.variable.TextVariable;
import com.ontoweb.newworkflow.core.model.variable.Variable;
import com.ontoweb.newworkflow.core.process.handler.ProcessEventHandler;
import com.ontoweb.newworkflow.core.query.ProcessVariableQuery;
import com.ontoweb.newworkflow.core.query.impl.ProcessVariableQueryImpl;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.SchedulerService;
import com.ontoweb.newworkflow.core.utils.ProcessListenerUtils;
import com.ontoweb.newworkflow.handler.constant.RedisKey;
import com.ontoweb.newworkflow.handler.dao.*;
import com.ontoweb.newworkflow.handler.utils.RedisUtils;
import com.ontoweb.newworkflow.handler.utils.SpringBootBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午3:21
 * @description：
 * @modified By：
 * @version: $
 */
@Slf4j
public class EndNode extends Node{

    private static final long serialVersionUID = 3694427904622957142L;
    private boolean terminate=true;
    @Override
    public void cancel(Context context, ProcessInstance processInstance) {
    }
    @Override
    public boolean enter(Context context,ProcessInstance processInstance) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized String leave(Context context,ProcessInstance processInstance,String flowName) {
        //Session session=context.getSession();
        ProcessService processService = SpringBootBeanUtil.getBean(ProcessService.class);
        RedisUtils redisUtils = SpringBootBeanUtil.getBean(RedisUtils.class);
        ProcessDefinition process = processService.getProcessById(processInstance.getProcessId());
        boolean isRootProcessInstance=(processInstance.getId()==processInstance.getRootId());
        //List<ProcessInstance> children=session.createCriteria(ProcessInstance.class).add(Restrictions.eq("rootId",processInstance.getRootId())).list();
        EntityManager entityManager = SpringBootBeanUtil.getBean(EntityManager.class);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProcessInstance> query = criteriaBuilder.createQuery(ProcessInstance.class);
        // 设置查询根，可以根据查询的类型设置不同的 就是 Form 语句 后面的 entity
        Root<ProcessInstance> root = query.from(ProcessInstance.class);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("rootId"), processInstance.getRootId()));
        query.where(restrictions);
        TypedQuery<ProcessInstance> queryResult = entityManager.createQuery(query);
        List<ProcessInstance> children = queryResult.getResultList();

        if (redisUtils.hasKey(RedisKey.PROCESSINSTANCE+processInstance.getId())) {
            redisUtils.delete(RedisKey.PROCESSINSTANCE+processInstance.getId());
        }

        if(isRootProcessInstance){
            ProcessListenerUtils.fireProcessEndListers(processInstance, context);
            completeProcessInstance(context,process,processInstance,processInstance,children);
        }else if(isTerminate()){
//            ProcessInstance rootProcessInstance=(ProcessInstance)session.get(ProcessInstance.class,processInstance.getRootId());
            ProcessInstance rootProcessInstance = queryResult.getSingleResult();
            ProcessListenerUtils.fireProcessEndListers(rootProcessInstance, context);
            completeProcessInstance(context,process,processInstance,rootProcessInstance,children);
        }else{
            if(children.size()==1){
                ProcessInstance rootProcessInstance = queryResult.getSingleResult();
                ProcessListenerUtils.fireProcessEndListers(rootProcessInstance, context);
                completeProcessInstance(context,process,processInstance,rootProcessInstance,children);
            }else{
                doLeaveEventHandler(context, processInstance);
                completeActivityHistory(context, processInstance,null);
                ProcessInstanceDao processInstanceDao = SpringBootBeanUtil.getBean(ProcessInstanceDao.class);
                processInstanceDao.delete(processInstance);
                context.getExpressionContext().removeContext(processInstance);
            }
        }
        return null;
    }

    private void completeProcessInstance(Context context,ProcessDefinition process,ProcessInstance processInstance, ProcessInstance rootProcessInstance,List<ProcessInstance> children) {
        ProcessInstanceDao processInstanceDao = SpringBootBeanUtil.getBean(ProcessInstanceDao.class);

        doLeaveEventHandler(context, processInstance);
        completeActivityHistory(context, processInstance,null);
        //执行流程级别结束事件
        String processEventHandlerBean=process.getEventHandlerBean();
        if(StringUtils.isNotEmpty(processEventHandlerBean)){
            ProcessEventHandler bean=(ProcessEventHandler)context.getApplicationContext().getBean(processEventHandlerBean);
            bean.end(rootProcessInstance, context);
        }
        //Session session=context.getSession();
        for(ProcessInstance pi:children){
            if(pi.getId()==rootProcessInstance.getId()){
                continue;
            }
            ProcessVariableQuery query=new ProcessVariableQueryImpl(context.getCommandService());
            query.processInstanceId(pi.getId());
            List<Variable> variables=query.list();
            saveHistoryProcessInstanceVariables(pi,context,variables);
            processInstanceDao.delete(pi);
            deleteProcessInstanceJobs(context,pi,false);
        }
        ProcessVariableQuery query=new ProcessVariableQueryImpl(context.getCommandService());
        query.rootprocessInstanceId(rootProcessInstance.getRootId());
        List<Variable> variables=query.list();
        log.info("variables:{}" + variables);

        saveHistoryProcessInstanceVariables(rootProcessInstance,context,variables);

        if(rootProcessInstance.getParentId()>0){
            //表明当前为子流程
            executeParentProcessInstance(context, rootProcessInstance);
        }else{
            context.getExpressionContext().removeContext(rootProcessInstance);
        }
        rootProcessInstance.setState(ProcessInstanceState.End);
        processInstanceDao.delete(rootProcessInstance);
        SaveHistoryProcessInstanceCommand saveHistoryProcessInstanceCommand = SpringBootBeanUtil.getBean(SaveHistoryProcessInstanceCommand.class);
        saveHistoryProcessInstanceCommand.saveHistoryProcessInstanceCommand(rootProcessInstance);

        deleteProcessInstanceJobs(context, rootProcessInstance,true);
    }
    @SuppressWarnings("unchecked")
    private void deleteProcessInstanceJobs(Context context,ProcessInstance processInstance,boolean isRoot) {
        //Session session=context.getSession();
        EntityManager entityManager = SpringBootBeanUtil.getBean(EntityManager.class);
        TaskDao taskDao = SpringBootBeanUtil.getBean(TaskDao.class);

        SchedulerService schedulerService = SpringBootBeanUtil.getBean(SchedulerService.class);
        String hql="from "+ Task.class.getName()+" where ";
        if(isRoot){
            hql+="rootProcessInstanceId=:rootProcessInstanceId";
        }else{
            hql+="processInstanceId=:processInstanceId";
        }
        Query query = entityManager.createQuery(hql);
        if(isRoot){
            query.setParameter("rootProcessInstanceId", processInstance.getRootId());
        }else{
            query.setParameter("processInstanceId", processInstance.getId());
        }
        List<Task> tasks=query.getResultList();

        TaskParticipatorDao taskParticipatorDao = SpringBootBeanUtil.getBean(TaskParticipatorDao.class);
        for(Task t:tasks){
            if(t.getType().equals(TaskType.Participative)){
//                hql="delete "+ TaskParticipator.class.getName()+" where taskId=:taskId";
//                Query deleteQuery = entityManager.createQuery(hql).setParameter("taskId", t.getId());
//                deleteQuery.executeUpdate();

                taskParticipatorDao.deleteByTaskId(t.getId());
            }
            schedulerService.removeReminderJob(t);
            taskDao.delete(t);
        }
    }

    private void executeParentProcessInstance(Context context,ProcessInstance rootProcessInstance) {
        ProcessService processService=context.getProcessService();
        ProcessInstance parentProcessInstance=processService.getProcessInstanceById(rootProcessInstance.getParentId());
        ProcessDefinition parentProcess=processService.getProcessById(parentProcessInstance.getProcessId());

        //检查当前父流程实例所在节点是否为子流程节点
        Node node=parentProcess.getNode(parentProcessInstance.getCurrentNode());
        if(node instanceof SubprocessNode){
            SubprocessNode subprocessNode=(SubprocessNode)node;
            Map<String,Object> vars=null;
            List<SubprocessVariable> outVariables=subprocessNode.getOutVariables();
            if(outVariables!=null && outVariables.size()>0){
                //将指定需要从子流程返回到父流程的变量取出来写入到父流程当中
                vars=new HashMap<String,Object>();
                for(SubprocessVariable var:outVariables){
                    String key=var.getInParameterKey();
                    Object obj=context.getExpressionContext().eval(rootProcessInstance, "${"+key+"}");
                    if(obj==null)obj=processService.getProcessVariable(key, rootProcessInstance);
                    if(obj==null){
                        throw new IllegalArgumentException("Variable "+key+" value is not found in process instance "+rootProcessInstance.getId());
                    }
                    vars.put(var.getOutParameterKey(), obj);
                }
            }
            if(vars!=null){
                SaveProcessInstanceVariablesCommand saveProcessInstanceVariablesCommand = SpringBootBeanUtil.getBean(SaveProcessInstanceVariablesCommand.class);
                saveProcessInstanceVariablesCommand.saveProcessInstanceVariablesCommand(parentProcessInstance, vars);
                context.getExpressionContext().addContextVariables(parentProcessInstance, vars);
            }
        }
        context.getExpressionContext().removeContext(rootProcessInstance);
        String leaveFlowName=node.leave(context, parentProcessInstance,null);
        node.completeActivityHistory(context, parentProcessInstance,leaveFlowName);
    }

    private void saveHistoryProcessInstanceVariables(ProcessInstance rootProcessInstance,Context context,List<Variable> variables) {
        //Session session=context.getSession();
        HistoryBlobDao historyBlobDao = SpringBootBeanUtil.getBean(HistoryBlobDao.class);
        BlobDao blobDao = SpringBootBeanUtil.getBean(BlobDao.class);
        HistoryVariableDao historyVariableDao = SpringBootBeanUtil.getBean(HistoryVariableDao.class);
        VariableDao variableDao = SpringBootBeanUtil.getBean(VariableDao.class);
        for(Variable var:variables){
            HistoryVariable hisVar=new HistoryVariable();
            hisVar.setId(var.getId());
            hisVar.setKey(var.getKey());
            hisVar.setHistoryProcessInstanceId(rootProcessInstance.getHistoryProcessInstanceId());

            if(var instanceof BlobVariable){
                BlobVariable blobVar=(BlobVariable)var;
                long blobId=blobVar.getBlobId();
                hisVar.setStringValue(String.valueOf(blobId));
                HistoryBlob hisBlob=new HistoryBlob(blobVar.getValue());
                hisBlob.setId(blobId);
                historyBlobDao.save(hisBlob);
                blobDao.delete(blobVar.getBlob());
            }else if(var instanceof TextVariable){
                TextVariable textVar=(TextVariable)var;
                long blobId=textVar.getBlobId();
                hisVar.setStringValue(String.valueOf(blobId));
                HistoryBlob hisBlob=new HistoryBlob(textVar.getValue());
                hisBlob.setId(blobId);
                historyBlobDao.save(hisBlob);
                blobDao.delete(textVar.getBlob());
            }else if(var instanceof DateVariable){
                SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
                hisVar.setStringValue(sd.format(((Date)var.getValue())));
            }else{
                String stringValue=(var.getValue()==null?null:var.getValue().toString());
                if(stringValue!=null && stringValue.length()>250){
                    stringValue=stringValue.substring(0,250);
                }
                hisVar.setStringValue(stringValue);
            }
            hisVar.setType(var.getType());
            historyVariableDao.save(hisVar);
            variableDao.delete(var);
        }
    }

    @Override
    public NodeType getType() {
        if(terminate){
            return NodeType.TerminalEnd;
        }
        return NodeType.End;
    }

    public boolean isTerminate() {
        return terminate;
    }
    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }
}

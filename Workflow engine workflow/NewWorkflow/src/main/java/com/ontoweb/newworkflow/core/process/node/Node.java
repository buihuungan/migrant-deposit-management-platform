package com.ontoweb.newworkflow.core.process.node;

import com.ontoweb.newworkflow.core.command.impl.SaveHistoryActivityCommand;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.HistoryActivity;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlowImpl;
import com.ontoweb.newworkflow.core.process.handler.NodeEventHandler;
import com.ontoweb.newworkflow.handler.utils.SpringBootBeanUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午11:11
 * @description：节点描述信息
 * @modified By：
 * @version: $
 */
@Data
@Slf4j
public abstract class Node implements java.io.Serializable{

    private static final long serialVersionUID = -7347322856264296067L;
    private String name;
    private String label;
    private String description;
    private long processId;
    private NodeDiagram diagram;
    private List<SequenceFlowImpl> sequenceFlows;
    private String eventHandlerBean;

    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * 进入当前节点的需要做的工作
     * @param context 上下文对象
     * @param processInstance 当前节点所在的流程实例对象
     * @return 返回是否执行当前节点的leave动作
     */
    public abstract boolean enter(Context context, ProcessInstance processInstance);

    /**
     * 取消当前节点的需要做的工作
     * @param context 上下文对象
     * @param processInstance 当前节点所在的流程实例对象
     */
    public abstract void cancel(Context context,ProcessInstance processInstance);


    /**
     * 离开当前节点的需要做的工作
     * @param context 上下文对象
     * @param processInstance 当前节点所在的流程实例对象
     * @param flowName 要沿着哪条sequenceFlow离开
     * @return 返回离开当前节点的sequenceFlow的name
     */
    public abstract String leave(Context context,ProcessInstance processInstance,String flowName);

    private HistoryActivity saveActivityHistory(Context context, ProcessInstance processInstance, boolean isEnd, String leaveFlowName){
        SaveHistoryActivityCommand saveHistoryActivityCommand = SpringBootBeanUtil.getBean(SaveHistoryActivityCommand.class);
        return saveHistoryActivityCommand.saveHistoryActivityCommand(processInstance, this,isEnd,leaveFlowName);
    }

    public HistoryActivity createActivityHistory(Context context,ProcessInstance processInstance){
        return saveActivityHistory(context,processInstance,false,null);
    }
    public HistoryActivity completeActivityHistory(Context context,ProcessInstance processInstance,String flowName){
        return saveActivityHistory(context,processInstance,true,flowName);
    }

    protected String leaveNode(Context context,ProcessInstance processInstance,String flowName){
        if(StringUtils.isNotEmpty(flowName)){
            SequenceFlowImpl flow=getFlow(flowName);
            if(flow==null){
                throw new IllegalArgumentException("Sequence flow ["+flowName+"] not exist!");
            }
            flow.execute(context, processInstance);
            return flow.getName();
        }
        for(SequenceFlowImpl flow:sequenceFlows){
            log.info("sequenceFlows:{}",flow.getName());
            if(flow.canExecute(context, processInstance)){
                flow.execute(context, processInstance);
                return flow.getName();
            }
        }
        throw new IllegalArgumentException("All the sequence flow can not be perform!");
    }

    private void executeEventHandler(Context context,ProcessInstance processInstance,boolean enter){
        if(StringUtils.isNotEmpty(eventHandlerBean)){
            NodeEventHandler handler=(NodeEventHandler)context.getApplicationContext().getBean(eventHandlerBean);
            if(enter){
                handler.enter(this, processInstance, context);
            }else{
                handler.leave(this, processInstance, context);
            }
        }
    }

    public void doEnterEventHandler(Context context,ProcessInstance processInstance){
        executeEventHandler(context, processInstance, true);
    }
    public void doLeaveEventHandler(Context context,ProcessInstance processInstance){
        executeEventHandler(context, processInstance, false);
    }

    protected SequenceFlowImpl getFlow(String flowName){
        SequenceFlowImpl flow = new SequenceFlowImpl();
        for(SequenceFlowImpl f:getSequenceFlows()){
            SequenceFlowImpl temp = new SequenceFlowImpl();
            String label = f.getLabel();
            // 路由决策，此处是为lable判断  其他为name判断
            if(StringUtils.isNotEmpty(label)){
                String toNode = f.getToNode();
                String name = f.getLabel();

                if (label.startsWith("$")) {
                    String[] labels = label.substring(1,label.length()-1).split(",");
                    String[] toNodes = toNode.substring(1,toNode.length()-1).split(",");
                    String[] names = name.substring(1,name.length()-1).split(",");
                    for (int i = 0; i < labels.length; i++) {
                        if (flowName.equals(labels[i])) {
                            temp.setLabel(labels[i]);
                            temp.setName(names[i]);
                            temp.setToNode(toNodes[i]);
                        }
                    }
                    flow = temp;
                    break;
                }

                if(flowName.equals(label)){
                    flow=f;
                    break;
                }
            }else{
                String name1 = f.getName();
                if(flowName.equals(name1)){
                    flow=f;
                    break;
                }
            }

        }
        return flow;
    }

    public abstract NodeType getType();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public NodeDiagram getDiagram() {
        return diagram;
    }

    public void setDiagram(NodeDiagram diagram) {
        this.diagram = diagram;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<SequenceFlowImpl> getSequenceFlows() {
        return sequenceFlows;
    }

    public void setSequenceFlows(List<SequenceFlowImpl> sequenceFlows) {
        this.sequenceFlows = sequenceFlows;
    }

    public long getProcessId() {
        return processId;
    }

    public void setProcessId(long processId) {
        this.processId = processId;
    }

    public String getEventHandlerBean() {
        return eventHandlerBean;
    }

    public void setEventHandlerBean(String eventHandlerBean) {
        this.eventHandlerBean = eventHandlerBean;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

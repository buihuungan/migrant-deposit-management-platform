package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.deploy.parse.Parser;
import com.ontoweb.newworkflow.core.diagram.ProcessDiagram;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.process.node.StartNode;
import com.ontoweb.newworkflow.core.process.swimlane.Swimlane;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午8:36
 * @description：流程转换
 * @modified By：
 * @version: $
 */

@Slf4j
@Service
public class ProcessParser extends AbstractParser implements ApplicationContextAware{
    private static ProcessParser processParser;
    public static ProcessDefinition parseProcess(JSONObject inputJson, long processId, boolean parseChildren){
        if(processParser.support(inputJson)){
            ProcessDefinition pd = (ProcessDefinition)processParser.parse(inputJson,processId,parseChildren);
            return pd;
        }
        return null;
    }

    /**
     *
     * @param inputJson
     * @param processId
     * @param parseChildren 是否是更新的流程，如果是更新的流程还需要更新子节点信息
     * @return
     */
    @Override
    public Object parse(JSONObject inputJson,long processId,boolean parseChildren) {
        ProcessDefinition process=new ProcessDefinition();
        process.setId(processId);
        process.setEventHandlerBean(unescape(inputJson.getString("event_handler_bean")));
        process.setName(unescape(inputJson.getString("name")));
        process.setStartProcessUrl(unescape(inputJson.getString("start_process_url")));
        process.setKey(unescape(inputJson.getString("key")));
        process.setCategoryId(unescape(inputJson.getString("category_id")));
        //新加字段
        JSONObject jsonObject = inputJson.getJSONObject("formAttribute");
        process.setProcessMainTable(jsonObject.getString("processMainTable"));
        process.setProcessMainKey(jsonObject.getString("processMainKey"));
        process.setMainTableAssociationKey(jsonObject.getString("mainTableAssociationkey"));
        process.setProcessAssociationTable(jsonObject.getString("processAssociationTable"));
        process.setProcessAssociationKey(jsonObject.getString("processAssociationKey"));

        String effectDateStr=unescape(inputJson.getString("effect_date"));
        if(StringUtils.isNotEmpty(effectDateStr)){
            SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                process.setEffectDate(sd.parse(effectDateStr));
            } catch (ParseException e) {
                log.error("Process definition "+process.getName()+" effect date is invalid:"+effectDateStr);
            }
        }
        if(StringUtils.isEmpty(process.getCategoryId())){
            process.setCategoryId(EnvironmentUtils.getEnvironment().getCategoryId());
        }

        if(!StringUtils.isEmpty(inputJson.get("description").toString().trim())){
            String desc = inputJson.getString("description");
            desc = (desc.length()>120?desc.substring(0,120):desc);
            process.setDescription(unescape(desc));
        }
        if(parseChildren){
            List<Node> nodes=new ArrayList<Node>();
            List<Swimlane> swimlanes=new ArrayList<Swimlane>();

            // 将节点信息转化为list
            List<JSONObject> nodeList = JSON.parseArray(inputJson.getString("nodeList"), JSONObject.class);

            for(JSONObject obj : nodeList){
                for(Parser parser:parsers){
                    String parserName = parser.getClass().toString().substring(parser.getClass().toString().lastIndexOf(".") + 1);
                    if(!parser.support(obj) || "SequenceFlowParser".equals(parserName)){
                        continue;
                    }
                    Object processElement = parser.parse(obj,processId,parseChildren);
                    if(processElement instanceof Node){
                        Node node=(Node)processElement;
                        node.setProcessId(processId);
                        nodes.add(node);
                    }
                    if(processElement instanceof StartNode){
                        process.setStartNode((StartNode)processElement);
                    }
                    if(processElement instanceof Swimlane){
                        swimlanes.add((Swimlane)processElement);
                    }
                    break;
                }
            }
            process.setSwimlanes(swimlanes);
            process.setNodes(nodes);
        }
//        if(parseChildren){
//            process.setDiagram(buildProcessDiagram(process));
//        }
        return process;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.getString("type").equals("process");
    }

    private ProcessDiagram buildProcessDiagram(ProcessDefinition process){
        ProcessDiagramBudiler processDiagramBuilder=new ProcessDiagramBudiler(process);
        return processDiagramBuilder.getDiagram();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.setApplicationContext(applicationContext);
        ProcessParser.processParser=this;
    }
}

package com.ontoweb.newworkflow.core.deploy.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.ontoweb.newworkflow.core.deploy.StringTools;
import com.ontoweb.newworkflow.core.deploy.parse.impl.SequenceFlowParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.process.assign.Assignee;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlowImpl;
import com.ontoweb.newworkflow.core.process.node.FormElement;
import com.ontoweb.newworkflow.core.process.node.Mapping;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.process.node.UserData;
import com.ontoweb.newworkflow.core.process.security.Authority;
import com.ontoweb.newworkflow.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午5:05
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public abstract class AbstractParser implements Parser, ApplicationContextAware {
    protected Collection<Parser> parsers;

    protected String unescape(String str){
        if(StringUtils.isEmpty(str))return str;
        return StringTools.unescape(str);
    }

    protected NodeDiagram parseDiagram(JSONObject inputJson){
        NodeDiagram diagram=new NodeDiagram();
        String g=inputJson.getString("g");
        String name=inputJson.getString("name");
        String label=inputJson.getString("label");
        diagram.setLabel(label);
        diagram.setName(name);
        if(StringUtils.isNotBlank(g)){
            String[] info=g.split(",");
            if(info.length!=4){
                throw new IllegalArgumentException("Node "+inputJson.getString("name")+" diagram info is invalide!");
            }
            BigDecimal x= Utils.toBigDecimal(info[0]);
            diagram.setX(x.intValue());
            BigDecimal y=Utils.toBigDecimal(info[1]);
            diagram.setY(y.intValue());
            BigDecimal w=Utils.toBigDecimal(info[2]);
            diagram.setWidth(w.intValue());
            BigDecimal h=Utils.toBigDecimal(info[3]);
            diagram.setHeight(h.intValue());
        }
        String x = inputJson.getString("left");
        String y=inputJson.getString("height");
//        String width=element.attributeValue("width");
//        String height=element.attributeValue("height");
        if(StringUtils.isNotBlank(x)){
            // 前端传过来的信息有'px'字符，需要进行处理
            x = x.replace("px", "");
            BigDecimal bd=Utils.toBigDecimal(x);
            diagram.setX(bd.intValue());
        }
        if(StringUtils.isNotBlank(y)){
            y = y.replace("px", "");
            BigDecimal bd=Utils.toBigDecimal(y);
            diagram.setY(bd.intValue());
        }
//        if(StringUtils.isNotBlank(width)){
//            BigDecimal bd=Utils.toBigDecimal(width);
//            diagram.setWidth(bd.intValue());
//        }
//        if(StringUtils.isNotBlank(height)){
//            BigDecimal bd=Utils.toBigDecimal(height);
//            diagram.setHeight(bd.intValue());
//        }
        return diagram;
    }


    protected void parseNodeCommonInfo(JSONObject inputJson, Node node) {
        node.setName(unescape(inputJson.getString("name")));
        String label=inputJson.getString("label");
        if(StringUtils.isNotEmpty(label)){
            node.setLabel(unescape(label));
        }

        if(StringUtils.isNoneEmpty(inputJson.getString("description"))){
            node.setDescription(unescape(inputJson.getString("description").trim()));
        }
        node.setEventHandlerBean(unescape(inputJson.getString("event_handler_bean")));
        int x=0,y=0,width=80,height=40;

//        String g = element.attributeValue("g");
//
//        // 用于分割字符串
//        if(StringUtils.isNotBlank(g)){
//            StringTokenizer tokenizer=new StringTokenizer(g,",");
//            if (tokenizer.countTokens() == 4) {
//                x=Integer.valueOf(tokenizer.nextToken());
//                y=Integer.valueOf(tokenizer.nextToken());
//                width=Integer.valueOf(tokenizer.nextToken());
//                height=Integer.valueOf(tokenizer.nextToken());
//            }
//        }

        node.setX(x);
        node.setY(y);
        node.setWidth(width);
        node.setHeight(height);
        String xAttr = inputJson.getString("left");
        String yAttr = inputJson.getString("top");

        // 防止后期需要加上没个节点的大小，现在只是定义了每个节点的位置信息
//        String widthAttr=element.attributeValue("width");
//        String heightAttr=element.attributeValue("height");
        if(StringUtils.isNotBlank(xAttr)){
            xAttr = xAttr.replace("px", "");
            BigDecimal bd=new BigDecimal(xAttr);
            node.setX(bd.intValue());
        }
        if(StringUtils.isNotBlank(yAttr)){
            yAttr = yAttr.replace("px", "");
            BigDecimal bd=new BigDecimal(yAttr);
            node.setY(bd.intValue());
        }
//        if(StringUtils.isNotBlank(widthAttr)){
//            BigDecimal bd=new BigDecimal(widthAttr);
//            node.setWidth(bd.intValue());
//        }
//        if(StringUtils.isNotBlank(heightAttr)){
//            BigDecimal bd=new BigDecimal(heightAttr);
//            node.setHeight(bd.intValue());
//        }

    }

    protected List<Assignee> parserAssignees(JSONObject inputJson){
        List<Assignee> assignees=new ArrayList<Assignee>();

        List<JSONObject> nodeList = JSON.parseArray(inputJson.getString("assignee"), JSONObject.class);

        for(JSONObject obj : nodeList){
//            if(!obj.getString("type").equals("assignee")){
//                continue;
//            }
            String id=unescape(obj.getString("id"));
            String name=unescape(obj.getString("name"));
            String providerId=unescape(obj.getString("providerId"));
            Assignee assignee=new Assignee();
            assignee.setId(id);
            assignee.setName(name);
            assignee.setProviderId(providerId);
            assignees.add(assignee);
        }
        return assignees;
    }

    protected List<UserData> parseUserData(JSONObject inputJson){
        List<UserData> data=new ArrayList<UserData>();

        if (StringUtils.isNotEmpty(inputJson.getString("user_data"))) {
            // 如果返回的是列表作出对应的调整即可
            JSONObject childJson = JSONObject.parseObject(inputJson.getString("user_data"));
            data.add(new UserData(childJson.getString("key"),childJson.getString("value")));
        }

        return data;
    }

    /*protected List<FormElement> parseFormElements(JSONObject inputJson){

        List<FormElement> formElements=new ArrayList<FormElement>();
        for(Object object:element.elements()){
            if(!(object instanceof Element))continue;
            Element ele=(Element)object;
            if(!ele.getName().equals("form-element"))continue;
            FormElement formElement=new FormElement();
            formElement.setName(ele.attributeValue("name"));
            formElement.setCaption(ele.attributeValue("caption"));
            formElement.setDataType(ele.attributeValue("data-type"));
            formElement.setDefaultValue(ele.attributeValue("default-value"));
            formElement.setEditorType(ele.attributeValue("editor-type"));
            formElement.setRequired(Boolean.valueOf(ele.attributeValue("required")));
            formElement.setAuthority(Authority.valueOf(ele.attributeValue("authority")));
            List<Mapping> mappings=null;
            for(Object obj:ele.elements()){
                if(!(obj instanceof Element)){
                    continue;
                }
                Element mappingElement=(Element)obj;
                if(!mappingElement.getName().equals("mapping")){
                    continue;
                }
                if(mappings==null){
                    mappings=new ArrayList<Mapping>();
                }
                Mapping mapping=new Mapping();
                mapping.setKey(mappingElement.attributeValue("key"));
                mapping.setLabel(mappingElement.attributeValue("label"));
                mappings.add(mapping);
            }
            formElement.setMappings(mappings);
            formElements.add(formElement);
        }
        return formElements;

    }*/

    protected String parseFormUrl(JSONObject inputJson){
        String allId = inputJson.getString("formDesignerId") + "-" + inputJson.getString("onlineTableId") + "-" + inputJson.getString("onlineDataId");

        return allId;
    }

    protected List<SequenceFlowImpl> parseFlowElement(JSONObject inputJson, long processId, boolean parseChildren){
        List<SequenceFlowImpl> flows=new ArrayList<SequenceFlowImpl>();

//        log.info("inputJson:{}11" + inputJson);

        // 将节点信息转化为list
//        List<JSONObject> nodeList = JSON.parseArray(inputJson.getString("id"), JSONObject.class);
//        if (nodeList == null)
//            return flows;
//
//        for (JSONObject jsonObject : nodeList) {
//            for(Parser parser:parsers){
//                if(!parser.support(inputJson)){
//                    continue;
//                }
//                Object processElement=parser.parse(jsonObject,processId,parseChildren);
//                if(processElement instanceof SequenceFlowImpl){
//                    SequenceFlowImpl flow=(SequenceFlowImpl)processElement;
//                    flows.add(flow);
//                }
//                break;
//            }
//        }

        for (Parser parser : parsers) {
            String parserName = parser.getClass().toString().substring(parser.getClass().toString().lastIndexOf(".") + 1);
            if ("SequenceFlowParser".equals(parserName)) {
                Object processElement= parser.parse(inputJson,processId,parseChildren);
                if(processElement instanceof SequenceFlowImpl){
                    SequenceFlowImpl flow=(SequenceFlowImpl)processElement;
                    log.info("flow:{}" + flow.getLabel());
                    flows.add(flow);
                }
            }
        }

        return flows;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        parsers=applicationContext.getBeansOfType(Parser.class).values();
    }
}

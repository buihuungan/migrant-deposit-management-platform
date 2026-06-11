package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.process.node.ForeachNode;
import com.ontoweb.newworkflow.core.process.node.ForeachType;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:09
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class ForeachParser extends AbstractParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        ForeachNode node=new ForeachNode();
        node.setProcessId(processId);
        parseNodeCommonInfo(inputJson, node);
        node.setSequenceFlows(parseFlowElement(inputJson,processId,parseChildren));
        String type=inputJson.getString("foreach_type");
        if(StringUtils.isNotEmpty(type)){
            node.setForeachType(ForeachType.valueOf(type));
        }
        node.setVariable(unescape(inputJson.getString("var")));
        if(StringUtils.isNotBlank(inputJson.getString("process_variable"))){
            node.setProcessVariable(unescape(inputJson.getString("process_variable")));
        }else{
            node.setProcessVariable(unescape(inputJson.getString("in")));
        }
        node.setHandlerBean(unescape(inputJson.getString("handler_bean")));
        NodeDiagram diagram=parseDiagram(inputJson);
        diagram.setIcon("/icons/foreach.svg");
        diagram.setShapeType(ShapeType.Circle);
        diagram.setBorderWidth(1);
        node.setDiagram(diagram);
        return node;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.getString("type").equals("foreach");
    }
}

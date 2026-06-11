package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.process.node.ForkNode;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:10
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class ForkParser extends AbstractParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        ForkNode node=new ForkNode();
        node.setProcessId(processId);
        parseNodeCommonInfo(inputJson, node);
        node.setSequenceFlows(parseFlowElement(inputJson,processId,parseChildren));
        NodeDiagram diagram=parseDiagram(inputJson);
        diagram.setIcon(inputJson.getString("icon"));
        diagram.setShapeType(ShapeType.Circle);
        diagram.setBorderWidth(1);
        node.setDiagram(diagram);
        return node;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.getString("type").equals("fork");
    }
}

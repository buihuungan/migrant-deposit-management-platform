package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.process.node.ActionNode;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:04
 * @description：动作节点相关解析器
 * @modified By：
 * @version: $
 */
@Service
public class ActionParser extends AbstractParser {

    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        ActionNode node=new ActionNode();
        node.setProcessId(processId);
        parseNodeCommonInfo(inputJson, node);
        node.setSequenceFlows(parseFlowElement(inputJson,processId,parseChildren));
        node.setHandlerBean(unescape(inputJson.get("handler_bean").toString()));

        // 节点图形，现在这一块还没有具体设计，保留原来的代码，后期可能会有修改
        NodeDiagram diagram=parseDiagram(inputJson);
        diagram.setIcon(inputJson.getString("ico"));
        diagram.setShapeType(ShapeType.Rectangle);
        diagram.setBorderWidth(1);
        diagram.setBorderColor("3, 104, 154");
        diagram.setBackgroundColor("250, 250, 250");
        node.setDiagram(diagram);
        return node;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.get("type").toString().equals("action");
    }
}

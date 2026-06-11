package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.process.node.DecisionNode;
import com.ontoweb.newworkflow.core.process.node.DecisionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:07
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class DecisionParser extends AbstractParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        DecisionNode node=new DecisionNode();
        node.setProcessId(processId);
        parseNodeCommonInfo(inputJson, node);
        node.setSequenceFlows(parseFlowElement(inputJson,processId,parseChildren));
        node.setDecisionType(DecisionType.valueOf(inputJson.getString("decision_type")));

        if(node.getDecisionType().equals(DecisionType.Expression)){

            // 传进来的直接就是对应的节点信息了
            //List<JSONObject> nodeList = JSON.parseArray(inputJson.getString("nodeList"), JSONObject.class);
            node.setExpression(inputJson.getString("expression"));
            // 不需要添加这段代码
//            if(StringUtils.isBlank(node.getExpression())){
//                node.setExpression(inputJson.getString("expression"));
//            }
        }
        node.setHandlerBean(unescape(inputJson.getString("handler_bean")));
        NodeDiagram diagram=parseDiagram(inputJson);
        diagram.setIcon(inputJson.getString("ico"));
        diagram.setShapeType(ShapeType.Circle);
        diagram.setBorderWidth(1);
        node.setDiagram(diagram);
        return node;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return "decision".equals(inputJson.get("type").toString());
    }
}

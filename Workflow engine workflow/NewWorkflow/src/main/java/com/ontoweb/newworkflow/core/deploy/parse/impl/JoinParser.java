package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.process.node.JoinNode;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:11
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class JoinParser extends AbstractParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        JoinNode node=new JoinNode();
        node.setProcessId(processId);
        parseNodeCommonInfo(inputJson, node);
        node.setSequenceFlows(parseFlowElement(inputJson,processId,parseChildren));
        String multiplicity=inputJson.getString("multiplicity");
        if(StringUtils.isNotEmpty(multiplicity)){
            int mul=Integer.parseInt(multiplicity);
            if(mul>0){
                node.setMultiplicity(mul);
            }
        }
        String percentMultiplicity = inputJson.getString("percent_multiplicity");
        if(StringUtils.isNotEmpty(percentMultiplicity)){
            int mul=Integer.parseInt(percentMultiplicity);
            if(mul>0){
                node.setPercentMultiplicity(mul);
            }
        }
        NodeDiagram diagram=parseDiagram(inputJson);
        diagram.setIcon(inputJson.getString("icon"));
        diagram.setShapeType(ShapeType.Circle);
        diagram.setBorderWidth(1);
        node.setDiagram(diagram);
        return node;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.getString("type").equals("join");
    }
}

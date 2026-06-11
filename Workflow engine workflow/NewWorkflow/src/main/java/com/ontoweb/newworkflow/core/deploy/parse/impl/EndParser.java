package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.process.node.EndNode;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:08
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class EndParser extends AbstractParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        EndNode node=new EndNode();
        node.setProcessId(processId);
        parseNodeCommonInfo(inputJson, node);
        String terminate=inputJson.getString("terminate");
        if(StringUtils.isNotEmpty(terminate)){
            node.setTerminate(Boolean.valueOf(terminate));
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
        return inputJson.getString("type").equals("end");
    }
}

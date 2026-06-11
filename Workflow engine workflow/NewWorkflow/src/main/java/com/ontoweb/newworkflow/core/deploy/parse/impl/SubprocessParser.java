package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.process.node.SubprocessNode;
import com.ontoweb.newworkflow.core.process.node.SubprocessType;
import com.ontoweb.newworkflow.core.process.node.SubprocessVariable;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:13
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class SubprocessParser extends AbstractParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        SubprocessNode node=new SubprocessNode();
        node.setProcessId(processId);
        parseNodeCommonInfo(inputJson, node);
        node.setSequenceFlows(parseFlowElement(inputJson,processId,parseChildren));
        String type=inputJson.getString("subprocess_type");
        if(StringUtils.isNotEmpty(type)){
            node.setSubprocessType(SubprocessType.valueOf(type));
        }
        String completeStartTask=inputJson.getString("complete_start_task");
        if(StringUtils.isNotBlank(completeStartTask)){
            node.setCompleteStartTask(Boolean.valueOf(completeStartTask));
        }
        node.setSubprocessId(unescape(inputJson.getString("subprocess_id")));
        node.setSubprocessKey(unescape(inputJson.getString("subprocess_key")));
        node.setSubprocessName(unescape(inputJson.getString("subprocess_name")));
        List<SubprocessVariable> inVars=new ArrayList<SubprocessVariable>();
        List<SubprocessVariable> outVars=new ArrayList<SubprocessVariable>();

        List<JSONObject> nodeList = JSON.parseArray(inputJson.get("nodeList").toString(), JSONObject.class);
        for(JSONObject obj : nodeList){
            if(obj.getString("type").equals("in-subprocess-variable")){
                SubprocessVariable var=new SubprocessVariable(unescape(obj.getString("in_parameter_key")),unescape(obj.getString("out_parameter_key")));
                inVars.add(var);
            }
            if(obj.getString("type").equals("out_subprocess_variable")){
                SubprocessVariable var=new SubprocessVariable(unescape(obj.getString("in_parameter_key")),unescape(obj.getString("out_parameter_key")));
                outVars.add(var);
            }
        }
        node.setInVariables(inVars);
        node.setOutVariables(outVars);
        NodeDiagram diagram=parseDiagram(inputJson);
        diagram.setIcon(inputJson.getString("icon"));
        diagram.setShapeType(ShapeType.Rectangle);
        diagram.setBorderWidth(1);
        diagram.setBorderColor("3, 104, 154");
        diagram.setBackgroundColor("250, 250, 250");
        node.setDiagram(diagram);
        return node;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.getString("type").equals("subprocess");
    }
}

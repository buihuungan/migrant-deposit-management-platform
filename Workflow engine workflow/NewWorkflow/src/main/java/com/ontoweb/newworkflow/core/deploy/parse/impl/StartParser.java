package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractTaskParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.process.node.StartNode;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:13
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class StartParser extends AbstractTaskParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        StartNode node=new StartNode();
        node.setProcessId(processId);
        node.setTaskName(unescape(inputJson.getString("task_name")));
        node.setUrl(unescape(inputJson.getString("url")));
        node.setFormTemplate(unescape(inputJson.getString("form_template")));
        parseNodeCommonInfo(inputJson, node);
        node.setSequenceFlows(parseFlowElement(inputJson,processId,parseChildren));
        // 没有使用过uflo自带的工作流绑定表单
        //node.setFormElements(parseFormElements(element));

        // 组件的权限设置
        node.setComponentAuthorities(parseComponentAuthorities(inputJson));
        NodeDiagram diagram=parseDiagram(inputJson);
        diagram.setIcon(inputJson.getString("icon"));
        diagram.setShapeType(ShapeType.Circle);
        diagram.setBorderWidth(1);
        node.setDiagram(diagram);
        return node;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.getString("type").equals("start");
    }
}

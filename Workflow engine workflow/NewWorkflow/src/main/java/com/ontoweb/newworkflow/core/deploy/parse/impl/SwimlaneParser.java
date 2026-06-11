package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractParser;
import com.ontoweb.newworkflow.core.process.node.AssignmentType;
import com.ontoweb.newworkflow.core.process.swimlane.Swimlane;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:14
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class SwimlaneParser extends AbstractParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        Swimlane swimlane=new Swimlane();
        String name=inputJson.getString("name");
        swimlane.setName(unescape(name));
        if(!StringUtils.isBlank(inputJson.getString("description"))){
            String desc=unescape(inputJson.getString("description"));
            desc=(desc.length()>120?desc.substring(0,120):desc);
            swimlane.setDescription(desc);
        }
        String type = inputJson.getString("assignment_type");
        if(StringUtils.isNotEmpty(type)){
            swimlane.setAssignmentType(AssignmentType.valueOf(type));
        }
        swimlane.setAssignmentHandlerBean(unescape(inputJson.getString("assignment_handler_bean")));
        swimlane.setExpression(unescape(inputJson.getString("expression")));
        AssignmentType assignmentType=swimlane.getAssignmentType();
        if(assignmentType!=null && (assignmentType.equals(AssignmentType.Assignee))){
            swimlane.setAssignees(parserAssignees(inputJson));
        }
        return swimlane;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.getString("type").equals("swimlane");
    }
}

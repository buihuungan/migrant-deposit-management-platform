package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.deploy.StringTools;
import com.ontoweb.newworkflow.core.deploy.parse.AbstractTaskParser;
import com.ontoweb.newworkflow.core.diagram.NodeDiagram;
import com.ontoweb.newworkflow.core.diagram.ShapeType;
import com.ontoweb.newworkflow.core.model.task.DateUnit;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import com.ontoweb.newworkflow.core.process.node.AssignmentType;
import com.ontoweb.newworkflow.core.process.node.TaskNode;
import com.ontoweb.newworkflow.core.process.node.reminder.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:16
 * @description：
 * @modified By：
 * @version: $
 */

@Service
@Slf4j
public class TaskParser extends AbstractTaskParser {
    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {

        TaskNode node=new TaskNode();
        node.setProcessId(processId);
        parseNodeCommonInfo(inputJson, node);
        node.setSequenceFlows(parseFlowElement(inputJson,processId,parseChildren));
        String countersignMultiplicity= inputJson.getString("countersign_multiplicity");
        if(StringUtils.isNotEmpty(countersignMultiplicity)){
            node.setCountersignMultiplicity(Integer.valueOf(countersignMultiplicity));
        }
        String countersignPercentMultiplicity=inputJson.getString("countersign_percent_multiplicity");
        if(StringUtils.isNotEmpty(countersignPercentMultiplicity)){
            node.setCountersignPercentMultiplicity(Integer.valueOf(countersignPercentMultiplicity));
        }
        String countersignExpression=inputJson.getString("countersign_expression");
        if(StringUtils.isNotEmpty(countersignExpression)){
            node.setCountersignExpression(countersignExpression);
        }
        String countersignHandler=inputJson.getString("countersign_handler");
        if(StringUtils.isNotEmpty(countersignHandler)){
            node.setCountersignHandler(countersignHandler);
        }
        String taskListenerBean=inputJson.getString("task_listener_bean");
        if(StringUtils.isNotEmpty(taskListenerBean)){
            node.setTaskListenerBean(taskListenerBean);
        }
        node.setAssignmentType(AssignmentType.valueOf(inputJson.getString("assignment_type")));
        node.setAssignmentHandlerBean(unescape(inputJson.getString("assignment_handler_bean")));
        node.setSwimlane(unescape(inputJson.getString("swimlane")));
        node.setExpression(unescape(inputJson.getString("expression")));
        if(node.getAssignmentType().equals(AssignmentType.Assignee)){
            node.setAssignees(parserAssignees(inputJson));
        }

        String allowSpecifyAssigne=inputJson.getString("allow_specify_assignee");
        if(StringUtils.isNotEmpty(allowSpecifyAssigne)){
            node.setAllowSpecifyAssignee(Boolean.valueOf(allowSpecifyAssigne));
        }
        String taskType=inputJson.getString("task_type");
        if(StringUtils.isNotEmpty(taskType)){
            node.setTaskType(TaskType.valueOf(taskType));
        }
//        node.setUrl(unescape(inputJson.getString("url")));
        node.setUrl(unescape(parseFormUrl(inputJson)));
        node.setFormTemplate(unescape(inputJson.getString("form_template")));
        node.setTaskName(unescape(inputJson.getString("task_name")));
        node.setComponentAuthorities(parseComponentAuthorities(inputJson));
        node.setDueDefinition(parseDueDefinition(inputJson));

        // 将formDesignerId、onlineTableId、onlineDataId放在url中保存下来，在前端展示时再做处理进行返回
//        node.setFormElements(parseFormElements(inputJson));


        node.setUserData(parseUserData(inputJson));
        NodeDiagram diagram=parseDiagram(inputJson);
        diagram.setIcon(inputJson.getString("icon"));
        diagram.setShapeType(ShapeType.Rectangle);
        diagram.setBorderWidth(1);
        diagram.setBorderColor("3, 104, 154");
        diagram.setBackgroundColor("250, 250, 250");
        node.setDiagram(diagram);
        return node;
    }

    private DueDefinition parseDueDefinition(JSONObject inputJson){
        DueDefinition reminderDef=null;

        if (StringUtils.isNotEmpty(inputJson.getString("due"))) {
            if(reminderDef==null){
                reminderDef=new DueDefinition();
            }
            String dayStr=inputJson.getString("day");
            if(StringUtils.isNotEmpty(dayStr)){
                reminderDef.setDay(Integer.valueOf(dayStr));
            }
            String dayHour=inputJson.getString("hour");
            if(StringUtils.isNotEmpty(dayHour)){
                reminderDef.setHour(Integer.valueOf(dayHour));
            }
            String minuteStr=inputJson.getString("minute");
            if(StringUtils.isNotEmpty(minuteStr)){
                reminderDef.setMinute(Integer.valueOf(minuteStr));
            }
            reminderDef.setCalendarInfos(parseCalendarProviders(inputJson));

            if(StringUtils.isNotEmpty(inputJson.getString("once_reminder"))){
                JSONObject childJson = JSONObject.parseObject(inputJson.getString("once_reminder"));
                Reminder reminder=new Reminder();
                reminder.setHandlerBean(unescape(childJson.getString("handler_bean")));
                reminderDef.setReminder(reminder);
            }else if(StringUtils.isNotEmpty(inputJson.getString("period_reminder"))){
                JSONObject childJson = JSONObject.parseObject(inputJson.getString("period_reminder"));
                PeriodReminder reminder=new PeriodReminder();
                reminder.setHandlerBean(unescape(childJson.getString("handler_bean")));
                reminder.setRepeat(Integer.valueOf(childJson.getShort("repeat")));
                reminder.setUnit(DateUnit.valueOf(childJson.getString("unit")));
                reminder.setCalendarInfos(parseCalendarProviders(childJson));
                reminderDef.setReminder(reminder);
            }else if(StringUtils.isNotEmpty(inputJson.getString("due_action"))){
                JSONObject childJson = JSONObject.parseObject(inputJson.getString("due_action"));
                DueAction action=new DueAction();
                String day= childJson.getString("day");
                if(StringUtils.isNotEmpty(day)){
                    action.setDay(Integer.valueOf(day));
                }
                String hour=childJson.getString("hour");
                if(StringUtils.isNotEmpty(hour)){
                    action.setHour(Integer.valueOf(hour));
                }
                String minute=childJson.getString("minute");
                if(StringUtils.isNotEmpty(minute)){
                    action.setMinute(Integer.valueOf(minute));
                }
                action.setCalendarInfos(parseCalendarProviders(childJson));
                action.setHandlerBean(unescape(childJson.getString("handler_bean")));
                reminderDef.setDueAction(action);
            }
        }
        return reminderDef;
    }

    private List<CalendarInfo> parseCalendarProviders(JSONObject inputJson){
        List<CalendarInfo> list=new ArrayList<CalendarInfo>();

        if (StringUtils.isNotEmpty(inputJson.getString("calendar_provider"))){
            CalendarInfo info=new CalendarInfo();
            info.setId(unescape(inputJson.getString("id")));
            info.setName(unescape(inputJson.getString("name")));
            list.add(info);
        }
        return list;
    }

    @Override
    public boolean support(JSONObject inputJson) {
        return inputJson.get("type").equals("task");
    }
}

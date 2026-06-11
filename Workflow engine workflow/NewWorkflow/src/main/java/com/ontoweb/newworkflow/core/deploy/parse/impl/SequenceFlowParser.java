package com.ontoweb.newworkflow.core.deploy.parse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.ontoweb.newworkflow.core.deploy.parse.Parser;
import com.ontoweb.newworkflow.core.diagram.Point;
import com.ontoweb.newworkflow.core.diagram.SequenceFlowDiagram;
import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.process.flow.ConditionType;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlowImpl;
import com.ontoweb.newworkflow.handler.dao.BlobDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/5 下午4:55
 * @description：
 * @modified By：
 * @version: $
 */
@Slf4j
@Service
public class SequenceFlowParser implements Parser {
    @Autowired
    private BlobDao blobDao;

    @Override
    public Object parse(JSONObject inputJson, long processId, boolean parseChildren) {
        List<SequenceFlowImpl> returnList = new ArrayList<>();
        try {
            Blob blob = blobDao.findblobProcessId(processId);
            byte[] blobValue = blob.getBlobValue();
            String str = new String(blobValue, "UTF-8");
            JSONObject inputJsonAll = JSONObject.parseObject(str);

            // 节点之间的连线
            List<JSONObject> lineList = JSON.parseArray(inputJsonAll.getString("lineList"), JSONObject.class);

            // 所有节点信息，为了获取下一节点
            List<JSONObject> nodeList = JSON.parseArray(inputJsonAll.getString("nodeList"), JSONObject.class);
            Map<String, String> nodeIdNameMap = new HashMap<>();
            for (JSONObject jsonObject : nodeList) {
                nodeIdNameMap.put(jsonObject.getString("id"), jsonObject.getString("name"));
            }

            for (JSONObject jsonObject : lineList) {
                SequenceFlowImpl flow = new SequenceFlowImpl();
                if (jsonObject.getString("from").equals(inputJson.getString("id"))) {

                    flow.setProcessId(processId);
                    flow.setName(unescape(inputJson.getString("name")));
                    flow.setToNode(unescape(nodeIdNameMap.get(jsonObject.getString("to"))));
                    flow.setLabel(unescape(jsonObject.getString("label")));
                    String conditionType = inputJson.getString("condition_type");
                    if (StringUtils.isNotEmpty(conditionType)) {
                        flow.setConditionType(ConditionType.valueOf(conditionType));
                        flow.setExpression(inputJson.getString("expression"));
                        flow.setHandlerBean(inputJson.getString("handler_bean"));
                    }
                    flow.setDiagram(parseDiagram(inputJson));
                    String g = jsonObject.getString("g");
                    if (StringUtils.isNotBlank(g)) {
                        int pos = g.indexOf(":");
                        if (pos > -1) {
                            g = g.substring(0, pos);
                            g = g.replaceAll(";", ",");
                        } else {
                            g = null;
                        }
                    }
                    flow.setG(g);
                    returnList.add(flow);
                    //flow = new SequenceFlowImpl();
                }
            }

            SequenceFlowImpl returnFlow = new SequenceFlowImpl();
            String name = "$", toNode = "$", label = "$";
            if (returnList.size() > 1) {
                for (SequenceFlowImpl flow1 : returnList) {
                    log.info("flow1.getName():{}" + flow1.getLabel());
                    name += flow1.getName() + ",";
                    toNode += flow1.getToNode() + ",";
                    label += flow1.getLabel() + ",";
                }
                returnFlow.setName(name);
                returnFlow.setToNode(toNode);
                returnFlow.setLabel(label);
                returnFlow.setProcessId(processId);
                return returnFlow;
            }

            return returnList.get(0);

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private SequenceFlowDiagram parseDiagram(JSONObject inputJson){
        SequenceFlowDiagram diagram=new SequenceFlowDiagram();
        diagram.setBorderColor("0,69,123");
        diagram.setFontColor("0,69,123");
        diagram.setBorderWidth(2);
        String name=inputJson.getString("name");
        diagram.setTo(inputJson.getString("to"));
        diagram.setName(name);
        String g=inputJson.getString("g");
        if(StringUtils.isEmpty(g))return diagram;
        String[] pointInfos=null;
        if(StringUtils.isNotEmpty(name)){
            String[] info=g.split(":");
            if(info.length==1){
                diagram.setLabelPosition(info[0]);
                return diagram;
            }
            pointInfos=info[0].split(";");
            diagram.setLabelPosition(info[1]);
        }else{
            String[] info=g.split(":");
            if(info.length==0){
                pointInfos=g.split(";");
            }else{
                pointInfos=info[0].split(";");
            }
            if(pointInfos.length==0){
                return diagram;
            }
        }
        diagram.setPoints(buildPoint(pointInfos));
        return diagram;
    }

    private List<Point> buildPoint(String[] info){
        List<Point> points=new ArrayList<Point>();
        for(String diagram:info){
            String[] d=diagram.split(",");
            if(StringUtils.isEmpty(d[0])){
                continue;
            }
            if(d.length>1){
                for(int i=0;i<d.length;i+=2){
                    Point point=new Point();
                    point.setX(Integer.valueOf(d[i]));
                    point.setY(Integer.valueOf(d[i+1]));
                    points.add(point);
                }
            }
        }
        return points;
    }

    @Override
    public boolean support(JSONObject inputJson) {
//        return StringUtils.isNotEmpty(inputJson.getString("lineList"));
        return true;
    }

    protected String unescape(String str){
        if(StringUtils.isEmpty(str))return str;
        str= StringEscapeUtils.escapeXml(str);
        return StringEscapeUtils.unescapeXml(str);
    }
}

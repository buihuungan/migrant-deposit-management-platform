package com.ontoweb.newworkflow.handler.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.SaveProcessResourceCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/12 下午10:44
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class DefaultFileProcessProvider implements ProcessProvider{

    @Autowired
    private SaveProcessResourceCommand commandService;


    public String prefix="file:";

    private String fileStoreDir;

    private boolean disabled;
    @Override
    public InputStream loadProcess(String file) {
        if(file.startsWith(prefix)){
            file=file.substring(prefix.length(),file.length());
        }
        String fullPath=fileStoreDir+"/"+file;
        try {
            return new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * update默认是false，如果是更新的改为true
     * @param jsonObject
     * @return
     */
    @Override
    public Boolean saveProcess(JSONObject jsonObject) {
        try {
            String s = jsonObject.get("content").toString();
            byte[] ba = s.getBytes("UTF-8");
            JSONObject processByteMap = JSONObject.parseObject(ba, JSONObject.class);
            List<JSONObject> nodeList = JSON.parseArray(processByteMap.getString("nodeList"), JSONObject.class);
            for (JSONObject nodeJson : nodeList) {
                //Expression类型的决策节点需要处理expression表达式
                if (nodeJson.getString("type").equals("decision")&&nodeJson.getString("decision_type").equals("Expression")) {
                    List<JSONObject> expressionList = JSON.parseArray(nodeJson.getString("if_expression"), JSONObject.class);
                    String expressionResult = "${";
                    for (JSONObject expressionJson : expressionList) {
                        expressionResult += expressionJson.getString("formId") + expressionJson.getString("symbol") + expressionJson.getString("textValue") + "&&";
                    }
                    expressionResult = expressionResult.substring(0, expressionResult.length() - 2);
                    expressionResult += "?";
                    JSONObject resultJson = JSONObject.parseObject(nodeJson.getString("result"));
                    String trueResult = "";
                    String falseResult = "";
                    Set<String> strings = resultJson.keySet();
                    for (String key : strings) {
                        if (resultJson.getString(key).equals("true")) {
                            trueResult = key;
                        }
                        if (resultJson.getString(key).equals("false")) {
                            falseResult = key;
                        }
                    }

                    expressionResult += "\"" + trueResult + "\"" + ":" + "\"" + falseResult + "\"";
                    expressionResult += "}";
                    nodeJson.put("expression", expressionResult);
                }

            }

            processByteMap.put("nodeList", nodeList);
            ba = processByteMap.toString().getBytes("UTF-8");

            // 现在条件表达式和引擎所需要的格式不一致，因此需要进行调整

            return commandService.handlerData(ba, jsonObject.get("name").toString(), Boolean.parseBoolean(jsonObject.get("update").toString()));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("has a error in save process file");
        }
    }

    @Override
    public void deleteProcess(String file) {
        if(file.startsWith(prefix)){
            file=file.substring(prefix.length(),file.length());
        }
        String fullPath=fileStoreDir+"/"+file;
        File f=new File(fullPath);
        if(f.exists()){
            f.delete();
        }
    }

    @Override
    public List<ProcessFile> loadAllProcesses() {
        File file=new File(fileStoreDir);
        List<ProcessFile> list=new ArrayList<ProcessFile>();
        for(File f:file.listFiles()){
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(f.lastModified());
            list.add(new ProcessFile(f.getName(),calendar.getTime()));
        }
        Collections.sort(list, new Comparator<ProcessFile>(){
            @Override
            public int compare(ProcessFile f1, ProcessFile f2) {
                return f2.getUpdateDate().compareTo(f1.getUpdateDate());
            }
        });
        return list;
    }

//    @PostConstruct
//    public void afterPropertiesSet() throws Exception {
//        File file=new File(fileStoreDir);
//        if(file.exists()){
//            return;
//        }
//
//        // 获取servlet的绝对路径
//        String basePath = request.getServletContext().getRealPath("/");
//        fileStoreDir=basePath+fileStoreDir;
//        file=new File(fileStoreDir);
//        if(!file.exists()){
//            file.mkdirs();
//        }
//    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean support(String fileName) {
        return fileName.startsWith(prefix);
    }
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getName() {
        return "默认文件系统";
    }
}

package com.ontoweb.newworkflow.core.deploy.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.DeployProcessCommand;
import com.ontoweb.newworkflow.core.command.impl.DeployProcessResourceCommand;
import com.ontoweb.newworkflow.core.deploy.ProcessDeployer;
import com.ontoweb.newworkflow.core.deploy.parse.impl.ProcessParser;
import com.ontoweb.newworkflow.core.deploy.validate.ProcessValidateException;
import com.ontoweb.newworkflow.core.deploy.validate.impl.ProcessValidator;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:36
 * @description：默认流程部署
 * @modified By：
 * @version: $
 */
@Service
public class DefaultProcessDeployer implements ProcessDeployer {
//    private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//    private ProcessValidator processValidator;

    @Autowired
    private DeployProcessCommand commandService;
    @Autowired
    private DeployProcessResourceCommand deployProcessResourceCommand;

    @Override
    public ProcessDefinition deploy(JSONObject inputJson) {
        ProcessDefinition process=null;
        long processId = IDGenerator.getInstance().nextId();

        // 接收传递过来的流程json文件
        process = deployFile(inputJson,null,processId,false);

        return process;
    }

    @Override
    public ProcessDefinition deploy(JSONObject inputJson,int version, long processId) {
        ProcessDefinition process=deployFile(inputJson,version,processId,true);
        return process;
    }

    private ProcessDefinition deployFile(JSONObject inputJson,Integer version,long processId,boolean update) {
        ProcessDefinition process=null;
        try{
            // 在使用json的过程中不需要使用验证过程，由前端保证json文件的正确性
            // validateProcess(new ByteArrayInputStream(bytes));
            process = ProcessParser.parseProcess(inputJson,processId,update);
            process.setCreateDate(new Date());
            if(update){
                process.setVersion(version);
            }
            commandService.operateProcessDefinition(process,update);

            String s = inputJson.toJSONString();
            byte[] ba = s.getBytes("UTF-8");
            deployProcessResourceCommand.handlerData(ba,process.getName(),processId,update);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }

        return process;
    }


//    private ProcessDefinition deployZip(ZipInputStream zipInputStream,long processId) {
//        ProcessDefinition process=null;
//        try {
//            ZipEntry zipEntry = zipInputStream.getNextEntry();
//            while (zipEntry != null) {
//                String entryName = zipEntry.getName();
//                if(entryName.endsWith(PROCESS_EXTENSION_NAME)){
//                    byte[] bytes = IOUtils.toByteArray(zipInputStream);
//                    validateProcess(new ByteArrayInputStream(bytes));
//                    process=ProcessParser.parseProcess(bytes,processId,false);
//                    commandService.executeCommand(new DeployProcessCommand(process,false));
//                    commandService.executeCommand(new DeployProcessResourceCommand(bytes,entryName,processId,false));
//                }else if(entryName.endsWith(PROCESSIMG_EXTENSION_NAME)){
//                    byte[] bytes = IOUtils.toByteArray(zipInputStream);
//                    commandService.executeCommand(new DeployProcessResourceCommand(bytes,entryName,processId,false));
//                }
//                zipEntry = zipInputStream.getNextEntry();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            IOUtils.closeQuietly(zipInputStream);
//        }
//        return process;
//    }
//
//    private void validateProcess(InputStream inputStream) {
//        StringBuffer errorInfo=new StringBuffer();
//        try {
//            Document document = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
//            List<String> errors=new ArrayList<String>();
//            List<String> nodeNames=new ArrayList<String>();
//            Element element=document.getDocumentElement();
//            if(processValidator.support(element)){
//                processValidator.validate(element, errors, nodeNames);
//                if(errors.size()>0){
//                    for(int i=0;i<errors.size();i++){
//                        errorInfo.append((i+1)+"."+errors.get(i)+"\r\r");
//                    }
//                }
//            }else{
//                errorInfo.append("当前XML文件不是一个合法的UFLO流程模版文件");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        String msg=errorInfo.toString();
//        if(StringUtils.isNotEmpty(msg)){
//            throw new ProcessValidateException(msg);
//        }
//    }
}

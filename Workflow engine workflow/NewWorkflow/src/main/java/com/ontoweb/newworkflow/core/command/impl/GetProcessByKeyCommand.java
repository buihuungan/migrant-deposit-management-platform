package com.ontoweb.newworkflow.core.command.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.deploy.ProcessDeployer;
import com.ontoweb.newworkflow.core.deploy.parse.impl.ProcessParser;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import org.hibernate.Session;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:03
 * @description：根据key获取流程信息
 * @modified By：
 * @version: $
 */
public class GetProcessByKeyCommand implements Command<ProcessDefinition> {
    private String processKey;
    public GetProcessByKeyCommand(String processKey){
        this.processKey=processKey;
    }

    @Override
    public ProcessDefinition execute(Context context) {
        Session session=context.getSession();
        ProcessDefinition p=(ProcessDefinition)session.createQuery("from "+ProcessDefinition.class.getName()+" where key=:key").setString("key", processKey).uniqueResult();
        if(p==null){
            return null;
        }else{
            return parseProcess(p.getId(),p.getName(),session);
        }
    }

    private ProcessDefinition parseProcess(long processId,String processName,Session session){
        String hql="from "+ Blob.class.getName()+" where processId=:processId and name=:name";
        Blob blob=(Blob)session.createQuery(hql).setLong("processId",processId).setString("name",processName+ ProcessDeployer.PROCESS_EXTENSION_NAME).uniqueResult();
        try {
            String inputstring = new String(blob.getBlobValue(), "UTF-8");
            JSONObject inputJson = JSONObject.parseObject(inputstring);
            ProcessDefinition process= ProcessParser.parseProcess(inputJson,processId,true);
            process.setId(processId);
            return process;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

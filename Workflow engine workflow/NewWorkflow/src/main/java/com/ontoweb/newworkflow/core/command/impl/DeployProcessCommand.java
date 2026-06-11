package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.handler.dao.ProcessDefinitionDao;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:50
 * @description：部署流程
 * @modified By：
 * @version: $
 */
@Service
public class DeployProcessCommand {

    @Resource
    private ProcessDefinitionDao processDefinitionDao;

    private ProcessDefinition process;
    private boolean update = false;

    @Transactional(rollbackFor = Exception.class)
    public ProcessDefinition operateProcessDefinition(ProcessDefinition process, Boolean update) {
        String key=process.getKey();
        if(!update && StringUtils.isNotEmpty(key)){
            int size = processDefinitionDao.getProcessDefinitionByKey(key).size();
            if(size>0){
                throw new IllegalArgumentException("Process definition "+process.getName()+"'s key "+key+" is not the only one!");
            }
        }
        int newVersion=1;
        if(!update){
            List<ProcessDefinition> processes = processDefinitionDao.getProcessDefinitionByName(process.getName());
            if(processes.size()>0){
                newVersion=processes.get(0).getVersion()+1;
                process.setVersion(newVersion);
            }else{
                process.setVersion(newVersion);
            }
        }
        if(StringUtils.isEmpty(key)){
            key=process.getName()+"-"+newVersion;
            process.setKey(key);
        }

        // jpa不论是保存还是更新都使用save方法
        processDefinitionDao.save(process);
        return process;
    }
}

package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import com.ontoweb.newworkflow.handler.dao.BlobDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:51
 * @description：部署流程源信息
 * @modified By：
 * @version: $
 */
@Service
public class DeployProcessResourceCommand{

    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private BlobDao blobDao;

    @Transactional(rollbackFor = Exception.class)
    public Blob handlerData(byte[] processRes, String name, long processId, Boolean update) {
        if(update){

            Long blobId = blobDao.findByProcessId(processId);
            blobDao.deleteUflo(blobId);
        }
        Blob lob=new Blob();
        lob.setId(idGenerator.nextId());
        lob.setBlobValue(processRes);
        lob.setName(name);
        lob.setProcessId(processId);
        blobDao.save(lob);

        return lob;
    }
}

package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.model.BlobTemp;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import com.ontoweb.newworkflow.handler.dao.BlobTempDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/22 下午10:47
 * @description：保存流程文件，部署前的操作
 * @modified By：
 * @version: $
 */
@Component
@Slf4j
public class SaveProcessResourceCommand{

    @Autowired
    private BlobTempDao blobTempDao;

    @Autowired
    private IDGenerator idGenerator;

    @Transactional(rollbackFor = Exception.class)
    public Boolean handlerData(byte[] processRes, String name, Boolean update) {
        try {
            if (update) {
                Long blobId = blobTempDao.findByName(name);
                blobTempDao.deleteUfloTemp(blobId);
            }
            BlobTemp blobTemp = new BlobTemp();
            blobTemp.setId(idGenerator.nextId());
            blobTemp.setBlobValue(processRes);
            blobTemp.setName(name);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            blobTemp.setCreateTime(sdf.format(date));


            blobTempDao.save(blobTemp);

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }
}

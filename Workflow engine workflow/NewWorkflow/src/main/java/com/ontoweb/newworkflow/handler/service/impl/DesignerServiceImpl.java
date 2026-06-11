package com.ontoweb.newworkflow.handler.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.BlobTemp;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.handler.dao.BlobDao;
import com.ontoweb.newworkflow.handler.dao.BlobTempDao;
import com.ontoweb.newworkflow.handler.service.DesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/31 下午7:40
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class DesignerServiceImpl implements DesignerService {

    @Resource
    private BlobTempDao blobTempDao;
    @Resource
    private BlobDao blobDao;
    @Autowired
    private ProcessService processService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deployByFileName(JSONObject jsonObject) {

        byte[] inputBytes = blobTempDao.getByFileName(jsonObject.getString("fileName"));
        String str = "";
        try {
            str = new String(inputBytes, "UTF-8");
            JSONObject inputJson = JSONObject.parseObject(str);
            ProcessDefinition processDefinition = processService.deployProcess(inputJson);

            if (processDefinition != null) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public Boolean deleteFile(Long id) {
        try {
            blobTempDao.deleteFile(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public JSONObject modifyFile(Long id) {
        BlobTemp blobTemp = blobTempDao.getByFileId(id);
        byte[] blobValue = blobTemp.getBlobValue();
        try {
            String str = new String(blobValue, "UTF-8");
            log.info("inputJson:{}" + str);
            JSONObject inputJson = JSONObject.parseObject(str);
            String categories = inputJson.getString("category_id");
            String[] split = categories.split("-");
            List<String> categoryList = new ArrayList<>(Arrays.asList(split));
            int domain = categoryList.indexOf("domain");
            int type = categoryList.indexOf("type");
            int end = categoryList.size();
            List<String> domains = new ArrayList<>();
            List<String> types = new ArrayList<>();
            if (domain > -1 && type > -1) {
                int j = 0;
                for (int i = 1; i < type; i++) {
                    domains.add(categoryList.get(i));
                }


            }
            if (domain > -1 && type < 0) {
                for (int i = 1; i < end; i++) {
                    domains.add(categoryList.get(i));
                }
            }

            if (type > -1) {
                for (int i = type + 1; i < end; i++) {
                    types.add(categoryList.get(i));
                }
            }
            inputJson.put("domains", domains);
            inputJson.put("types", types);

            return inputJson;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFilebyName(String name) {
        try {
            blobTempDao.deleteFilebyName(name);
            blobDao.deleteUflobyName(name);
        } catch (Exception e) {
            log.error(e.getMessage());
            //触发回滚事件 保证数据库的一致性
            throw new RuntimeException("删除流程失败: " + e.getMessage(), e);
        }

        return true;
    }


}

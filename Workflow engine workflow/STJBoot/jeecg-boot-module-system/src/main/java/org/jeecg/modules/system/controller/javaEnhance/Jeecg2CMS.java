package org.jeecg.modules.system.controller.javaEnhance;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecg.modules.system.service.feign.CmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/17 上午10:51
 * @description：将jeecg的表同步到cms的表中(新增)
 * @modified By：
 * @version: 1.0$
 */

@Component("jeecg2cmsAdd")
@Slf4j
public class Jeecg2CMS implements CgformEnhanceJavaInter {

    @Autowired
    private CmsService cmsService;

    @Override
    public int execute(String s, Map<String, Object> map) throws BusinessException {
        return 0;
    }

    @Override
    public int execute(String tableName, JSONObject jsonObject) throws BusinessException {
        log.info("tableName:{}" + tableName);
        log.info("jsonObject:{}" + jsonObject.toJSONString());

        return cmsService.insertValue(tableName, jsonObject);
    }
}

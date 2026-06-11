package org.jeecg.modules.system.controller.javaEnhance;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecg.modules.system.service.feign.CmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/18 下午2:45
 * @description：jeecg2Cms编辑接口
 * @modified By：
 * @version: $
 */
@Slf4j
@Component("jeecg2cmsUpdate")
public class Jeecg2CMSUpdate implements CgformEnhanceJavaInter {

    @Autowired
    private CmsService cmsService;

    @Override
    public int execute(String s, Map<String, Object> map) throws BusinessException {
        return 0;
    }

    @Override
    public int execute(String tablename, JSONObject jsonObject) throws BusinessException {
        log.info("jsonObject:{}" + jsonObject);

        return cmsService.editValue(tablename, jsonObject);
    }
}

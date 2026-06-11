package org.jeecg.modules.system.service.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/17 上午11:21
 * @description：cms相关的微服务接口
 * @modified By：
 * @version: 1.0$
 */
@Component
@FeignClient("CMS")
public interface CmsService {
    @PostMapping("/cms/cms/addTableValue")
    int insertValue(@RequestParam String tableName, @RequestBody JSONObject jsonObject);

    @PostMapping("/cms/cms/editTableValue")
    int editValue(@RequestParam String tableName, @RequestBody JSONObject jsonObject);

    @PostMapping("/cms/cms/deleteTableValue")
    int deleteValue(@RequestParam String tableName, @RequestBody JSONObject jsonObject);
}

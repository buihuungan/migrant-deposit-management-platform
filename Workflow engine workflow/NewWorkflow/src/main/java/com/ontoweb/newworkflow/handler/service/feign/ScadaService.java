package com.ontoweb.newworkflow.handler.service.feign;

import com.ontoweb.newworkflow.handler.VO.CictOfflineDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 10:49 2022/2/14
 */
@Service
@FeignClient("DISPLAYIDENTITY")
public interface ScadaService {

    @PostMapping("/CictOffline/deleteOfflineData")
    void deleteOfflineData(@RequestBody CictOfflineDataVo cictOfflineDataVo);
}

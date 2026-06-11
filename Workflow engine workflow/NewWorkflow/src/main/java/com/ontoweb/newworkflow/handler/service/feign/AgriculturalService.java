package com.ontoweb.newworkflow.handler.service.feign;

import com.ontoweb.newworkflow.handler.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenjian
 */
@Service
@FeignClient(value = "STJ-BOOT",contextId = "agricultural")
public interface AgriculturalService {
    @PostMapping("/stj/agricultural/add")
    Result<?> insertAgriculturalInspection(@RequestParam String caseId, @RequestParam String caseName);

    @PostMapping("/stj/agricultural/addidentify")
    Result<?> insertAgriculturalIdentify(@RequestParam String caseId, @RequestParam String caseName);

    @PostMapping("/stj/agricultural/addvalue")
    Result<?> insertAgriculturalValue(@RequestParam String caseId,@RequestParam String caseName);
}

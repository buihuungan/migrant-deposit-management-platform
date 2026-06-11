package com.ontoweb.newworkflow.handler.service.feign;

import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient("FMAKING")
public interface FormDesignerService {

    @GetMapping("/admin/desform/{id}/getConent")
    Result<?> getConent(@PathVariable("id") @ApiParam("表单ID") String id);
}

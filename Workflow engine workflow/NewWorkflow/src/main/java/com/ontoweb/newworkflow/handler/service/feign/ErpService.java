package com.ontoweb.newworkflow.handler.service.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ontoweb.newworkflow.handler.utils.Result;


/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 16:15 2022/3/22
 */

@Service
@FeignClient("ERP")
public interface ErpService {

    @PostMapping("/dailyProductionSchedulingPlan/insertInitData")
    @ApiOperation("获取日生产的数据")
    Result<?> insertInitData(@RequestParam("exampleId") String exampleId,@RequestParam("wareIdString")String wareIdString);
}

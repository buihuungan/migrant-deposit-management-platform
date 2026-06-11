package com.ontoweb.newworkflow.handler.service.feign;

import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 16:13 2022/3/22
 */

@Service
@FeignClient("ORDER")
public interface OrderService {

    @PostMapping("/cictOrder/insertInitData")
    @ApiOperation("预制销售订单数据")
    Result<?> insertInitData(@RequestParam("exampleId") String exampleId,@RequestParam("wareIdString")String wareIdString);
}

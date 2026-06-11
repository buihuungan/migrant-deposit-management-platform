package cn.zhenglili.fmaking.feign.online.service;

import cn.zhenglili.fmaking.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName：OnlineFeignService
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/8/8 16:42
 **/
@Service
@FeignClient("FANGZHEN-SYSTEM")
public interface OnlineFeignService {

    @GetMapping({"/online/cgform/field/listByHeadId"})
    Result<?> listByHeadId(@RequestParam("headId") String headId);


    @GetMapping({"/online/cgform/head/queryById"})
    Result<?> queryById(@RequestParam(name = "id") String id);
}

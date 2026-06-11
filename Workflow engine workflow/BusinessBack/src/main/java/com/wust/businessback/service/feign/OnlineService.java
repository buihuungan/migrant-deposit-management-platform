package com.wust.businessback.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.wust.businessback.constant.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Service
@FeignClient("STJ-BOOT")
public interface OnlineService {
    /**
     * 获取表单数据
     *
     * @param code
     * @param id
     * @return
     */
    @GetMapping({"/stj/online/cgform/api/form/{code}/{id}"})
    Result<?> getForm(@PathVariable("code") String code, @PathVariable("id") String id);

    @PutMapping({"/admin/cgform/api/form/{code}?tableType=1"})
    Result<?> editTableValue(@PathVariable("code") String code, JSONObject jsonObject);
}

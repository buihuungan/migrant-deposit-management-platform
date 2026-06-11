package com.ontoweb.newworkflow.margin.Bean;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.handler.NodeEventHandler;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.handler.constant.RedisKey;
import com.ontoweb.newworkflow.handler.entity.ProcessMapping;
import com.ontoweb.newworkflow.handler.mapper.ProcessMappingMapper;
import com.ontoweb.newworkflow.handler.service.feign.OnlineService;
import com.ontoweb.newworkflow.handler.utils.RedisUtils;
import com.ontoweb.newworkflow.handler.utils.Result;
import com.ontoweb.newworkflow.margin.entity.MainPayment;
import com.ontoweb.newworkflow.margin.mapper.MainPaymentMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class PaymentEventHandlerSecond implements NodeEventHandler {
    @Autowired
    private OnlineService onlineService;
    @Autowired
    private ProcessMappingMapper processMappingMapper;
    @Autowired
    private MainPaymentMapper mainPaymentMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void enter(Node node, ProcessInstance processInstance, Context context) {

    }

    @Override
    public void leave(Node node, ProcessInstance processInstance, Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long processInstanceId =  processInstance.getId();

        //获取传参
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtils.get(RedisKey.PROCESSINSTANCE+processInstanceId)));
        String onlineTableId = jsonObject.getString("onlineTableId");
        String onlineDataId = jsonObject.getString("onlineDataId");

        //流程映射信息
        QueryWrapper<ProcessMapping> processMappingWrapper = new QueryWrapper<>();
        processMappingWrapper.eq("process_instance_id", processInstanceId);
        ProcessMapping processMapping = processMappingMapper.selectOne(processMappingWrapper);

        if (processMapping != null){
            //更新数据
            MainPayment mainPayment = mainPaymentMapper.selectById(processMapping.getMainId());

            Result<?> res = onlineService.getForm(onlineTableId, onlineDataId);
            Map result = (Map) res.getResult();
            mainPayment.setMoney(Double.valueOf(result.get("money").toString()));
            mainPayment.setRemainingAmount(Double.valueOf(result.get("money").toString()));
            mainPayment.setProportions(result.get("proportions").toString());
            mainPayment.setReason(result.get("reason").toString());
            try {
                mainPayment.setStorageStartDate(sdf.parse(result.get("storage_start_date").toString()));
                mainPayment.setStorageEndDate(sdf.parse(result.get("storage_end_date").toString()));
            }catch (Exception e){
                e.printStackTrace();
            }
            int tag = mainPaymentMapper.updateById(mainPayment);

            if (tag > 0){
                log.info("marginPayment 第二次数据更新成功:{}",mainPayment);
            }else {
                log.info("marginPayment 第二次数据更新失败");
            }
        }
    }
}

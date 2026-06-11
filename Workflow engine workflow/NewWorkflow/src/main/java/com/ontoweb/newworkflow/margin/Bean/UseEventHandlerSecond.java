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
import com.ontoweb.newworkflow.margin.entity.MainUse;
import com.ontoweb.newworkflow.margin.mapper.MainChangeMapper;
import com.ontoweb.newworkflow.margin.mapper.MainPaymentMapper;
import com.ontoweb.newworkflow.margin.mapper.MainUseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class UseEventHandlerSecond implements NodeEventHandler {
    @Autowired
    private OnlineService onlineService;
    @Autowired
    private ProcessMappingMapper processMappingMapper;
    @Autowired
    private MainPaymentMapper mainPaymentMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MainUseMapper mainUseMapper;

    @Override
    public void enter(Node node, ProcessInstance processInstance, Context context) {

    }

    @Override
    public void leave(Node node, ProcessInstance processInstance, Context context) {

        Long processInstanceId =  processInstance.getId();

        //获取传参
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtils.get(RedisKey.PROCESSINSTANCE+processInstanceId)));
        String onlineTableId = jsonObject.getString("onlineTableId");
        String onlineDataId = jsonObject.getString("onlineDataId");

        //流程映射信息
        QueryWrapper<ProcessMapping> processMappingWrapper = new QueryWrapper<>();
        processMappingWrapper.eq("process_instance_id", processInstanceId);
        ProcessMapping processMapping = processMappingMapper.selectOne(processMappingWrapper);

        Result<?> res = onlineService.getForm(onlineTableId, onlineDataId);
        Map result = (Map) res.getResult();
        //使用结果
        int useResult = Integer.valueOf(result.get("use_result").toString());
        //修改保证金金额
        if (processMapping != null && useResult == 1){
            //保证金使用的数据
            MainUse mainUse = mainUseMapper.selectById(processMapping.getMainId());
            //保证金存缴 数据
            MainPayment mainPayment = mainPaymentMapper.selectById(mainUse.getMainId());
            if (mainPayment != null){
                Double remainingAmount = mainPayment.getRemainingAmount();
                Double useMoney = mainUse.getUseMoneyLow();
                //修改剩余金额
                mainPayment.setRemainingAmount(remainingAmount - useMoney);

                int tag = mainPaymentMapper.updateById(mainPayment);
                if (tag > 0){
                    log.info("mainPayment 剩余金额修改成功:{}",mainPayment);
                }else {
                    log.info("mainPayment 剩余金额修改失败");
                }
            }
        }
    }
}

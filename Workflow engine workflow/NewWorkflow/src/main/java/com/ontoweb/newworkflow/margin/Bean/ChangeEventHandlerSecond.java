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
import com.ontoweb.newworkflow.margin.entity.MainChange;
import com.ontoweb.newworkflow.margin.entity.MainPayment;
import com.ontoweb.newworkflow.margin.entity.MainUse;
import com.ontoweb.newworkflow.margin.mapper.MainChangeMapper;
import com.ontoweb.newworkflow.margin.mapper.MainPaymentMapper;
import com.ontoweb.newworkflow.margin.mapper.MainUseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ChangeEventHandlerSecond implements NodeEventHandler {
    @Autowired
    private ProcessMappingMapper processMappingMapper;
    @Autowired
    private MainPaymentMapper mainPaymentMapper;
    @Autowired
    private MainChangeMapper mainChangeMapper;

    @Override
    public void enter(Node node, ProcessInstance processInstance, Context context) {

    }

    @Override
    public void leave(Node node, ProcessInstance processInstance, Context context) {

        Long processInstanceId =  processInstance.getId();

        //流程映射信息
        QueryWrapper<ProcessMapping> processMappingWrapper = new QueryWrapper<>();
        processMappingWrapper.eq("process_instance_id", processInstanceId);
        ProcessMapping processMapping = processMappingMapper.selectOne(processMappingWrapper);

        if (processMapping != null){
            //保证金方式变更 数据
            MainChange mainChange = mainChangeMapper.selectById(processMapping.getMainId());
            //保证金存缴 数据
            MainPayment mainPayment = mainPaymentMapper.selectById(mainChange.getMainId());
            //修改存缴方式
            if (mainPayment != null){
                mainPayment.setDepositWay(mainChange.getNewDepositMethod());
                int tag = mainPaymentMapper.updateById(mainPayment);
                if (tag > 0){
                    log.info("mainPayment 存缴方式修改成功:{}",mainPayment);
                }else {
                    log.info("mainPayment 存缴方式修改失败");
                }
            }
        }
    }
}

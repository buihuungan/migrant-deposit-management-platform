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
public class UseEventHandlerFirst implements NodeEventHandler {
    @Autowired
    private OnlineService onlineService;
    @Autowired
    private ProcessMappingMapper processMappingMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MainUseMapper mainUseMapper;

    @Override
    public void enter(Node node, ProcessInstance processInstance, Context context) {

    }

    @Override
    public void leave(Node node, ProcessInstance processInstance, Context context) {

        String mainId = null;
        Long processInstanceId =  processInstance.getId();

        //获取传参
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtils.get(RedisKey.PROCESSINSTANCE+processInstanceId)));
        Long isWithdraw = jsonObject.containsKey("isWithdraw") ? jsonObject.getLong("isWithdraw") : null;
        String frontId = jsonObject.containsKey("frontId") ? jsonObject.getString("frontId") : null;
        String onlineTableId = jsonObject.getString("onlineTableId");
        String onlineDataId = jsonObject.getString("onlineDataId");

        if (frontId != null || isWithdraw != null){
            MainUse mainUse = new MainUse();
            Result<?> res = onlineService.getForm(onlineTableId, onlineDataId);
            Map result = (Map) res.getResult();

            mainUse.setCreateTime(new Date());
            mainUse.setLegalMobile(result.get("legal_mobile").toString());
            mainUse.setLegalRepresentative(result.get("legal_representative").toString());
            mainUse.setBank(result.get("bank").toString());
            mainUse.setBankMobile(result.get("bank_mobile").toString());
            mainUse.setPaymentRecipient(result.get("payment_recipient").toString());
            mainUse.setIdNumber(result.get("id_number").toString());
            mainUse.setUseReason(result.get("use_reason").toString());
            mainUse.setUseMoneyLow(Double.valueOf(result.get("use_money_low").toString()));
            mainUse.setDocNumber(result.get("doc_number").toString());

            //撤回时 再次提交时更新数据
            if (isWithdraw != null) {
                QueryWrapper<ProcessMapping> processMappingWrapper = new QueryWrapper<>();
                processMappingWrapper.eq("process_instance_id", processInstanceId);
                ProcessMapping processMapping = processMappingMapper.selectOne(processMappingWrapper);
                if (processMapping != null) {
                    mainUse.setId(processMapping.getMainId());
                    int tag = mainUseMapper.updateById(mainUse);

                    if (tag > 0){
                        log.info("mainUse 撤回数据更新成功:{}",mainUse);
                    }else {
                        log.info("mainUse 撤回数据更新失败");
                    }
                }
            }else {
                mainUse.setMainId(frontId);
                int tag = mainUseMapper.insert(mainUse);
                if (tag > 0){
                    //第一次插入数据
                    mainId = mainUse.getId();
                    log.info("mainUse 第一次数据插入成功:{}",mainUse);
                }else {
                    log.info("mainUse 第一次数据插入失败");
                }
            }
        }

        //映射表维护
        if (mainId != null) {
            ProcessMapping processMapping = new ProcessMapping();
            processMapping.setMainId(mainId);
            processMapping.setProcessId(processInstance.getProcessId());
            processMapping.setProcessInstanceId(processInstanceId);
            processMapping.setProcessHisInstanceId(processInstanceId + 1);
            processMapping.setTableName("main_use");
            processMappingMapper.insert(processMapping);
        }
    }
}

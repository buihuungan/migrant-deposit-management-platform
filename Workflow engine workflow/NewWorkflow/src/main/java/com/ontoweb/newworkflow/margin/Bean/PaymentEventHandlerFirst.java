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
import com.ontoweb.newworkflow.margin.mapper.MainChangeMapper;
import com.ontoweb.newworkflow.margin.mapper.MainPaymentMapper;
import com.ontoweb.newworkflow.margin.mapper.MainUseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
@Slf4j
@Component
public class PaymentEventHandlerFirst implements NodeEventHandler {
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
        MainPayment mainPayment = new MainPayment();
        mainPayment.setCreateTime(new Date());
        Long processInstanceId =  processInstance.getId();
        String mainId = null;
        //获取传参
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtils.get(RedisKey.PROCESSINSTANCE+processInstanceId)));
        Long isWithdraw = jsonObject.containsKey("isWithdraw") ? jsonObject.getLong("isWithdraw") : null;
        String onlineTableId = jsonObject.getString("onlineTableId");
        String onlineDataId = jsonObject.getString("onlineDataId");


        Result<?> res = onlineService.getForm(onlineTableId, onlineDataId);
        Map result = (Map) res.getResult();

        mainPayment.setEnterpriseName(result.get("company_name").toString());
        mainPayment.setProjectName(result.get("project_name").toString());
        mainPayment.setContractAmount(Double.valueOf(result.get("money").toString()));
        mainPayment.setProjectAddress(result.get("project_adress").toString());
        mainPayment.setResponsiblePerson(result.get("responsible_person").toString());
        mainPayment.setMobile(result.get("mobile").toString());
        mainPayment.setAddressDetail(result.get("adress_detail").toString());
        mainPayment.setCreditCode(result.get("credit_code").toString());
        mainPayment.setPostalCode(result.get("postal_code").toString());
        mainPayment.setCompanyAddress(result.get("company_address").toString());
        mainPayment.setDepositWay(result.get("deposit_way").toString());

        try {
            mainPayment.setFormCreateDate(sdf.parse((String) result.get("start_date")));
            mainPayment.setFormEndDate(sdf.parse((String) result.get("end_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //撤回时 再次提交时更新数据
        if (isWithdraw != null) {
            //流程映射信息
            QueryWrapper<ProcessMapping> processMappingWrapper = new QueryWrapper<>();
            processMappingWrapper.eq("process_instance_id", processInstanceId);
            ProcessMapping processMapping = processMappingMapper.selectOne(processMappingWrapper);
            if (processMapping != null) {
                mainPayment.setId(processMapping.getMainId());
                int tag = mainPaymentMapper.updateById(mainPayment);

                if (tag > 0){
                    log.info("mainPayment 撤回数据更新成功:{}",mainPayment);
                }else {
                    log.info("mainPayment 撤回数据更新失败");
                }
            }
        }else{
            //第一次插入数据
            int tag = mainPaymentMapper.insert(mainPayment);
            if (tag > 0){
                //第一次插入数据
                mainId = mainPayment.getId();
                log.info("mainPayment 第一次数据插入成功:{}",mainPayment);
            }else {
                log.info("mainPayment 第一次数据插入失败");
            }
        }

        //映射表维护
        if (mainId != null) {
            ProcessMapping processMapping = new ProcessMapping();
            processMapping.setMainId(mainId);
            processMapping.setProcessId(processInstance.getProcessId());
            processMapping.setProcessInstanceId(processInstanceId);
            processMapping.setProcessHisInstanceId(processInstanceId + 1);
            processMapping.setTableName("main_payment");
            processMappingMapper.insert(processMapping);
        }
    }
}

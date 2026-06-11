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
import com.ontoweb.newworkflow.margin.entity.MainUse;
import com.ontoweb.newworkflow.margin.mapper.MainChangeMapper;
import com.ontoweb.newworkflow.margin.mapper.MainPaymentMapper;
import com.ontoweb.newworkflow.margin.mapper.MainUseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class ChangeEventHandlerFirst implements NodeEventHandler {
    @Autowired
    private OnlineService onlineService;
    @Autowired
    private ProcessMappingMapper processMappingMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MainChangeMapper mainChangeMapper;

    @Override
    public void enter(Node node, ProcessInstance processInstance, Context context) {

    }

    @Override
    public void leave(Node node, ProcessInstance processInstance, Context context) {

        Long processInstanceId =  processInstance.getId();
        String mainId = null;

        //获取传参
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtils.get(RedisKey.PROCESSINSTANCE+processInstanceId)));
        Long isWithdraw = jsonObject.containsKey("isWithdraw") ? jsonObject.getLong("isWithdraw") : null;
        String frontId = jsonObject.containsKey("frontId") ? jsonObject.getString("frontId") : null;
        String onlineTableId = jsonObject.getString("onlineTableId");
        String onlineDataId = jsonObject.getString("onlineDataId");

        if (frontId != null || isWithdraw != null){
            MainChange mainChange = new MainChange();
            Result<?> res = onlineService.getForm(onlineTableId, onlineDataId);
            Map result = (Map) res.getResult();
            mainChange.setCreateTime(new Date());
            mainChange.setOldDepositMethod(result.get("old_deposit_method").toString());
            mainChange.setNewDepositMethod(result.get("new_deposit_method").toString());
            mainChange.setChangeReason(result.get("change_reason").toString());

            if (StringUtils.isNotEmpty(result.get("handling_method").toString())){
                mainChange.setHandlingMethod(Integer.valueOf(result.get("handling_method").toString()));
            }

            //撤回时 再次提交时更新数据
            if (isWithdraw != null) {
                //流程映射信息
                QueryWrapper<ProcessMapping> processMappingWrapper = new QueryWrapper<>();
                processMappingWrapper.eq("process_instance_id", processInstanceId);
                ProcessMapping processMapping = processMappingMapper.selectOne(processMappingWrapper);
                if (processMapping != null) {
                    mainChange.setId(processMapping.getMainId());
                    int tag = mainChangeMapper.updateById(mainChange);

                    if (tag > 0){
                        log.info("mainChange 撤回数据更新成功:{}",mainChange);
                    }else {
                        log.info("mainChange 撤回数据更新失败");
                    }
                }
            }else {
                //第一次插入数据
                mainChange.setMainId(frontId);
                int tag = mainChangeMapper.insert(mainChange);
                if (tag > 0){
                    //第一次插入数据
                    mainId = mainChange.getId();
                    log.info("mainChange 第一次数据插入成功:{}",mainChange);
                }else {
                    log.info("mainChange 第一次数据插入失败");
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
            processMapping.setTableName("main_change");
            processMappingMapper.insert(processMapping);
        }

    }
}

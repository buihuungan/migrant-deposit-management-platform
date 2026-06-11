package org.jeecg.modules.system.service.myService.impl;

import org.jeecg.modules.system.mapper.myMapper.AgriculturalMapper;
import org.jeecg.modules.system.service.myService.AgriculturalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenjian
 */
@Service
public class AgriculturalServiceImpl implements AgriculturalService {
    @Resource
    private AgriculturalMapper agriculturalMapper;
    @Override
    public void insert(String caseId, String caseName) {
        agriculturalMapper.insert(caseId, caseName);
    }

    @Override
    public void insertidentify(String caseId, String caseName) {
        agriculturalMapper.insertidentify(caseId, caseName);
    }

    @Override
    public void insertvalue(String caseId, String caseName) {
        agriculturalMapper.insertvalue(caseId, caseName);
    }

}

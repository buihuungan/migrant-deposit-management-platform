package org.jeecg.modules.system.service.myService;

import org.springframework.stereotype.Service;

/**
 * @author chenjian
 */
@Service
public interface AgriculturalService {
    void insert(String caseId, String caseName);

    void insertidentify(String caseId, String caseName);

    void insertvalue(String caseId, String caseName);
}

package org.jeecg.modules.system.mapper.myMapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chenjian
 */
@Mapper
public interface AgriculturalMapper {
    @Insert("INSERT INTO inspection (id,case_id, case_name) VALUES (UUID() , #{caseId}, #{caseName})")
    void insert(String caseId, String caseName);

    @Insert("INSERT INTO identify (id,case_id, case_name) VALUES (UUID() , #{caseId}, #{caseName})")
    void insertidentify(String caseId, String caseName);

    @Insert("INSERT INTO value_assessment (id,case_id, case_name) VALUES (UUID() , #{caseId}, #{caseName})")
    void insertvalue(String caseId, String caseName);
}

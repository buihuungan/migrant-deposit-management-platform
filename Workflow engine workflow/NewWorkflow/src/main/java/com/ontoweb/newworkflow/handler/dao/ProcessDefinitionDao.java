package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/31 下午2:32
 * @description：ProcessDefinition的数据处理
 * @modified By：
 * @version: $
 */
public interface ProcessDefinitionDao extends JpaRepository<ProcessDefinition, Integer> {
    /**
     * 根据流程流程key值获取流程数量
     * @param key
     * @return
     */
    @Query("select pd from ProcessDefinition pd where pd.key = ?1")
    List<ProcessDefinition> getProcessDefinitionByKey(String key);

    @Query("select pd from ProcessDefinition pd where pd.name = ?1 order by pd.version desc ")
    List<ProcessDefinition> getProcessDefinitionByName(String name);

    @Query(value = "SELECT id_, category_id_, effect_date_, start_process_url_, version_, b.name_, description_, create_date_, key_, " +
            "PROCESS_MAIN_TABLE_, PROCESS_MAIN_KEY_,  MAIN_TABLE_ASSOCIATION_KEY_, PROCESS_ASSOCIATION_TABLE_, PROCESS_ASSOCIATION_KEY_ " +
            "FROM uflo_process AS process " +
            "JOIN (SELECT name_, MAX(version_) AS version FROM uflo_process ps " +
            "      WHERE (?1 = '' OR ps.category_id_ = ?1) " +
            "      GROUP BY name_) AS b " +
            "ON process.name_ = b.name_ AND process.version_ = b.version",
            nativeQuery = true)
    List<ProcessDefinition> getProcessList(String categoryId);

}

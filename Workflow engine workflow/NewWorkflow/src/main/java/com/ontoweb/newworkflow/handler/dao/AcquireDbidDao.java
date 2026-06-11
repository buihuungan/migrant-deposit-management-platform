package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.ContextProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/27 下午3:49
 * @description：生成id的工具
 * @modified By：
 * @version: $
 */
public interface AcquireDbidDao extends CrudRepository<ContextProperty, Integer> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select cp from ContextProperty cp where cp.key = ?1")
    List<ContextProperty> getlist(String idKey);


}

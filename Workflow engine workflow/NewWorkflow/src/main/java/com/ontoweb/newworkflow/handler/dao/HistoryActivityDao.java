package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.HistoryActivity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/4 下午10:14
 * @description：
 * @modified By：
 * @version: $
 */
public interface HistoryActivityDao extends JpaRepository<HistoryActivity, Integer> {
}

package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.HistoryVariable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/10 下午8:59
 * @description：
 * @modified By：
 * @version: $
 */
public interface HistoryVariableDao extends JpaRepository<HistoryVariable, Integer> {
}

package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.variable.Variable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/10 下午9:01
 * @description：
 * @modified By：
 * @version: $
 */
public interface VariableDao extends JpaRepository<Variable, Integer> {
}

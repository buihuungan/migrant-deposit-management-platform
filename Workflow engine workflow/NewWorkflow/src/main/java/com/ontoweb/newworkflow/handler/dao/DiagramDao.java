package com.ontoweb.newworkflow.handler.dao;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.ContextProperty;
import com.ontoweb.newworkflow.handler.entity.Diagram;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/10 上午11:13
 * @description：
 * @modified By：
 * @version: $
 */
@Mapper
public interface DiagramDao extends CrudRepository<Diagram, Integer> {
    @Query(value = "SELECT " +
            "a.id_ as id," +
            "a.process_id_ as process_id," +
            "a.node_name_ as node_name," +
            "a.create_date_ as create_date," +
            "a.end_date_ as end_date," +
            "a.task_id_ AS task_id," +
            "a.business_id_ AS business_id," +
            "a.assignee_ AS assignee," +
            "a.type_ AS type," +
            "a.url_ AS url," +
            "a.prev_task_ as prev_task, " +
            "a.state_ AS state " +
            "FROM " +
            "uflo_his_task a " +
            "WHERE " +
            "a.process_instance_id_ = ?1 " +
            "ORDER BY " +
            "a.create_date_ ASC", nativeQuery = true)
    List<Diagram> getHisTaskInformation(long proInsId);

    @Query(value = "SELECT " +
            "a.id_ as id," +
            "a.process_id_ as process_id," +
            "a.node_name_ as node_name," +
            "a.create_date_ as create_date," +
            "a.end_date_ as end_date," +
            "0 AS task_id," + // 故意写错的，保证不匹配
            "a.business_id_ AS business_id," +
            "a.assignee_ AS assignee," +
            "a.type_ AS type," +
            "a.prev_task_ as prev_task," +
            "a.url_ AS url," +
            "a.state_ AS state " +
            "FROM " +
            "uflo_task a " +
            "WHERE " +
            "a.process_instance_id_ = ?1 " +
            "ORDER BY " +
            "a.create_date_ ASC", nativeQuery = true)
    List<Diagram> getTaskInformation(long proInsId);
}

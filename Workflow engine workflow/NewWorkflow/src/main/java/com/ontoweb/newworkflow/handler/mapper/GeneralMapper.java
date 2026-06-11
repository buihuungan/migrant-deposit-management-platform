package com.ontoweb.newworkflow.handler.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@DS("slave")
@Mapper
public interface GeneralMapper {
    @Select("select * from ${tableName}")
    List<JSONObject> selectTableData(String tableName);
}

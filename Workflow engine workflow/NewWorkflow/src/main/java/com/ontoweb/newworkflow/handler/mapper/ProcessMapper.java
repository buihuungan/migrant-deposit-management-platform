package com.ontoweb.newworkflow.handler.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ontoweb.newworkflow.handler.entity.UfloProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProcessMapper extends BaseMapper<UfloProcess>  {
}

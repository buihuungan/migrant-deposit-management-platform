package com.ontoweb.newworkflow.margin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ontoweb.newworkflow.margin.entity.MainUse;
import org.apache.ibatis.annotations.Mapper;

//@DS("slave")
@Mapper
public interface MainUseMapper extends BaseMapper<MainUse> {
}

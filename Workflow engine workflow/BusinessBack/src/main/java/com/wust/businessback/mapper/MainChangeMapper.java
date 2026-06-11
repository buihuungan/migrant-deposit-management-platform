package com.wust.businessback.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wust.businessback.entity.MainChange;
import org.apache.ibatis.annotations.Mapper;

@DS("slave")
@Mapper
public interface MainChangeMapper extends BaseMapper<MainChange> {
}

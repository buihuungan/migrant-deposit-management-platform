package com.ontoweb.newworkflow.margin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ontoweb.newworkflow.margin.entity.MainPayment;
import org.apache.ibatis.annotations.Mapper;

//@DS("slave")
@Mapper
public interface MainPaymentMapper extends BaseMapper<MainPayment> {
}

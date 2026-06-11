package cn.zhenglili.fmaking.desform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：DesFormQuery
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/7/29 15:51
 **/
@Data
@ApiModel(description = "查询时用的实体")
public class DesFormQuery {

    @ApiModelProperty(value = "表单设计名称")
    private String name;

    @ApiModelProperty(value = "表单设计编码")
    private String encoding;

    @ApiModelProperty(required = true)
    private int deleteFlag=2;
}

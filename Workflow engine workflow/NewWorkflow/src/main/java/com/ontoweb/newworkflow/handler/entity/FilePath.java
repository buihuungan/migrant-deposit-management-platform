package com.ontoweb.newworkflow.handler.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZhangD
 * @since 2021-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FilePath对象", description="")
public class FilePath implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String fileName;

    private String lessonId;


}

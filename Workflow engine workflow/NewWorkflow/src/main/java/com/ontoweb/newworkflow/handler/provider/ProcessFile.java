package com.ontoweb.newworkflow.handler.provider;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/4 下午5:04
 * @description：流程文件
 * @modified By：
 * @version: $
 */
@Data
@AllArgsConstructor
public class ProcessFile {
    private String name;
    private Date updateDate;
}

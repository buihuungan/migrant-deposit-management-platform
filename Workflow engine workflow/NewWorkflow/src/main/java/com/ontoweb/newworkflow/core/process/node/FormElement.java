package com.ontoweb.newworkflow.core.process.node;

import com.ontoweb.newworkflow.core.process.security.Authority;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午2:42
 * @description：表单元素
 * @modified By：
 * @version: $
 */
@Data
public class FormElement implements Serializable {
    private static final long serialVersionUID = 2304388144156396115L;
    private String name;
    private String caption;
    private String dataType;
    private String editorType;
    private Authority authority;
    private boolean required;
    private String defaultValue;
    private List<Mapping> mappings;

}

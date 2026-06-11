package com.ontoweb.newworkflow.handler.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ontoweb.newworkflow.handler.entity.FilePath;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZhangD
 * @since 2021-12-21
 */
public interface FilePathService extends IService<FilePath> {

    FilePath selectFileIfExist(String lessonId);
}

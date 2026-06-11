package com.ontoweb.newworkflow.handler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ontoweb.newworkflow.handler.entity.FilePath;
import com.ontoweb.newworkflow.handler.mapper.FilePathMapper;
import com.ontoweb.newworkflow.handler.service.FilePathService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZhangD
 * @since 2021-12-21
 */
@Service
public class FilePathServiceImpl extends ServiceImpl<FilePathMapper, FilePath> implements FilePathService {

    @Resource
    private FilePathMapper filePathMapper;

    @Override
    public FilePath selectFileIfExist(String lessonId) {


        LambdaQueryWrapper<FilePath> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FilePath::getLessonId,lessonId);
        FilePath filePath = filePathMapper.selectOne(wrapper);

        return filePath;



    }
}

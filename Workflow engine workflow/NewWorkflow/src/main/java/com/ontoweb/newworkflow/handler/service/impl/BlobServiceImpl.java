package com.ontoweb.newworkflow.handler.service.impl;

import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.handler.dao.BlobDao;
import com.ontoweb.newworkflow.handler.service.BlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlobServiceImpl implements BlobService {

    @Autowired
    BlobDao blobDao;

    @Override
    public List<Blob> getListByName(String name) {
        List<Blob> blobs = blobDao.getListByName(name);

        return blobs;
    }

    @Override
    public boolean selectIfDeploy(String name) {
        List<Blob> blobs = blobDao.getListByName(name);

        return false;
    }
}

package com.ontoweb.newworkflow.handler.service;

import com.ontoweb.newworkflow.core.model.Blob;

import java.util.List;

public interface BlobService {

    List<Blob> getListByName(String name);

    boolean selectIfDeploy(String name);
}

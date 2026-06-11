package com.ontoweb.newworkflow.margin.service;

import com.ontoweb.newworkflow.margin.entity.TextWaterMark;

import java.util.Map;

public interface PoiService {

    Boolean exportWord (Map<String,Object> mapText, Map<String,Object> mapPicture, TextWaterMark waterMarkInfo);


}

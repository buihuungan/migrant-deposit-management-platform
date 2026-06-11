package com.wust.businessback.service;


import com.wust.businessback.entity.TextWaterMark;
import java.util.Map;

public interface PoiService {

    Boolean exportWord (Map<String,Object> mapText, Map<String,Object> mapPicture, TextWaterMark waterMarkInfo);


}

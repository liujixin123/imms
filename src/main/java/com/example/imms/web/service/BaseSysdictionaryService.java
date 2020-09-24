package com.example.imms.web.service;

import com.example.imms.web.model.BaseSysdictionary;
import com.example.imms.web.response.PageDataResult;

import java.util.List;
import java.util.Map;

public interface BaseSysdictionaryService {

    PageDataResult getDicList(BaseSysdictionary dic, Integer pageNum, Integer pageSize);

    Map<String,Object> addDic(BaseSysdictionary dic);

    Map<String,Object> updateDic(BaseSysdictionary dic);

    Map<String,Object> delDicById(Integer id);

    List<BaseSysdictionary> getDics(String type);
}

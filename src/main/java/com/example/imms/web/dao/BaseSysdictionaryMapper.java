package com.example.imms.web.dao;


import com.example.imms.web.model.BaseSysdictionary;
import com.example.imms.web.model.ImmsSRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机房设备
 */
@Mapper
public interface BaseSysdictionaryMapper extends BaseMapper<BaseSysdictionary>{

    List<BaseSysdictionary> getDicList(BaseSysdictionary dic);

    BaseSysdictionary getDic(@Param("codevalue") String  codevalue, @Param("lable") String lable, @Param("columntype") String  columntype,  @Param("id") String  id);

    List<BaseSysdictionary> getDics(@Param("type") String type);

    BaseSysdictionary getBaseSysdictionary(@Param("codevalue") String  codevalue, @Param("columntype") String  columntype);


}

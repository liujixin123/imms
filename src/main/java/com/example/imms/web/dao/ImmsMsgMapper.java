package com.example.imms.web.dao;

import com.example.imms.web.model.ImmsMsg;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 库房设备
 */
@Mapper
public interface ImmsMsgMapper extends BaseMapper<ImmsMsg>{

    List<ImmsMsg> getMsgList(ImmsMsg msg);

    int deleteBath(@Param("ids") List<String> ids);

    int clearMsg();

    List<Map<String,Object>> getNews(@Param("userId") Integer userId);
}

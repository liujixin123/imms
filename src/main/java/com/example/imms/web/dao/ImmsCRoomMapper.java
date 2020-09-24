package com.example.imms.web.dao;


import com.example.imms.web.model.ImmsCRoom;
import com.example.imms.web.model.ImmsSRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机房设备
 */
@Mapper
public interface ImmsCRoomMapper extends BaseMapper<ImmsCRoom>{

    List<ImmsCRoom> getImmsCRoomList(ImmsCRoom immsCRoom);

    ImmsCRoom getImmsCRoomMapperByNumber(@Param("devNumber") String devNumber, @Param("id") Integer id);

    int deleteBath(@Param("ids") List<String> ids);

    int insertBatchs(@Param("list") List<ImmsCRoom> list);

    int updateBatchs(@Param("list") List<ImmsCRoom> list);
}

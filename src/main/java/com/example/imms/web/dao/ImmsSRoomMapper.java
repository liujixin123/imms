package com.example.imms.web.dao;

import com.example.imms.web.model.ImmsSRoom;
import com.example.imms.web.model.ImmsSRoomExcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库房设备
 */
@Mapper
public interface ImmsSRoomMapper extends BaseMapper<ImmsSRoom>{

    List<ImmsSRoom> getImmsSRoomList(ImmsSRoom immsSRoom);

    List<ImmsSRoomExcel> queryImmsSRoomExcelList(ImmsSRoom immsSRoom);

    ImmsSRoom getImmsSRoomMapperByNumber(@Param("devNumber") String devNumber, @Param("id") Integer id);

    List<ImmsSRoom> getImmsSRoomOutList(ImmsSRoom immsSRoom);

    int insertBatchs(@Param("list") List<ImmsSRoom> list);

    int updateBatchs(@Param("list") List<ImmsSRoom> list);

    int deleteBath(@Param("ids") List<String> ids);
}

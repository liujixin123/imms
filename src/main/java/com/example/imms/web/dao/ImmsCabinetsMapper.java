package com.example.imms.web.dao;


import com.example.imms.web.model.ImmsCabinets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 屏柜
 */
@Mapper
public interface ImmsCabinetsMapper extends BaseMapper<ImmsCabinets>{

    List<ImmsCabinets> getImmsCabinetsList(ImmsCabinets immsCabinets);

    ImmsCabinets getImmsCabinetsMapperByCode(@Param("devCode") String devCode, @Param("id") Integer id);

    int deleteBath(@Param("ids") List<String> ids);

    int insertBatchs(@Param("list") List<ImmsCabinets> list);

    int updateBatchs(@Param("list") List<ImmsCabinets> list);

    List<ImmsCabinets> getImmsRoomMapper(ImmsCabinets immsCabinets);
}

package com.example.imms.web.dao;


import com.example.imms.web.model.ImmsServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 屏柜
 */
@Mapper
public interface ImmsServerMapper extends BaseMapper<ImmsServer>{

    List<ImmsServer> getImmsServerList(ImmsServer immsServer);

    int deleteBath(@Param("ids") List<String> ids);

    int insertBatchs(@Param("list") List<ImmsServer> list);

    int updateBatchs(@Param("list") List<ImmsServer> list);

    List<ImmsServer> getImmsRoomMapper(ImmsServer immsServer);
}

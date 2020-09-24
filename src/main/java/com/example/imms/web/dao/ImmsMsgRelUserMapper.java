package com.example.imms.web.dao;

import com.example.imms.web.model.ImmsMsgRelUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;



/**
 * 库房设备
 */
@Mapper
public interface ImmsMsgRelUserMapper extends BaseMapper<ImmsMsgRelUser>{


    int insertBath(@Param("immsMsgRelUsers") List<ImmsMsgRelUser> immsMsgRelUsers);

    int deleteByMsg(@Param("msgId")Integer msgId,@Param("userId") Integer userId);

    int deleteBathByMsg(@Param("ids")List<String> ids, @Param("userId")Integer userId);

    void isRead(@Param("id")Integer id);
}

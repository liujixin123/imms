package com.example.imms.web.dao;

import com.example.imms.web.model.ImmsMsg;
import com.example.imms.web.model.ImmsMsgUser;
import com.example.imms.web.model.ImmsSRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 库房设备
 */
@Mapper
public interface ImmsMsgUserMapper extends BaseMapper<ImmsMsgUser>{

    List<ImmsMsgUser> getSignMsgList(ImmsMsgUser immsMsgUser);

    int insertBath(@Param("immsMsgUsers") List<ImmsMsgUser> immsMsgUsers);

    List<ImmsMsgUser>  getMsgByModule(@Param("module")String module, @Param("userId")Integer userId);
}

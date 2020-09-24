package com.example.imms.web.dao;


import com.example.imms.web.dto.AdminRoleDTO;
import com.example.imms.web.dto.AdminUserDTO;
import com.example.imms.web.dto.UserSearchDTO;

import com.example.imms.web.model.BaseAdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface BaseAdminUserMapper    extends BaseMapper<UserSearchDTO>{

    List<AdminUserDTO> getUserList(UserSearchDTO userSearchDTO);

    BaseAdminUser getUserByUserName(@Param("sysUserName") String sysUserName, @Param("id") Integer id);

    int updateUserStatus(@Param("id") Integer id, @Param("status") Integer status);

    int updateUser(BaseAdminUser user);

    BaseAdminUser findByUserName(@Param("userName") String userName);

    int updatePwd(@Param("userName") String userName, @Param("password") String password);

    BaseAdminUser selectByPrimaryKey(Integer id);

    List<AdminUserDTO> getUserMsgSignList(UserSearchDTO userSearchDTO);

    int insertUser(BaseAdminUser user);
}
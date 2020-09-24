package com.example.imms.web.dao;


import com.example.imms.web.dto.PermissionDTO;

import com.example.imms.web.model.BaseAdminPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface BaseAdminPermissionMapper   extends BaseMapper<PermissionDTO> {
    List<PermissionDTO> getPermissionList();

    List<PermissionDTO> parentPermissionList();

    int updatePermission(BaseAdminPermission permission);

    List<PermissionDTO> getPermissionListByPId(@Param("pid") Integer pid);

    BaseAdminPermission selectByPrimaryKey(Integer id);

    int deleteByPrimaryKey(Integer id);

    int insertPermission(BaseAdminPermission permission);
}
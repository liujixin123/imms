package com.example.imms.web.dao;

 import com.example.imms.web.dto.AdminRoleDTO;
 import com.example.imms.web.model.BaseAdminRole;
 import org.apache.ibatis.annotations.Mapper;
 import org.apache.ibatis.annotations.Param;

 import java.util.List;


@Mapper
public interface BaseAdminRoleMapper     extends BaseMapper<AdminRoleDTO>{

    List<BaseAdminRole> getRoleList();

    List<BaseAdminRole> getRoles();

    int updateRole(BaseAdminRole role);

    int updateRoleStatus(@Param("id") Integer id, @Param("roleStatus") Integer roleStatus);

    BaseAdminRole selectByPrimaryKey(Integer roleId);

    int insertRole(BaseAdminRole role);
}
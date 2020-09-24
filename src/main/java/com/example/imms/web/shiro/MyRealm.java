package com.example.imms.web.shiro;


import com.example.imms.web.model.BaseAdminPermission;
import com.example.imms.web.model.BaseAdminRole;
import com.example.imms.web.model.BaseAdminUser;
import com.example.imms.web.service.AdminPermissionService;
import com.example.imms.web.service.AdminRoleService;
import com.example.imms.web.service.AdminUserService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: MyRealm
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/22 15:53
 */
public class MyRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private AdminUserService userService;

    @Autowired
    private AdminRoleService roleService;

    @Autowired
    private AdminPermissionService permissionService;


    /**
     *
     * 功能描述: 授权
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/9/11 10:30
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        //授权
        logger.info("授予角色和权限");
        // 添加权限 和 角色信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当前登陆用户
        Subject subject = SecurityUtils.getSubject();
        BaseAdminUser user = (BaseAdminUser) subject.getPrincipal();
        System.out.println(user);
        if (user.getSysUserName().equals("admin")) {
            // 超级管理员，添加所有角色、添加所有权限
            authorizationInfo.addRole("*");
            authorizationInfo.addStringPermission("*");
            SecurityUtils.getSubject().getSession().setAttribute("roleCode","admin");
        } else if (user.getSysUserName().equals("sysadmin")) {
            // 超级管理员，添加所有角色、添加所有权限
            authorizationInfo.addRole("*");
            authorizationInfo.addStringPermission("*");
            SecurityUtils.getSubject().getSession().setAttribute("roleCode","sysadmin");
        } else {
            // 普通用户，查询用户的角色，根据角色查询权限
            Integer roleId = user.getRoleId();
            BaseAdminRole role = roleService.findRoleById(roleId);
            if (null != role ) {
                String permissions = role.getPermissions();
                String[] ids = permissions.split(",");
                for (String id : ids) {
                    authorizationInfo.addRole(role.getRoleName());
                    // 角色对应的权限数据
                    BaseAdminPermission perm = permissionService.getById(id);
                    if (null != perm ) {
                        // 授权角色下所有权限
                        authorizationInfo.addStringPermission(perm.getUrl());
                    }
                }
            }
            List<String> roles = new ArrayList<>();
            roles.add(role.getRoleCode());
            authorizationInfo.addRoles(roles);
            SecurityUtils.getSubject().getSession().setAttribute("roleCode",role.getRoleCode());
        }
        return authorizationInfo;
    }


    /**
     *
     * 功能描述: 认证
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/9/11 10:32
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken用于存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("用户登录认证：验证当前Subject时获取到token为：" + ReflectionToStringBuilder
                .toString(token, ToStringStyle.MULTI_LINE_STYLE));
        String username = token.getUsername();
        // 调用数据层
        BaseAdminUser sysUser = userService.findByUserName(username);
        logger.debug("用户登录认证！用户信息user：" + sysUser);
        if (sysUser == null) {
            // 用户不存在
            return null;
        }
        // 返回密码
        return new SimpleAuthenticationInfo(sysUser, sysUser.getSysUserPwd(), ByteSource.Util.bytes(username), getName());

    }

}

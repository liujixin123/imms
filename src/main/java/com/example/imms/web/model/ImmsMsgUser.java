package com.example.imms.web.model;

import java.io.Serializable;

public class ImmsMsgUser implements Serializable {

    private Integer id;
    private Integer userId;
    private String userName;
    private String moduleCode;
    private String moduleName;
    private String lv;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    @Override
    public String toString() {
        return "ImmsMsgUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName=" + userName +
                ", moduleCode='" + moduleCode + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", lv='" + lv + '\'' +
                '}';
    }
}
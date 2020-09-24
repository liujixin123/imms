package com.example.imms.web.model;

import java.io.Serializable;
import java.util.Date;

public class ImmsMsg implements Serializable {

    private Integer id;
    private String actionUserName;
    private String moduleCode;
    private String moduleName;
    private String url;
    private String actions;
    private Integer sid;
    private String sname;
    private String title;
    private Date oncreate;
    private String isRead;
    private String userId;

    private String version;
    private Integer rid;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActionUserName() {
        return actionUserName;
    }

    public void setActionUserName(String actionUserName) {
        this.actionUserName = actionUserName;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getOncreate() {
        return oncreate;
    }

    public void setOncreate(Date oncreate) {
        this.oncreate = oncreate;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ImmsMsg{" +
                "id=" + id +
                ", actionUserName='" + actionUserName + '\'' +
                ", moduleCode='" + moduleCode + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", url='" + url + '\'' +
                ", actions='" + actions + '\'' +
                ", sid=" + sid +
                ", sname='" + sname + '\'' +
                ", title='" + title + '\'' +
                ", oncreate=" + oncreate +
                ", isRead='" + isRead + '\'' +
                ", userId='" + userId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

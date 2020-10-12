package com.example.imms.web.model;


import java.io.Serializable;

/**
 * 屏柜
 */
public class ImmsCabinets implements Serializable {

    private Integer id;

    /** 屏柜名称 */
    private String cabinetName;

    /** 编码 */
    private String devCode;

    /** 高度 */
    private String devHeight;

    /** 责任人 */
    private String responsibilityMan;

    /** 类型 */
    private String devType;

    /** 所属机房 */
    private String roomId;
    private String roomName;

    /** 位置 */
    private String location;
    private String locationX;
    private String locationY;

    /** 所属分区 */
    private String belongsPartition;

    /** 备注 */
    private String remarks;

    /** 描述 */
    private String devDescribe;

    /** 创建时间 */
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getDevHeight() {
        return devHeight;
    }

    public void setDevHeight(String devHeight) {
        this.devHeight = devHeight;
    }

    public String getResponsibilityMan() {
        return responsibilityMan;
    }

    public void setResponsibilityMan(String responsibilityMan) {
        this.responsibilityMan = responsibilityMan;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBelongsPartition() {
        return belongsPartition;
    }

    public void setBelongsPartition(String belongsPartition) {
        this.belongsPartition = belongsPartition;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDevDescribe() {
        return devDescribe;
    }

    public void setDevDescribe(String devDescribe) {
        this.devDescribe = devDescribe;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "ImmsCabinets{" +
                "id=" + id +
                ", cabinetName='" + cabinetName + '\'' +
                ", devCode='" + devCode + '\'' +
                ", devHeight='" + devHeight + '\'' +
                ", responsibilityMan='" + responsibilityMan + '\'' +
                ", devType='" + devType + '\'' +
                ", roomId='" + roomId + '\'' +
                ", location='" + location + '\'' +
                ", locationX='" + locationX + '\'' +
                ", locationY='" + locationY + '\'' +
                ", belongsPartition='" + belongsPartition + '\'' +
                ", remarks='" + remarks + '\'' +
                ", devDescribe='" + devDescribe + '\'' +
                ", roomName='" + roomName + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
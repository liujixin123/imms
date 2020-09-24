package com.example.imms.web.model;


import java.io.Serializable;

/**
 * 机房设备
 */
public class ImmsCRoom implements Serializable {

    private Integer id;

    /** 机柜名称 */
    private String cabinetName;

    /** 设备高度 */
    private String devHeight;

    /** 设备名称 */
    private String devName;

    /** 制造商 */
    private String devManu;

    /** 设备型号 */
    private String devModel;

    /** 序列号 */
    private String devNumber;

    /** 所属系统 */
    private String system;

    /** 安全区 */
    private String safeArea;

    /** 运维等级 */
    private String maintainLevel;

    /** 投运日期 */
    private String useTime;

    /** 责任处室 */
    private String responsibilityDpt;

    /** 责任人 */
    private String responsibilityMan;

    /** 运维厂商 */
    private String maintainManu;

    /** 运维人员 */
    private String maintainMan;

    /** 联系电话 */
    private String maintainManPhone;

    /** IP */
    private String ip;

    /** room */
    private String room;

    private String startTime;

    private String endTime;

    private String remarks;
    private Integer sid;
    private String version;

    private String two;

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

    public String getDevHeight() {
        return devHeight;
    }

    public void setDevHeight(String devHeight) {
        this.devHeight = devHeight;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevManu() {
        return devManu;
    }

    public void setDevManu(String devManu) {
        this.devManu = devManu;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public String getDevNumber() {
        return devNumber;
    }

    public void setDevNumber(String devNumber) {
        this.devNumber = devNumber;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSafeArea() {
        return safeArea;
    }

    public void setSafeArea(String safeArea) {
        this.safeArea = safeArea;
    }

    public String getMaintainLevel() {
        return maintainLevel;
    }

    public void setMaintainLevel(String maintainLevel) {
        this.maintainLevel = maintainLevel;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getResponsibilityDpt() {
        return responsibilityDpt;
    }

    public void setResponsibilityDpt(String responsibilityDpt) {
        this.responsibilityDpt = responsibilityDpt;
    }

    public String getResponsibilityMan() {
        return responsibilityMan;
    }

    public void setResponsibilityMan(String responsibilityMan) {
        this.responsibilityMan = responsibilityMan;
    }

    public String getMaintainManu() {
        return maintainManu;
    }

    public void setMaintainManu(String maintainManu) {
        this.maintainManu = maintainManu;
    }

    public String getMaintainMan() {
        return maintainMan;
    }

    public void setMaintainMan(String maintainMan) {
        this.maintainMan = maintainMan;
    }

    public String getMaintainManPhone() {
        return maintainManPhone;
    }

    public void setMaintainManPhone(String maintainManPhone) {
        this.maintainManPhone = maintainManPhone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    @Override
    public String toString() {
        return "ImmsCRoom{" +
                "id=" + id +
                ", cabinetName='" + cabinetName + '\'' +
                ", devHeight='" + devHeight + '\'' +
                ", devName='" + devName + '\'' +
                ", devManu='" + devManu + '\'' +
                ", devModel='" + devModel + '\'' +
                ", devNumber='" + devNumber + '\'' +
                ", system='" + system + '\'' +
                ", safeArea='" + safeArea + '\'' +
                ", maintainLevel='" + maintainLevel + '\'' +
                ", useTime='" + useTime + '\'' +
                ", responsibilityDpt='" + responsibilityDpt + '\'' +
                ", responsibilityMan='" + responsibilityMan + '\'' +
                ", maintainManu='" + maintainManu + '\'' +
                ", maintainMan='" + maintainMan + '\'' +
                ", maintainManPhone='" + maintainManPhone + '\'' +
                ", ip='" + ip + '\'' +
                ", room='" + room + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", remarks='" + remarks + '\'' +
                ", sid=" + sid +
                ", version='" + version + '\'' +
                ", two='" + two + '\'' +
                '}';
    }
}
package com.example.imms.web.model;


import java.io.Serializable;

/**
 * 库房设备
 */
public class ImmsSRoom implements Serializable {

    private Integer id;

    /** 设备类型 */
    private String devType;

    /** 制造商 */
    private String devManu;

    /** 设备型号 */
    private String devModel;

    private String devNumber;

    /** 入库时间 */
    private String inRoomTime;

    /** 类别 */
    private String useType;

    /** 接收人 */
    private String receiver;

    /** 存放地点 */
    private String devAddress;

    /** 责任处室 */
    private String responsibilityDpt;

    /** 责任人 */
    private String responsibilityMan;

    /** 所属项目 */
    private String project;

    /** 设备状态 */
    private String devStatus;

    /** 出库时间 */
    private String outRoomTime;

    /** 出货人 */
    private String outRoomUser;

    private String devGo;

    /** 备注 */
    private String remarks;

    private String startTime;

    private String endTime;
    private Integer sid;
    private String version;
    private String two;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
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

    public String getInRoomTime() {
        return inRoomTime;
    }

    public void setInRoomTime(String inRoomTime) {
        this.inRoomTime = inRoomTime;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDevAddress() {
        return devAddress;
    }

    public void setDevAddress(String devAddress) {
        this.devAddress = devAddress;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getOutRoomTime() {
        return outRoomTime;
    }

    public void setOutRoomTime(String outRoomTime) {
        this.outRoomTime = outRoomTime;
    }

    public String getOutRoomUser() {
        return outRoomUser;
    }

    public void setOutRoomUser(String outRoomUser) {
        this.outRoomUser = outRoomUser;
    }

    public String getDevGo() {
        return devGo;
    }

    public void setDevGo(String devGo) {
        this.devGo = devGo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        return "ImmsSRoom{" +
                "id=" + id +
                ", devType='" + devType + '\'' +
                ", devManu='" + devManu + '\'' +
                ", devModel='" + devModel + '\'' +
                ", devNumber='" + devNumber + '\'' +
                ", inRoomTime='" + inRoomTime + '\'' +
                ", useType='" + useType + '\'' +
                ", receiver='" + receiver + '\'' +
                ", devAddress='" + devAddress + '\'' +
                ", responsibilityDpt='" + responsibilityDpt + '\'' +
                ", responsibilityMan='" + responsibilityMan + '\'' +
                ", project='" + project + '\'' +
                ", devStatus='" + devStatus + '\'' +
                ", outRoomTime='" + outRoomTime + '\'' +
                ", outRoomUser='" + outRoomUser + '\'' +
                ", devGo='" + devGo + '\'' +
                ", remarks='" + remarks + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", sid=" + sid +
                ", version='" + version + '\'' +
                ", two='" + two + '\'' +
                '}';
    }
}
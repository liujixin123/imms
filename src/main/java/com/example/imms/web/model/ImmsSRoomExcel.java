package com.example.imms.web.model;


import com.example.imms.web.service.ExcelColumn;
import lombok.Data;

import java.io.Serializable;

/**
 * 库房设备
 */
@Data
public class ImmsSRoomExcel implements Serializable {

    /** 设备类型 */
    @ExcelColumn(value = "device_type", col = 1)
    private String devType;

    /** 制造商 */
    @ExcelColumn(value = "manufacturer", col = 2)
    private String devManu;

    /** 设备型号 */
    @ExcelColumn(value = "device_model", col = 3)
    private String devModel;

    @ExcelColumn(value = "serial_number", col = 4)
    private String devNumber;

    /** 入库时间 */
    @ExcelColumn(value = "storage_time", col = 5)
    private String inRoomTime;

    /** 类别 */
    @ExcelColumn(value = "use_category", col = 6)
    private String useType;

    /** 接收人 */
    @ExcelColumn(value = "receiver", col = 7)
    private String Receiver;

    /** 存放地点 */
    @ExcelColumn(value = "storage_location", col = 8)
    private String devAddress;

    /** 责任处室 */
    @ExcelColumn(value = "responsibility_office", col = 9)
    private String responsibilityDpt;

    /** 责任人 */
    @ExcelColumn(value = "responsible", col = 10)
    private String responsibilityMan;

    /** 所属项目 */
    @ExcelColumn(value = "project", col = 11)
    private String project;

    /** 二维码信息 */
    @ExcelColumn(value = "two", col = 12)
    private String two;

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
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
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

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    @Override
    public String toString() {
        return "ImmsSRoomExcel{" +
                "devType='" + devType + '\'' +
                ", devManu='" + devManu + '\'' +
                ", devModel='" + devModel + '\'' +
                ", devNumber='" + devNumber + '\'' +
                ", inRoomTime='" + inRoomTime + '\'' +
                ", useType='" + useType + '\'' +
                ", Receiver='" + Receiver + '\'' +
                ", devAddress='" + devAddress + '\'' +
                ", responsibilityDpt='" + responsibilityDpt + '\'' +
                ", responsibilityMan='" + responsibilityMan + '\'' +
                ", project='" + project + '\'' +
                ", two='" + two + '\'' +
                '}';
    }
}
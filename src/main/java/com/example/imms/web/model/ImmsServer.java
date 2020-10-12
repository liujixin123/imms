package com.example.imms.web.model;


import java.io.Serializable;

/**
 * 服务器
 */
public class ImmsServer implements Serializable {

    private Integer id;

    /** 电源额定功率 */
    private String powerRate;

    /** 电源数量 */
    private String powerNum;

    /** 操作系统类型 */
    private String systemType;

    /** 操作系统版本 */
    private String systemVersion;

    /** CPU数量 */
    private String cpuNum;

    /** CPU型号 */
    private String cpuXh;

    /** CPU主频 */
    private String cpuZp;
    /** 单CPU核数 */
    private String cpuHs;
    /** 内存条数量 */
    private String romNum;

    /** 内存大小 */
    private String romSize;

    /** 硬盘类型 */
    private String diskType;

    /** 硬盘数量 */
    private String diskNum;
    /** 硬盘容量 */
    private String diskSize;
    /** HBA卡数 */
    private String hbaNum;
    /** 网卡数 */
    private String netcardNum;
    /** RAID方式 */
    private String raidDetail;
    /** 外接存储 */
    private String externalRom;
    /** 双机版本 */
    private String doubleVersion;

    /** 光口数 */
    private String gks;

    /** 用户名/口令 */
    private String serverPwd;

    /** 所属机房 */
    private String roomName;
    private String roomId;
    private String remarks;


    /** 创建时间 */
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPowerRate() {
        return powerRate;
    }

    public void setPowerRate(String powerRate) {
        this.powerRate = powerRate;
    }

    public String getPowerNum() {
        return powerNum;
    }

    public void setPowerNum(String powerNum) {
        this.powerNum = powerNum;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(String cpuNum) {
        this.cpuNum = cpuNum;
    }

    public String getCpuXh() {
        return cpuXh;
    }

    public void setCpuXh(String cpuXh) {
        this.cpuXh = cpuXh;
    }

    public String getCpuZp() {
        return cpuZp;
    }

    public void setCpuZp(String cpuZp) {
        this.cpuZp = cpuZp;
    }

    public String getCpuHs() {
        return cpuHs;
    }

    public void setCpuHs(String cpuHs) {
        this.cpuHs = cpuHs;
    }

    public String getRomNum() {
        return romNum;
    }

    public void setRomNum(String romNum) {
        this.romNum = romNum;
    }

    public String getRomSize() {
        return romSize;
    }

    public void setRomSize(String romSize) {
        this.romSize = romSize;
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
    }

    public String getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(String diskNum) {
        this.diskNum = diskNum;
    }

    public String getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(String diskSize) {
        this.diskSize = diskSize;
    }

    public String getHbaNum() {
        return hbaNum;
    }

    public void setHbaNum(String hbaNum) {
        this.hbaNum = hbaNum;
    }

    public String getNetcardNum() {
        return netcardNum;
    }

    public void setNetcardNum(String netcardNum) {
        this.netcardNum = netcardNum;
    }

    public String getRaidDetail() {
        return raidDetail;
    }

    public void setRaidDetail(String raidDetail) {
        this.raidDetail = raidDetail;
    }

    public String getExternalRom() {
        return externalRom;
    }

    public void setExternalRom(String externalRom) {
        this.externalRom = externalRom;
    }

    public String getDoubleVersion() {
        return doubleVersion;
    }

    public void setDoubleVersion(String doubleVersion) {
        this.doubleVersion = doubleVersion;
    }

    public String getGks() {
        return gks;
    }

    public void setGks(String gks) {
        this.gks = gks;
    }

    public String getServerPwd() {
        return serverPwd;
    }

    public void setServerPwd(String serverPwd) {
        this.serverPwd = serverPwd;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
                ", powerRate='" + powerRate + '\'' +
                ", powerNum='" + powerNum + '\'' +
                ", systemType='" + systemType + '\'' +
                ", systemVersion='" + systemVersion + '\'' +
                ", cpuNum='" + cpuNum + '\'' +
                ", roomId='" + roomId + '\'' +
                ", cpuXh='" + cpuXh + '\'' +
                ", cpuZp='" + cpuZp + '\'' +
                ", cpuHs='" + cpuHs + '\'' +
                ", romNum='" + romNum + '\'' +
                ", romSize='" + romSize + '\'' +
                ", diskType='" + diskType + '\'' +
                ", diskNum='" + diskNum + '\'' +
                ", diskSize='" + diskSize + '\'' +
                ", hbaNum='" + hbaNum + '\'' +
                ", netcardNum='" + netcardNum + '\'' +
                ", raidDetail='" + raidDetail + '\'' +
                ", externalRom='" + externalRom + '\'' +
                ", doubleVersion='" + doubleVersion + '\'' +
                ", gks='" + gks + '\'' +
                ", serverPwd='" + serverPwd + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
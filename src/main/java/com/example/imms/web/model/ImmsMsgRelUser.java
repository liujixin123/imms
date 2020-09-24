package com.example.imms.web.model;

import java.io.Serializable;

public class ImmsMsgRelUser implements Serializable {

    private Integer id;
    private Integer msgId;
    private Integer userId;
    private String isRead;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "ImmsMsgRelUser{" +
                "id=" + id +
                ", msgId=" + msgId +
                ", userId=" + userId +
                ", isRead='" + isRead + '\'' +
                '}';
    }
}
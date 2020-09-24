package com.example.imms.web.service;

import com.example.imms.web.model.ImmsMsg;
import com.example.imms.web.model.ImmsMsgUser;
import com.example.imms.web.response.PageDataResult;

import java.util.List;
import java.util.Map;

public interface ImmsMsgService {

    PageDataResult getSignMsgList(ImmsMsgUser immsMsgUser, Integer pageNum, Integer pageSize);

    PageDataResult getMsgList(ImmsMsg msg, Integer pageNum, Integer pageSize);

    Map<String,Object> addMsg(String moduleCode, String moduleName, String lv, String ids, String names);

    Map<String,Object> msgUserDelById(Integer id);

    Map<String,Object> msgDelById(Integer id);

    Map<String,Object> deleteMsgBatch(String ids);

    void isRead(Integer rid);

    List<Map<String,Object>> getNews();

    void sendMsg(Map<String,String> map);
}

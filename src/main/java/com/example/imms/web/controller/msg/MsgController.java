package com.example.imms.web.controller.msg;

import com.example.imms.web.model.BaseSysdictionary;
import com.example.imms.web.model.ImmsMsg;
import com.example.imms.web.model.ImmsMsgUser;
import com.example.imms.web.model.ImmsSRoom;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.ImmsMsgService;
import com.example.imms.web.service.ImmsSRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("msg")
public class MsgController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImmsMsgService immsMsgService;

    @RequestMapping("/msgSign")
    public String msgSign() {
        return "msg/msgSign";
    }


    @RequestMapping("/msgManage")
    public String msgManage() {
        return "msg/msgManage";
    }

    @RequestMapping(value = "/getSignMsgList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getSignMsgList(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ ImmsMsgUser immsMsgUser) {
        /*logger.info("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",pageNum:" + page.getPageNum()
                + ",每页记录数量pageSize:" + page.getPageSize());*/
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = immsMsgService.getSignMsgList(immsMsgUser, pageNum, pageSize);
            logger.info("消息订阅用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("消息订阅用户列表查询异常！", e);
        }
        return pdr;
    }

    @RequestMapping(value = "/getMsgList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getMsgList(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ ImmsMsg msg) {
        /*logger.info("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",pageNum:" + page.getPageNum()
                + ",每页记录数量pageSize:" + page.getPageSize());*/
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = immsMsgService.getMsgList(msg, pageNum, pageSize);
            logger.info("消息邮件列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("消息邮件列表查询异常！", e);
        }
        return pdr;
    }

    @RequestMapping(value = "/news", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> news(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize)
    {
        Map<String, Object> data = new HashMap<>();
         //简单数据库中新闻
        List<Map<String ,Object>> newsList = immsMsgService.getNews();
        //总页数
        int pages =(int) Math.ceil((double)55 / pageSize);
        data.put("total",pages);
        data.put("rows",newsList);
        return data;
    }

    @RequestMapping(value = "/addMsg", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addMsg( String  moduleCode, String  moduleName,
                                String lv,
                                    String ids, String names) {
        logger.info("===>>>设置字段数据[新增或更新]！moduleCode:" + moduleCode+",moduleName="+moduleName+",lv="+lv+",ids="+ids+",names="+names);
        Map<String,Object> data =   immsMsgService.addMsg(moduleCode,moduleName,lv,ids,names);
        return data;
    }

    @RequestMapping(value = "/msgUserDel", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> msgUserDel(@RequestParam("id") Integer id) {
        logger.info("删除订阅！id:" + id );
        return immsMsgService.msgUserDelById(id);
    }

    @RequestMapping(value = "/msgDel", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> msgDel(@RequestParam("id") Integer id) {
        logger.info("删除消息！id:" + id );
        return immsMsgService.msgDelById(id);
    }


    @RequestMapping(value = "/deleteMsgBatch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteMsgBatch(  String ids ) {
        logger.info("===>>>批量删除消息！ids:" +ids);
        Map<String,Object> data =   immsMsgService.deleteMsgBatch(ids);
        return data;
    }

    @RequestMapping(value = "/isRead", method = RequestMethod.POST)
    @ResponseBody
    public void isRead( @RequestParam("rid")  Integer rid ) {
        immsMsgService.isRead(rid);

    }

}

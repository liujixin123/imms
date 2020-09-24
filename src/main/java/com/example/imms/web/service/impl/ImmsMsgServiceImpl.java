package com.example.imms.web.service.impl;

import com.example.imms.web.common.utils.ConstantUtil;
import com.example.imms.web.dao.BaseSysdictionaryMapper;
import com.example.imms.web.dao.ImmsMsgMapper;
import com.example.imms.web.dao.ImmsMsgRelUserMapper;
import com.example.imms.web.dao.ImmsMsgUserMapper;
import com.example.imms.web.model.*;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.ImmsMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImmsMsgServiceImpl implements ImmsMsgService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsMsgUserMapper immsMsgUserMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsMsgMapper immsMsgMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsMsgRelUserMapper immsMsgRelUserMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BaseSysdictionaryMapper baseSysdictionarymMapper;

    @Override
    public PageDataResult getSignMsgList(ImmsMsgUser immsMsgUser, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageDataResult pageDataResult = new PageDataResult();
        List<ImmsMsgUser> immsMsgUsers = immsMsgUserMapper.getSignMsgList(immsMsgUser);

        if(immsMsgUsers.size() != 0){
            PageInfo<ImmsMsgUser> pageInfo = new PageInfo<>(immsMsgUsers);
            pageDataResult.setList(immsMsgUsers);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public PageDataResult getMsgList(ImmsMsg msg, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        PageDataResult pageDataResult = new PageDataResult();
        BaseAdminUser user = (BaseAdminUser) SecurityUtils.getSubject().getPrincipal(); // 获取当前登录用户
        msg.setUserId( user.getId().toString());
        List<ImmsMsg> msgs = immsMsgMapper.getMsgList(msg);

        if(msgs.size() != 0){
            PageInfo<ImmsMsg> pageInfo = new PageInfo<>(msgs);
            pageDataResult.setList(msgs);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public Map<String, Object> addMsg(String moduleCode, String moduleName, String lv, String ids, String names) {
        Map<String,Object> data = new HashMap();
        try {

            List<ImmsMsgUser> immsMsgUsers = new ArrayList<>();
            String[] idsAry = ids.split(",");
            String[] namesAry = names.split(",");
            ImmsMsgUser immsMsgUser= null;
            for(int i=0;i< idsAry.length;i++){
                immsMsgUser = new ImmsMsgUser();
                immsMsgUser.setUserId(Integer.valueOf(idsAry[i]));
                immsMsgUser.setUserName(namesAry[i]);
                immsMsgUser.setModuleCode(moduleCode);
                immsMsgUser.setModuleName(moduleName);
                immsMsgUser.setLv(lv);
                immsMsgUsers.add(immsMsgUser);
            }
            int result = immsMsgUserMapper.insertBath(immsMsgUsers);
            if(result == 0){
                data.put("code",0);
                data.put("msg","新增失败！");
                logger.error("订阅消息[新增]，结果=新增失败！");
                return data;
            }
            data.put("code",1);
            data.put("msg","新增成功！");
            logger.info("订阅消息[新增]，结果=新增成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("订阅消息[新增]异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public Map<String, Object> msgUserDelById(Integer id) {
        Map<String, Object> data = new HashMap<>();
        try {
            int result =  immsMsgUserMapper.delete(id);
            if(result == 0){
                data.put("code",0);
                data.put("msg","删除订阅失败");
                logger.error("删除订阅失败");
                return data;
            }
            data.put("code",1);
            data.put("msg","删除订阅成功");
            logger.info("删除订阅成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除字典数据异常！", e);
        }
        return data;
    }

    @Override
    public Map<String, Object> msgDelById(Integer id) {
        Map<String, Object> data = new HashMap<>();
        try {
            /*int result =  immsMsgMapper.delete(id);
            if(result == 0){
                data.put("code",0);
                data.put("msg","删除消息失败");
                logger.error("删除消息失败");
                return data;
            }*/
            BaseAdminUser user = (BaseAdminUser) SecurityUtils.getSubject().getPrincipal(); // 获取当前登录用户
            int result2 =  immsMsgRelUserMapper.deleteByMsg(id,user.getId());
            if(result2 == 0){
                data.put("code",0);
                data.put("msg","删除消息失败");
                logger.error("删除消息失败");
                return data;
            }
            immsMsgMapper.clearMsg();
            data.put("code",1);
            data.put("msg","删除消息成功");
            logger.info("删除消息成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除消息异常！", e);
        }
        return data;
    }

    @Override
    public Map<String, Object> deleteMsgBatch(String ids) {
        Map<String,Object> data = new HashMap();
        try {
            BaseAdminUser user = (BaseAdminUser) SecurityUtils.getSubject().getPrincipal(); // 获取当前登录用户
            List<String> list = new ArrayList<>();
            String[] idsAry = ids.split(",");
            for(int i=0;i< idsAry.length;i++){
                list.add(idsAry[i]);
            }
       /*     int result = immsMsgMapper.deleteBath(list);
            if(result == 0){
                data.put("code",0);
                data.put("msg","批量删除消息失败！");
                logger.error("批量删除消息 ，结果= 失败！");
                return data;
            }*/

            int result2 = immsMsgRelUserMapper.deleteBathByMsg(list,user.getId());
            if(result2 == 0){
                data.put("code",0);
                data.put("msg","批量删除消息失败！");
                logger.error("批量删除消息 ，结果= 失败！");
                return data;
            }

             immsMsgMapper.clearMsg();

            data.put("code",1);
            data.put("msg","批量删除消息成功！");
            logger.info("批量删除消息 ，结果= 成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("批量删除消息异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public void isRead(Integer id) {
        immsMsgRelUserMapper.isRead(id);
    }

    @Override
    public List<Map<String, Object>> getNews() {
        BaseAdminUser user = (BaseAdminUser) SecurityUtils.getSubject().getPrincipal(); // 获取当前登录用户
         List<Map<String, Object>> msgs = immsMsgMapper.getNews(user.getId() );

        return msgs;
    }

    /**
     * 发送消息
     * @param module 模块
     * @param goUrl  模块跳转地址
     * @param action 动作
     * @param id     主键
     * @param number 标识
     */
    @Override
    public void sendMsg( Map<String,String> map) {
        String module = map.get("module");
         String goUrl = map.get("goUrl");
        String action = map.get("action");
        String id = map.get("id");
        String version = map.get("version");
        String number = map.get("number");

        BaseAdminUser user = (BaseAdminUser) SecurityUtils.getSubject().getPrincipal(); // 获取当前登录用户
        // 消息入库
        List<ImmsMsgUser>  sign =  immsMsgUserMapper.getMsgByModule(module, user.getId());
        BaseSysdictionary dic = baseSysdictionarymMapper.getBaseSysdictionary(module, ConstantUtil.MSG);
        List<ImmsMsgRelUser>  list = null;
        ImmsMsg msg = null;
        if(sign !=null && sign.size() > 0){//分发消息
            list = new ArrayList<>();
            msg = new ImmsMsg();
            msg.setActionUserName(user.getSysUserName());
            msg.setModuleCode(module);
            msg.setModuleName(dic.getLable());
            msg.setUrl(goUrl);
            msg.setActions(action);
            if(id!=null && !"".equals(id)){
                msg.setSid(Integer.valueOf(id));
            }
            msg.setSname(number);
            msg.setTitle(user.getSysUserName()+action+dic.getLable()+", 序列(名称):"+number);
            msg.setVersion(version);
            msg.setOncreate(new Date());
            immsMsgMapper.insert(msg);
            ImmsMsgRelUser immsMsgRelUser = null;
            for(ImmsMsgUser immsMsgUser :sign){
                immsMsgRelUser = new ImmsMsgRelUser();
                immsMsgRelUser.setMsgId(msg.getId());
                immsMsgRelUser.setUserId(immsMsgUser.getUserId());
                immsMsgRelUser.setIsRead(ConstantUtil.MSG_IS_NOT_READ);
                list.add(immsMsgRelUser);
            }
            immsMsgRelUserMapper.insertBath(list);
        }
    }

}

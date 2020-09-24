package com.example.imms.web.controller.base;

  import com.example.imms.web.model.BaseSysdictionary;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.BaseSysdictionaryService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
  import org.springframework.ui.Model;
  import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: UserController
 * @Description: 系统用户管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Controller
@RequestMapping("dic")
public class BaseSysdictionaryController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BaseSysdictionaryService baseSysdictionaryService;


    @RequestMapping("/dicManage")
    public String userManage(Model model) {
        Object roleCode=  SecurityUtils.getSubject().getSession().getAttribute("roleCode");
        model.addAttribute("roleCode",  roleCode==null?"":(String)roleCode);
        return "base/dicManage";
     }

    /**
     *
     * 功能描述: 分页查询用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/getDicList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getDicList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ BaseSysdictionary dic) {
        /*logger.info("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",pageNum:" + page.getPageNum()
                + ",每页记录数量pageSize:" + page.getPageSize());*/
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = baseSysdictionaryService.getDicList(dic, pageNum ,pageSize);
            logger.info("字典列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("字典列表查询异常！", e);
        }
        return pdr;
    }

    @RequestMapping(value = "/setDic", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setDic(BaseSysdictionary dic) {
        logger.info("设置字段数据[新增或更新]！dic:" + dic);
        Map<String,Object> data = new HashMap();
        if(dic.getId() == null || "".equals(dic.getId())){
            data = baseSysdictionaryService.addDic(dic);
        }else{
            data = baseSysdictionaryService.updateDic(dic);
        }
        return data;
    }

    @RequestMapping(value = "/delDic", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delDic(@RequestParam("id") Integer id) {
        logger.info("删除字典数据！id:" + id );
        return baseSysdictionaryService.delDicById(id);
    }

    @GetMapping("getDicListByType")
    @ResponseBody
    public List<BaseSysdictionary> getDicListByType(@RequestParam("type") String  type){
        logger.info("获取字典列表");
        return baseSysdictionaryService.getDics(type);
    }
}

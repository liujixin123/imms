package com.example.imms.web.service.impl;

import com.example.imms.web.dao.BaseSysdictionaryMapper;
import com.example.imms.web.model.BaseSysdictionary;
import com.example.imms.web.model.ImmsSRoom;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.BaseSysdictionaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseSysdictionaryServiceImpl implements BaseSysdictionaryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BaseSysdictionaryMapper baseSysdictionarymMapper;

    @Override
    public PageDataResult getDicList(BaseSysdictionary dic, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        PageDataResult pageDataResult = new PageDataResult();
        List<BaseSysdictionary> dics = baseSysdictionarymMapper.getDicList(dic);

        if(dics.size() != 0){
            PageInfo<BaseSysdictionary> pageInfo = new PageInfo<>(dics);
            pageDataResult.setList(pageInfo.getList());
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public Map<String, Object> addDic(BaseSysdictionary dic) {
        Map<String,Object> data = new HashMap();
        try {
            BaseSysdictionary old = baseSysdictionarymMapper.getDic(dic.getCodevalue(),dic.getLable(),dic.getColumntype(),null);
            if(old != null){
                data.put("code",0);
                data.put("msg","字典信息已存在！");
                logger.error("字典数据[新增]，结果=字典信息已存在！");
                return data;
            }

            int result = baseSysdictionarymMapper.insert(dic);
            if(result == 0){
                data.put("code",0);
                data.put("msg","新增失败！");
                logger.error("字典数据[新增]，结果=新增失败！");
                return data;
            }
            data.put("code",1);
            data.put("msg","新增成功！");
            logger.info("字典数据[新增]，结果=新增成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("字典数据[新增]异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public Map<String, Object> updateDic(BaseSysdictionary dic) {
        Map<String,Object> data = new HashMap();
        try {
            BaseSysdictionary old = baseSysdictionarymMapper.getDic(dic.getCodevalue(),dic.getLable(),dic.getColumntype(),dic.getId());
            if(old != null){
                data.put("code",0);
                data.put("msg","字典信息已存在！");
                logger.error("字典数据[更新]，结果=字典信息已存在！");
                return data;
            }

            int result = baseSysdictionarymMapper.update(dic);
            if(result == 0){
                data.put("code",0);
                data.put("msg","更新失败！");
                logger.error("字典数据[更新]，结果=更新失败！");
                return data;
            }
            data.put("code",1);
            data.put("msg","更新成功！");
            logger.info("字典数据[更新]，结果=更新成功！");
            return data;

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("字典数据[修改]异常！", e);
            return data;
        }
    }

    @Override
    public Map<String, Object> delDicById(Integer id) {
        Map<String, Object> data = new HashMap<>();
        try {
            int result =  baseSysdictionarymMapper.delete(id);
            if(result == 0){
                data.put("code",0);
                data.put("msg","删除字典数据失败");
                logger.error("删除字典数据失败");
                return data;
            }
            data.put("code",1);
            data.put("msg","删除字典数据成功");
            logger.info("删除字典数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除字典数据异常！", e);
        }
        return data;
    }

    @Override
    public List<BaseSysdictionary> getDics(String type) {
        return baseSysdictionarymMapper.getDics(  type) ;
    }
}

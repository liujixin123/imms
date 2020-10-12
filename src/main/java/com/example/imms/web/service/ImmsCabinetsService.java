package com.example.imms.web.service;

import com.example.imms.web.model.ImmsCabinets;
import com.example.imms.web.response.PageDataResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImmsCabinetsService {

    PageDataResult getImmsCabinetsList(ImmsCabinets immsCabinets, Integer pageNum, Integer pageSize);

    Map<String,Object> addImmsCabinets(ImmsCabinets immsCabinets, String uploadRoot, String returnUrl, int width, int height);

    Map<String,Object> updateImmsCabinets(ImmsCabinets immsCabinets);

    Map<String,Object> delDevById(Integer id);

    ImmsCabinets queryById(String id);

    Map<String,Object> deleteCabinetsBatch(String ids);

    List<Map<String,Object>> saveExcel(MultipartFile multipartFile, String uploadRoot, String returnUrl, int width, int height);

    List<ImmsCabinets> queryImmsCabinetsList(ImmsCabinets immsCabinets);
    List<ImmsCabinets> getImmsRoom();
}

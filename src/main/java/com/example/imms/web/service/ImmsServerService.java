package com.example.imms.web.service;

import com.example.imms.web.model.ImmsServer;
import com.example.imms.web.response.PageDataResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImmsServerService {

    PageDataResult getImmsServerList(ImmsServer immsServer, Integer pageNum, Integer pageSize);

    Map<String,Object> addImmsServer(ImmsServer immsServer, String uploadRoot, String returnUrl, int width, int height);

    Map<String,Object> updateImmsServer(ImmsServer immsServer);

    Map<String,Object> delDevById(Integer id);

    ImmsServer queryById(String id);

    Map<String,Object> deleteServerBatch(String ids);

    List<Map<String,Object>> saveExcel(MultipartFile multipartFile, String uploadRoot, String returnUrl, int width, int height);

    List<ImmsServer> queryImmsServerList(ImmsServer immsServer);
    List<ImmsServer> getImmsRoom();

}

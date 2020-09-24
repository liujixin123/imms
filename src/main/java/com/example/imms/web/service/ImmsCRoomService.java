package com.example.imms.web.service;

import com.example.imms.web.model.ImmsCRoom;
import com.example.imms.web.model.ImmsSRoom;
import com.example.imms.web.response.PageDataResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImmsCRoomService {

    PageDataResult getImmsCRoomList(ImmsCRoom immsCRoom, Integer pageNum, Integer pageSize);

    Map<String,Object> addImmsCRoom(ImmsCRoom immsCRoom, String uploadRoot, String returnUrl, int width, int height);

    Map<String,Object> updateImmsCRoom(ImmsCRoom immsCRoom);

    Map<String,Object> delDevById(Integer id);

    ImmsCRoom queryById(String id);

    Map<String,Object> deleteRoomBatch(String ids);

    List<Map<String,Object>> saveExcel(MultipartFile multipartFile, String uploadRoot, String returnUrl, int width, int height);

    List<ImmsCRoom> queryImmsCRoomList(ImmsCRoom immsCRoom);
}

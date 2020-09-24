package com.example.imms.web.service;

import com.example.imms.web.model.ImmsSRoom;
import com.example.imms.web.model.ImmsSRoomExcel;
import com.example.imms.web.response.PageDataResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImmsSRoomService {

    PageDataResult getImmsSRoomList(ImmsSRoom immsSRoom, Integer pageNum, Integer pageSize);

    ImmsSRoom queryById(String id);

    List<ImmsSRoom> queryImmsSRoomList(ImmsSRoom immsSRoom);

    List<ImmsSRoomExcel> queryImmsSRoomExcelList(ImmsSRoom immsSRoom);

    Map<String,Object> addImmsSRoom(ImmsSRoom immsSRoom,String uploadRoot,String  returnUrl, int WIDTH, int HEIGHT);

    Map<String,Object> updateImmsSRoom(ImmsSRoom immsSRoom);

    Map<String,Object> delDevById(Integer id);

    void insertBath(List<ImmsSRoom> list);

    List<Map<String,Object>> saveExcel(MultipartFile multipartFile,String uploadRoot,String  returnUrl, int WIDTH, int HEIGHT);

    Map<String,Object> deleteDevBatch(String ids);
}

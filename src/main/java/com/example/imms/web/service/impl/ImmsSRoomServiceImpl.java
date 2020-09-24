package com.example.imms.web.service.impl;

import com.example.imms.web.common.utils.ConstantUtil;
import com.example.imms.web.common.utils.DateUtils;
import com.example.imms.web.common.utils.ImmsExcelUtils;
import com.example.imms.web.common.utils.TwoUtils;
import com.example.imms.web.dao.*;
import com.example.imms.web.model.*;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.ImmsMsgService;
import com.example.imms.web.service.ImmsSRoomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class  ImmsSRoomServiceImpl implements ImmsSRoomService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsSRoomMapper immsSRoomMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsMsgUserMapper immsMsgUserMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsMsgRelUserMapper immsMsgRelUserMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsMsgMapper immsMsgMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BaseSysdictionaryMapper baseSysdictionarymMapper;

    @Autowired
    public ImmsMsgService immsMsgService;

    @Override
    public PageDataResult getImmsSRoomList(ImmsSRoom immsSRoom, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        PageDataResult pageDataResult = new PageDataResult();
        List<ImmsSRoom> immsSRooms = immsSRoomMapper.getImmsSRoomList(immsSRoom);

        if(immsSRooms.size() != 0){
            PageInfo<ImmsSRoom> pageInfo = new PageInfo<>(immsSRooms);
            pageDataResult.setList(immsSRooms);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public ImmsSRoom queryById(String id) {
        return immsSRoomMapper.queryById(Integer.valueOf(id));
    }

    @Override
    public List<ImmsSRoom> queryImmsSRoomList(ImmsSRoom immsSRoom) {
        return  immsSRoomMapper.getImmsSRoomList(immsSRoom);
    }

    @Override
    public List<ImmsSRoomExcel> queryImmsSRoomExcelList(ImmsSRoom immsSRoom) {
        return  immsSRoomMapper.queryImmsSRoomExcelList(immsSRoom);
    }

    @Override
    public Map<String, Object> addImmsSRoom(ImmsSRoom immsSRoom,String uploadRoot,String  returnUrl,  int WIDTH, int HEIGHT) {
        Map<String,Object> data = new HashMap();
        try {
            ImmsSRoom old = immsSRoomMapper.getImmsSRoomMapperByNumber(immsSRoom.getDevNumber(),null);
            if(old != null){
                data.put("code",0);
                data.put("msg","设备序列号已存在！");
                logger.error("库房设备[新增]，结果=设备序列号已存在！");
                return data;
            }
            immsSRoom.setVersion(DateUtils.getVersion());
            int result = immsSRoomMapper.insert(immsSRoom);
            if(result == 0){
                data.put("code",0);
                data.put("msg","新增失败！");
                logger.error("库房设备[新增]，结果=新增失败！");
                return data;
            }

            // 发送消息
            Map<String,String> map = new HashMap<>();
            map.put("module", ConstantUtil.MSG_SROOM);
            map.put("goUrl",  ConstantUtil.MSG_SROOM_URL);
            map.put("action", "新增");
            map.put("id",     immsSRoom.getId().toString());
            map.put("number", immsSRoom.getDevNumber());
            immsMsgService.sendMsg(map);

            //生成二维码
            TwoUtils.creatTow("2", immsSRoom.getId().toString(), "immsSRoom",   uploadRoot,  returnUrl ,WIDTH , HEIGHT);

            data.put("code",1);
            data.put("msg","新增成功！");
            logger.info("库房设备[新增]，结果=新增成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("库房设备[新增]异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public Map<String, Object> updateImmsSRoom(ImmsSRoom immsSRoom) {
        Map<String,Object> data = new HashMap();
        Integer id = immsSRoom.getId();
        try {
            ImmsSRoom old = immsSRoomMapper.getImmsSRoomMapperByNumber(immsSRoom.getDevNumber(),id);
            if(old != null){
                data.put("code",0);
                data.put("msg","设备序列号已存在！");
                logger.error("库房设备[更新]，结果=设备序列号已存在！");
                return data;
            }

            int result = immsSRoomMapper.update(immsSRoom);
            if(result == 0){
                data.put("code",0);
                data.put("msg","更新失败！");
                logger.error("库房设备[更新]，结果=更新失败！");
                return data;
            }
            Map<String,String> map = new HashMap<>();
            map.put("module", ConstantUtil.MSG_SROOM);
            map.put("goUrl",  ConstantUtil.MSG_SROOM_URL);
            map.put("action", "修改");
            map.put("id",     immsSRoom.getId().toString());
            map.put("number", immsSRoom.getDevNumber());
            immsMsgService.sendMsg(map);

            data.put("code",1);
            data.put("msg","更新成功！");
            logger.info("库房设备[更新]，结果=更新成功！");
            return data;

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("库房设备[修改]异常！", e);
            return data;
        }
    }

    @Override
    public Map<String, Object> delDevById(Integer id) {

        Map<String, Object> data = new HashMap<>();
        try {
            // 删除库房设备
            int result =  immsSRoomMapper.delete(id);
            if(result == 0){
                data.put("code",0);
                data.put("msg","删除库房设备失败");
                logger.error("删除库房设备失败");
                return data;
            }
            data.put("code",1);
            data.put("msg","删除库房设备成功");
            logger.info("删除库房设备成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除库房设备异常！", e);
        }
        return data;
    }

    @Override
    public void insertBath(List<ImmsSRoom> list) {
        immsSRoomMapper.insertBatchs(list);
    }

    @Override
    public List<Map<String, Object>> saveExcel(MultipartFile staffInfo,String uploadRoot,String  returnUrl, int WIDTH, int HEIGHT) {
        //获得文件名
        List<ImmsSRoom>    list = new ArrayList<>();
        List<ImmsSRoom> updateList = new ArrayList<>();
        List<Map<String, Object>> listMsg = new ArrayList<>();
        Map<String, Object> result = new HashMap<String, Object>();
        Workbook workbook = null;
        String msg =  "";
        String version =DateUtils.getVersion();
        try {
            String fileName = staffInfo.getOriginalFilename();
            if(fileName.endsWith(ImmsExcelUtils.XLS)) {
                workbook = new HSSFWorkbook(staffInfo.getInputStream());
            } else if(fileName.endsWith(ImmsExcelUtils.XLSX)) {
                workbook = new XSSFWorkbook(staffInfo.getInputStream());
            } else {
                msg = "文件不是Excel文件!";
                result.put("error", msg);
                listMsg.add(result);
                return listMsg;
            }

            Sheet sheet = workbook.getSheet("Sheet1");
            //获得表头
            Row rowHead = sheet.getRow(0);

            //判断表头是否正确
            if(rowHead.getPhysicalNumberOfCells() != 16)
            {
//            throw new Exception("表头的数量不对!");
                msg = "表头的数量(列数)不对!";
                result.put("error", msg);
                listMsg.add(result);
                return listMsg;
            }

            int rows = sheet.getLastRowNum(); //总行数
            if(rows == 0) {
                msg = "请添加数据之后再导入!";
                result.put("error", msg);
                listMsg.add(result);
                return listMsg;
            }
            Row row;
            ImmsSRoom immsSRoom;
            Cell cell;

            for(int i = 1; i<= rows; i++) {

                Map<String,Object> errorMap = new HashMap<>();
                row = sheet.getRow(i);
                immsSRoom = new ImmsSRoom();
                if(row != null) {
                    cell=row.getCell(0);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setDevType(cell.getStringCellValue());
                    }
                    cell=row.getCell(1);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setDevManu(cell.getStringCellValue());
                    }
                    cell=row.getCell(2);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setDevModel(cell.getStringCellValue());
                    }
                    cell=row.getCell(3);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setDevNumber(cell.getStringCellValue());
                    }

                    cell=row.getCell(4);
                  /*  if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setInRoomTime(cell.getStringCellValue());
                    }*/
                    if(null!=cell){
                       if(cell.getCellType()== Cell.CELL_TYPE_NUMERIC){
                           String value = "";
                           if (HSSFDateUtil.isCellDateFormatted(cell)) { //判断是否为日期格式
                               Date date = cell.getDateCellValue();
                               value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                               immsSRoom.setInRoomTime(value);
                           }
                       }
                    }



                    cell=row.getCell(5);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setUseType(cell.getStringCellValue());
                    }
                    cell=row.getCell(6);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setReceiver(cell.getStringCellValue());
                    }
                    cell=row.getCell(7);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setDevAddress(cell.getStringCellValue());
                    }
                    cell=row.getCell(8);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setResponsibilityDpt(cell.getStringCellValue());
                    }
                    cell=row.getCell(9);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setResponsibilityMan(cell.getStringCellValue());
                    }

                    cell=row.getCell(10);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setProject(cell.getStringCellValue());
                    }
                    cell=row.getCell(11);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if("库存".equals(cell.getStringCellValue())) {
                            immsSRoom.setDevStatus("IN");
                        } else if("出库".equals(cell.getStringCellValue())) {
                            immsSRoom.setDevStatus("OUT");
                        }

                    }
                    cell=row.getCell(12);
             /*       if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setOutRoomTime(cell.getStringCellValue());
                    }*/
                    String value2 = "";
                    if (HSSFDateUtil.isCellDateFormatted(cell)) { //判断是否为日期格式
                        Date date = cell.getDateCellValue();
                        value2 = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        immsSRoom.setOutRoomTime(value2);
                    }
//                    cell.setCellType(Cell.CELL_TYPE_STRING);//设置单元格获取格式
//                    value2 = cell.getStringCellValue();
//                    immsSRoom.setOutRoomTime(value2);

                    cell=row.getCell(13);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setOutRoomUser(cell.getStringCellValue());
                    }

                    cell=row.getCell(14);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setDevGo(cell.getStringCellValue());
                    }

                    cell=row.getCell(15);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setRemarks(cell.getStringCellValue());
                    }

                    immsSRoom.setVersion(version);

                    list.add(immsSRoom);
                }
            }
            workbook.close();

            boolean isMsg=false;

            int countAll =list.size();
            listMsg = filterOrder(list, updateList); // 根据条件过滤筛选
            if(list !=null && list.size() > 0 ){
                int results =  immsSRoomMapper.insertBatchs(list);
                isMsg=true;
            }

            if(updateList !=null && updateList.size()>0){
                int results2 =  immsSRoomMapper.updateBatchs(updateList);
                isMsg=true;
            }

            int insertNum = list.size();
            int updateNum = updateList.size();
            int failNum = countAll-insertNum-updateNum;

            String msgs=  "总结：一共导入："+countAll+"条数据，新增："+insertNum+"条，（序列号重复）更新："+updateNum+"条，失败："
                    +failNum+"条！";
            result.put("error", msgs);
            listMsg.add(result);

            if(isMsg){
                Map<String,String> map = new HashMap<>();
                map.put("module", ConstantUtil.MSG_SROOM);
                map.put("goUrl",  ConstantUtil.MSG_SROOM_URL);
                map.put("action", "导入");
                map.put("id",     "-1");
                map.put("version",     version);
                map.put("number", "新增:"+insertNum+"条,更新:"+updateNum+"条,失败:"+failNum+"条!");
                immsMsgService.sendMsg(map);
            }

            if(list !=null && list.size() > 0 ){
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getId()!= null && !"".equals(list.get(i).getId())){
                       //生成二维码
                        TwoUtils.creatTow("2", list.get(i).getId().toString(), "immsSRoom",   uploadRoot,  returnUrl, WIDTH, HEIGHT);
                    }

                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listMsg;
    }

    @Override
    public Map<String, Object> deleteDevBatch(String ids) {
        Map<String,Object> data = new HashMap();
        try {
            BaseAdminUser user = (BaseAdminUser) SecurityUtils.getSubject().getPrincipal(); // 获取当前登录用户
            List<String> list = new ArrayList<>();
            String[] idsAry = ids.split(",");
            for(int i=0;i< idsAry.length;i++){
                list.add(idsAry[i]);
            }
            int result = immsSRoomMapper.deleteBath(list);
            if(result == 0){
                data.put("code",0);
                data.put("msg","批量删除库房设备失败！");
                logger.error("批量删除库房设备 ，结果= 失败！");
                return data;
            }

            data.put("code",1);
            data.put("msg","批量删除库房设备成功！");
            logger.info("批量删除库房设备 ，结果= 成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("批量删除库房设备异常！", e);
            return data;
        }
        return data;
    }

    private List<Map<String,Object>> filterOrder(List<ImmsSRoom> list,  List<ImmsSRoom> updateList ) {
        List<Map<String, Object>> errorMessages = new ArrayList<>();

        Map<String, Object> errorMap = null;
        boolean isRemove;
        int number = 1;
        ImmsSRoom immsSRoom;
        List<String> devNumbers = new ArrayList<>();
        if(list !=null && list.size() > 0){
            for(int i=0; i<list.size();i++){
                number ++;
                isRemove = false;
                errorMap = new HashMap<>();
                immsSRoom = list.get(i);
                try {
                    // 判断字段是否为空
                    if( immsSRoom.getDevNumber()==null || "".equals( immsSRoom.getDevNumber())){
                        errorMap.put("error", "第" + number + "行：序列号是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if(devNumbers.contains(immsSRoom.getDevNumber())){
                        errorMap.put("error", "第" + number + "行：序列号在该文件中重复。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getDevType()==null || "".equals( immsSRoom.getDevType())){
                        errorMap.put("error", "第" + number + "行：设备类型是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getDevManu()==null || "".equals( immsSRoom.getDevManu())){
                        errorMap.put("error", "第" + number + "行：制造商是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getDevModel()==null || "".equals( immsSRoom.getDevModel())){
                        errorMap.put("error", "第" + number + "行：设备型号是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else  if( immsSRoom.getInRoomTime()==null || "".equals( immsSRoom.getInRoomTime())){
                        errorMap.put("error", "第" + number + "行：入库时间非法或者为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getUseType()==null || "".equals( immsSRoom.getUseType())){
                        errorMap.put("error", "第" + number + "行：类别是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getReceiver()==null || "".equals( immsSRoom.getReceiver())){
                        errorMap.put("error", "第" + number + "行：接收人是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getDevAddress()==null || "".equals( immsSRoom.getDevAddress())){
                        errorMap.put("error", "第" + number + "行：存放地点是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getResponsibilityDpt()==null || "".equals( immsSRoom.getResponsibilityDpt())){
                        errorMap.put("error", "第" + number + "行：责任处室是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getResponsibilityMan()==null || "".equals( immsSRoom.getResponsibilityMan())){
                        errorMap.put("error", "第" + number + "行：责任人是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getProject()==null || "".equals( immsSRoom.getProject())){
                        errorMap.put("error", "第" + number + "行：所属项目是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsSRoom.getDevStatus()==null || "".equals( immsSRoom.getDevStatus())){
                        errorMap.put("error", "第" + number + "行：设备状态是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if(  !DateUtils.isValidDate(immsSRoom.getInRoomTime())){
                        errorMap.put("error", "第" + number + "行：入库时间格式不对。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else{
                        devNumbers.add(immsSRoom.getDevNumber());
                        // 判断序列号是否重复
                        ImmsSRoom immsSRoomNew = immsSRoomMapper.getImmsSRoomMapperByNumber(immsSRoom.getDevNumber(),null);
                        if(immsSRoomNew !=null  ){
                            errorMap.put("error", "第" + number + "行：序列号已经存在。更新数据!" );
                            errorMessages.add(errorMap);
                            isRemove = true;
                            immsSRoom.setId(immsSRoomNew.getId());
                            updateList.add(immsSRoom);
                        }
                    }
                }catch (Exception e) {
                    errorMap.put("error", "第" + i + "行：数据异常无法保存。" );
                    errorMessages.add(errorMap);
                    isRemove = true;
                }

                if(isRemove){
                    list.remove(i);
                    i--;
                }
            }
        }
        return errorMessages;
    }

}

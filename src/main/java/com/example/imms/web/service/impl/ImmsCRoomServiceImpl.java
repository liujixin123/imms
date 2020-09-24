package com.example.imms.web.service.impl;

import com.example.imms.web.common.utils.ConstantUtil;
import com.example.imms.web.common.utils.DateUtils;
import com.example.imms.web.common.utils.ImmsExcelUtils;
import com.example.imms.web.common.utils.TwoUtils;
import com.example.imms.web.dao.ImmsCRoomMapper;
import com.example.imms.web.model.ImmsCRoom;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.ImmsCRoomService;
import com.example.imms.web.service.ImmsMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ImmsCRoomServiceImpl implements ImmsCRoomService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsCRoomMapper immsCRoomMapper;

    @Autowired
    public ImmsMsgService immsMsgService;

    @Override
    public PageDataResult getImmsCRoomList(ImmsCRoom immsCRoom, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        PageDataResult pageDataResult = new PageDataResult();
        List<ImmsCRoom> immsCRooms = immsCRoomMapper.getImmsCRoomList(immsCRoom);

        if(immsCRooms.size() != 0){
            PageInfo<ImmsCRoom> pageInfo = new PageInfo<>(immsCRooms);
            pageDataResult.setList(immsCRooms);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public Map<String, Object> addImmsCRoom(ImmsCRoom immsCRoom, String uploadRoot, String returnUrl, int WIDTH, int HEIGHT) {
        Map<String,Object> data = new HashMap();
        try {
            ImmsCRoom old = immsCRoomMapper.getImmsCRoomMapperByNumber(immsCRoom.getDevNumber(),null);
            if(old != null){
                data.put("code",0);
                data.put("msg","设备序列号已存在！");
                logger.error("机房设备[新增]，结果=设备序列号已存在！");
                return data;
            }
            immsCRoom.setVersion(DateUtils.getVersion());
            int result = immsCRoomMapper.insert(immsCRoom);
            if(result == 0){
                data.put("code",0);
                data.put("msg","新增失败！");
                logger.error("机房设备[新增]，结果=新增失败！");
                return data;
            }

            // 发送消息
            Map<String,String> map = new HashMap<>();
            map.put("module", ConstantUtil.MSG_CROOM);
            map.put("goUrl",  ConstantUtil.MSG_CROOM_URL);
            map.put("action", "新增");
            map.put("id",     immsCRoom.getId().toString());
            map.put("number", immsCRoom.getDevNumber());
            immsMsgService.sendMsg(map);

            //生成二维码
            TwoUtils.creatTow("1", immsCRoom.getId().toString(), "immsCRoom",   uploadRoot,  returnUrl ,WIDTH , HEIGHT);

            data.put("code",1);
            data.put("msg","新增成功！");
            logger.info("机房设备[新增]，结果=新增成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("机房设备[新增]异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public Map<String, Object> updateImmsCRoom(ImmsCRoom immsCRoom) {
        Map<String,Object> data = new HashMap();
        Integer id = immsCRoom.getId();
        try {
            ImmsCRoom old = immsCRoomMapper.getImmsCRoomMapperByNumber(immsCRoom.getDevNumber(),id);
            if(old != null){
                data.put("code",0);
                data.put("msg","设备序列号已存在！");
                logger.error("机房设备[更新]，结果=设备序列号已存在！");
                return data;
            }

            int result = immsCRoomMapper.update(immsCRoom);
            if(result == 0){
                data.put("code",0);
                data.put("msg","更新失败！");
                logger.error("机房设备[更新]，结果=更新失败！");
                return data;
            }
            Map<String,String> map = new HashMap<>();
            map.put("module", ConstantUtil.MSG_CROOM);
            map.put("goUrl",  ConstantUtil.MSG_CROOM_URL);
            map.put("action", "修改");
            map.put("id",     immsCRoom.getId().toString());
            map.put("number", immsCRoom.getDevNumber());
            immsMsgService.sendMsg(map);

            data.put("code",1);
            data.put("msg","更新成功！");
            logger.info("机房设备[更新]，结果=更新成功！");
            return data;

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("机房设备[修改]异常！", e);
            return data;
        }
    }

    @Override
    public Map<String, Object> delDevById(Integer id) {

        Map<String, Object> data = new HashMap<>();
        try {
            // 删除机房设备
            int result =  immsCRoomMapper.delete(id);
            if(result == 0){
                data.put("code",0);
                data.put("msg","删除机房设备失败");
                logger.error("删除机房设备失败");
                return data;
            }
            data.put("code",1);
            data.put("msg","删除机房设备成功");
            logger.info("删除机房设备成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除机房设备异常！", e);
        }
        return data;
    }

    @Override
    public ImmsCRoom queryById(String id)  {
        return immsCRoomMapper.queryById(Integer.valueOf(id));
    }

    @Override
    public Map<String, Object> deleteRoomBatch(String ids) {
        Map<String,Object> data = new HashMap();
        try {
            List<String> list = new ArrayList<>();
            String[] idsAry = ids.split(",");
            for(int i=0;i< idsAry.length;i++){
                list.add(idsAry[i]);
            }
            int result = immsCRoomMapper.deleteBath(list);
            if(result == 0){
                data.put("code",0);
                data.put("msg","批量删除机房设备失败！");
                logger.error("批量删除机房设备 ，结果= 失败！");
                return data;
            }

            data.put("code",1);
            data.put("msg","批量删除机房设备成功！");
            logger.info("批量删除机房设备 ，结果= 成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("批量删除机房设备异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public List<Map<String, Object>> saveExcel(MultipartFile staffInfo,String uploadRoot,String  returnUrl, int WIDTH, int HEIGHT) {
        //获得文件名
        List<ImmsCRoom>    list = new ArrayList<>();
        List<ImmsCRoom> updateList = new ArrayList<>();
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
            if(rowHead.getPhysicalNumberOfCells() != 18)
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
            ImmsCRoom immsCRoom;
            Cell cell;

            for(int i = 1; i<= rows; i++) {

                Map<String,Object> errorMap = new HashMap<>();
                row = sheet.getRow(i);
                immsCRoom = new ImmsCRoom();
                if(row != null) {
                    cell=row.getCell(0);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setCabinetName(cell.getStringCellValue());//机柜名称
                    }
                    cell=row.getCell(1);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setDevHeight(cell.getStringCellValue());//设备高度
                    }
                    cell=row.getCell(2);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setDevName(cell.getStringCellValue());//设备名称
                    }
                    cell=row.getCell(3);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setDevManu(cell.getStringCellValue());//制造商
                    }

                    cell=row.getCell(4);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setDevModel(cell.getStringCellValue());//设备型号
                    }

                    cell=row.getCell(5);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setDevNumber(cell.getStringCellValue());
                    }
                    cell=row.getCell(6);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setSystem(cell.getStringCellValue());
                    }
                    cell=row.getCell(7);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setSafeArea(cell.getStringCellValue());
                    }
                    cell=row.getCell(8);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setMaintainLevel(cell.getStringCellValue());
                    }
                    cell=row.getCell(9);
                    if(null!=cell){
                        String value = "";
                        if (HSSFDateUtil.isCellDateFormatted(cell)) { //判断是否为日期格式
                            Date date = cell.getDateCellValue();
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            immsCRoom.setUseTime(value);
                        }
                    }

                    cell=row.getCell(10);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setResponsibilityDpt(cell.getStringCellValue());
                    }
                    cell=row.getCell(11);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setResponsibilityMan(cell.getStringCellValue());
                    }

                    cell=row.getCell(12);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setMaintainManu(cell.getStringCellValue());
                    }

                    cell=row.getCell(13);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setMaintainMan(cell.getStringCellValue());
                    }

                    cell=row.getCell(14);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setMaintainManPhone(cell.getStringCellValue());
                    }

                    cell=row.getCell(15);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String ip =cell.getStringCellValue();
                        if(ip !=null && !"".equals(ip)){
                            immsCRoom.setIp(ip.replaceAll("\n",";"));
                        }

                    }
                    cell=row.getCell(16);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setRoom(cell.getStringCellValue());
                    }

                    cell=row.getCell(17);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCRoom.setRemarks(cell.getStringCellValue());
                    }

                    immsCRoom.setVersion(version);

                    list.add(immsCRoom);
                }
            }
            workbook.close();

            boolean isMsg=false;

            int countAll =list.size();
            listMsg = filterOrder(list, updateList); // 根据条件过滤筛选
            if(list !=null && list.size() > 0 ){
                int results =  immsCRoomMapper.insertBatchs(list);
                isMsg=true;
            }

            if(updateList !=null && updateList.size()>0){
                int results2 =  immsCRoomMapper.updateBatchs(updateList);
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
                map.put("module", ConstantUtil.MSG_CROOM);
                map.put("goUrl",  ConstantUtil.MSG_CROOM_URL);
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
                        TwoUtils.creatTow("1", list.get(i).getId().toString(), "immsCRoom",   uploadRoot,  returnUrl, WIDTH, HEIGHT);
                    }

                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listMsg;
    }

    @Override
    public List<ImmsCRoom> queryImmsCRoomList(ImmsCRoom immsCRoom) {
        return  immsCRoomMapper.getImmsCRoomList(immsCRoom);
    }

    private List<Map<String,Object>> filterOrder(List<ImmsCRoom> list,  List<ImmsCRoom> updateList ) {
        List<Map<String, Object>> errorMessages = new ArrayList<>();

        Map<String, Object> errorMap = null;
        boolean isRemove;
        int number = 1;
        ImmsCRoom immsCRoom;
        List<String> devNumbers = new ArrayList<>();
        if(list !=null && list.size() > 0){
            for(int i=0; i<list.size();i++){
                number ++;
                isRemove = false;
                errorMap = new HashMap<>();
                immsCRoom = list.get(i);
                try {
                    // 判断字段是否为空
                    if( immsCRoom.getDevNumber()==null || "".equals( immsCRoom.getDevNumber())){
                        errorMap.put("error", "第" + number + "行：序列号是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if(devNumbers.contains(immsCRoom.getDevNumber())){
                        errorMap.put("error", "第" + number + "行：序列号在该文件中重复。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getCabinetName()==null || "".equals( immsCRoom.getCabinetName())){
                        errorMap.put("error", "第" + number + "行：机柜名称是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getDevHeight()==null || "".equals( immsCRoom.getDevHeight())){
                        errorMap.put("error", "第" + number + "行：设备高度是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getDevName()==null || "".equals( immsCRoom.getDevName())){
                        errorMap.put("error", "第" + number + "行：设备名称是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getDevManu()==null || "".equals( immsCRoom.getDevManu())){
                        errorMap.put("error", "第" + number + "行：制造商是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getDevModel()==null || "".equals( immsCRoom.getDevModel())){
                        errorMap.put("error", "第" + number + "行：入库设备型号为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getSystem()==null || "".equals( immsCRoom.getSystem())){
                        errorMap.put("error", "第" + number + "行：所属系统是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getSafeArea()==null || "".equals( immsCRoom.getSafeArea())){
                        errorMap.put("error", "第" + number + "行：安全区是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getUseTime()==null || "".equals( immsCRoom.getUseTime())){
                        errorMap.put("error", "第" + number + "行：投运日期非法或者为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getResponsibilityDpt()==null || "".equals( immsCRoom.getResponsibilityDpt())){
                        errorMap.put("error", "第" + number + "行：责任处室是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getResponsibilityMan()==null || "".equals( immsCRoom.getResponsibilityMan())){
                        errorMap.put("error", "第" + number + "行：责任人是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getMaintainManu()==null || "".equals( immsCRoom.getMaintainManu())){
                        errorMap.put("error", "第" + number + "行：运维厂商是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getMaintainMan()==null || "".equals( immsCRoom.getMaintainMan())){
                        errorMap.put("error", "第" + number + "行：运维人员为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getMaintainManPhone()==null || "".equals( immsCRoom.getMaintainManPhone())){
                        errorMap.put("error", "第" + number + "行：联系电话是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getIp()==null || "".equals( immsCRoom.getIp())){
                        errorMap.put("error", "第" + number + "行：IP是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCRoom.getRoom()==null || "".equals( immsCRoom.getRoom())){
                        errorMap.put("error", "第" + number + "行：所属机房是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if(  !DateUtils.isValidDate(immsCRoom.getUseTime())){
                        errorMap.put("error", "第" + number + "行：投运日期格式不对。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else{
                        devNumbers.add(immsCRoom.getDevNumber());
                        // 判断序列号是否重复
                        ImmsCRoom immsCRoomNew = immsCRoomMapper.getImmsCRoomMapperByNumber(immsCRoom.getDevNumber(),null);
                        if(immsCRoomNew !=null  ){
                            errorMap.put("error", "第" + number + "行：序列号已经存在。更新数据!" );
                            errorMessages.add(errorMap);
                            isRemove = true;
                            immsCRoom.setId(immsCRoomNew.getId());
                            updateList.add(immsCRoom);
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

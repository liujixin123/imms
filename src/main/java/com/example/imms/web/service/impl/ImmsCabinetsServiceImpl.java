package com.example.imms.web.service.impl;

import com.example.imms.web.common.utils.DateUtils;
import com.example.imms.web.common.utils.ImmsExcelUtils;
import com.example.imms.web.common.utils.TwoUtils;
import com.example.imms.web.dao.ImmsCabinetsMapper;
import com.example.imms.web.model.ImmsCabinets;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.ImmsCabinetsService;
import com.example.imms.web.service.ImmsMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
public class ImmsCabinetsServiceImpl implements ImmsCabinetsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsCabinetsMapper immsCabinetsMapper;

    @Autowired
    public ImmsMsgService immsMsgService;

    @Override
    public PageDataResult getImmsCabinetsList(ImmsCabinets immsCabinets, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        PageDataResult pageDataResult = new PageDataResult();
        List<ImmsCabinets> immsCabinetss = immsCabinetsMapper.getImmsCabinetsList(immsCabinets);

        if(immsCabinetss.size() != 0){
            PageInfo<ImmsCabinets> pageInfo = new PageInfo<>(immsCabinetss);
            pageDataResult.setList(immsCabinetss);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public Map<String, Object> addImmsCabinets(ImmsCabinets immsCabinets, String uploadRoot, String returnUrl, int WIDTH, int HEIGHT) {
        Map<String,Object> data = new HashMap();
        try {
            ImmsCabinets old = immsCabinetsMapper.getImmsCabinetsMapperByCode(immsCabinets.getDevCode(),null);
            if(old != null){
                data.put("code",0);
                data.put("msg","屏柜编码已存在！");
                logger.error("屏柜设备[新增]，结果=屏柜编码已存在！");
                return data;
            }
            String value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            immsCabinets.setRemarks("手动添加");
            immsCabinets.setCreateTime(value);
            int result = immsCabinetsMapper.insert(immsCabinets);
            if(result == 0){
                data.put("code",0);
                data.put("msg","新增失败！");
                logger.error("屏柜设备[新增]，结果=新增失败！");
                return data;
            }

            //生成二维码
            TwoUtils.creatTow("1", immsCabinets.getId().toString(), "immsCabinets",   uploadRoot,  returnUrl ,WIDTH , HEIGHT);

            data.put("code",1);
            data.put("msg","新增成功！");
            logger.info("屏柜设备[新增]，结果=新增成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("屏柜设备[新增]异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public Map<String, Object> updateImmsCabinets(ImmsCabinets immsCabinets) {
        Map<String,Object> data = new HashMap();
        Integer id = immsCabinets.getId();
        try {
            ImmsCabinets old = immsCabinetsMapper.getImmsCabinetsMapperByCode(immsCabinets.getDevCode(),id);
            if(old != null){
                data.put("code",0);
                data.put("msg","屏柜编码已存在！");
                logger.error("屏柜设备[更新]，结果=屏柜编码已存在！");
                return data;
            }
            int result = immsCabinetsMapper.update(immsCabinets);
            if(result == 0){
                data.put("code",0);
                data.put("msg","更新失败！");
                logger.error("屏柜设备[更新]，结果=更新失败！");
                return data;
            }
            data.put("code",1);
            data.put("msg","更新成功！");
            logger.info("屏柜设备[更新]，结果=更新成功！");
            return data;

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("屏柜设备[修改]异常！", e);
            return data;
        }
    }
    @Override
    public Map<String, Object> delDevById(Integer id) {
        Map<String, Object> data = new HashMap<>();
        try {
            // 删除屏柜设备
            int result =  immsCabinetsMapper.delete(id);
            if(result == 0){
                data.put("code",0);
                data.put("msg","删除屏柜设备失败");
                logger.error("删除屏柜设备失败");
                return data;
            }
            data.put("code",1);
            data.put("msg","删除屏柜设备成功");
            logger.info("删除屏柜设备成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除屏柜设备异常！", e);
        }
        return data;
    }

    @Override
    public ImmsCabinets queryById(String id)  {
        return immsCabinetsMapper.queryById(Integer.valueOf(id));
    }

    @Override
    public Map<String, Object> deleteCabinetsBatch(String ids) {
        Map<String,Object> data = new HashMap();
        try {
            List<String> list = new ArrayList<>();
            String[] idsAry = ids.split(",");
            for(int i=0;i< idsAry.length;i++){
                list.add(idsAry[i]);
            }
            int result = immsCabinetsMapper.deleteBath(list);
            if(result == 0){
                data.put("code",0);
                data.put("msg","批量删除屏柜设备失败！");
                logger.error("批量删除屏柜设备 ，结果= 失败！");
                return data;
            }

            data.put("code",1);
            data.put("msg","批量删除屏柜设备成功！");
            logger.info("批量删除屏柜设备 ，结果= 成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("批量删除屏柜设备异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public List<Map<String, Object>> saveExcel(MultipartFile staffInfo,String uploadRoot,String  returnUrl, int WIDTH, int HEIGHT) {
        //获得文件名
        List<ImmsCabinets>    list = new ArrayList<>();
        List<ImmsCabinets> updateList = new ArrayList<>();
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
            if(rowHead.getPhysicalNumberOfCells() != 10)
            {
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
            ImmsCabinets immsCabinets;
            Cell cell;

            for(int i = 1; i<= rows; i++) {

                Map<String,Object> errorMap = new HashMap<>();
                row = sheet.getRow(i);
                immsCabinets = new ImmsCabinets();
                if(row != null) {
                    cell=row.getCell(0);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setCabinetName(cell.getStringCellValue());//屏柜名称
                    }
                    cell=row.getCell(1);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setDevCode(cell.getStringCellValue());//编码
                    }
                    cell=row.getCell(2);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setDevHeight(cell.getStringCellValue());//高度
                    }
                    cell=row.getCell(3);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setResponsibilityMan(cell.getStringCellValue());//责任人
                    }
                    cell=row.getCell(4);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setDevType(cell.getStringCellValue());//类型
                    }
                    cell=row.getCell(5);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setRoomId(cell.getStringCellValue());//所属机房
                    }
                    cell=row.getCell(6);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setLocationX(cell.getStringCellValue());//位置X值
                    }
                    cell=row.getCell(7);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setLocationY(cell.getStringCellValue());//位置Y值
                    }
                    cell=row.getCell(8);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setBelongsPartition(cell.getStringCellValue());//所属分区
                    }
                    cell=row.getCell(9);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsCabinets.setDevDescribe(cell.getStringCellValue());//描述
                    }
                    list.add(immsCabinets);
                }
            }
            workbook.close();

            int countAll =list.size();
            listMsg = filterOrder(list, updateList); // 根据条件过滤筛选
            if(list !=null && list.size() > 0 ){
                immsCabinetsMapper.insertBatchs(list);
            }
            if(updateList !=null && updateList.size()>0){
                immsCabinetsMapper.updateBatchs(updateList);
            }
            int insertNum = list.size();
            int updateNum = updateList.size();
            int failNum = countAll-insertNum-updateNum;

            String msgs=  "总结：一共导入："+countAll+"条数据，新增："+insertNum+"条，（编号重复）更新："+updateNum+"条，失败："
                    +failNum+"条！";
            result.put("error", msgs);
            listMsg.add(result);

            if(list !=null && list.size() > 0 ){
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getId()!= null && !"".equals(list.get(i).getId())){
                        //生成二维码
                        TwoUtils.creatTow("1", list.get(i).getId().toString(), "immsCabinets",   uploadRoot,  returnUrl, WIDTH, HEIGHT);
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listMsg;
    }

    @Override
    public List<ImmsCabinets> queryImmsCabinetsList(ImmsCabinets immsCabinets) {
        return  immsCabinetsMapper.getImmsCabinetsList(immsCabinets);
    }

    private List<Map<String,Object>> filterOrder(List<ImmsCabinets> list,  List<ImmsCabinets> updateList ) {
        List<Map<String, Object>> errorMessages = new ArrayList<>();

        Map<String, Object> errorMap = null;
        boolean isRemove;
        int number = 1;
        ImmsCabinets immsCabinets;
        List<String> code = new ArrayList<>();
        if(list !=null && list.size() > 0){
            for(int i=0; i<list.size();i++){
                number ++;
                isRemove = false;
                errorMap = new HashMap<>();
                immsCabinets = list.get(i);
                try {
                    // 判断字段是否为空
                    if( immsCabinets.getRoomName()==null || "".equals( immsCabinets.getRoomName())){
                        errorMap.put("error", "第" + number + "行：所属机房是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getCabinetName()==null || "".equals( immsCabinets.getCabinetName())){
                        errorMap.put("error", "第" + number + "行：屏柜名称是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if(code.contains(immsCabinets.getDevCode())){
                        errorMap.put("error", "第" + number + "行：编码在该文件中重复。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getDevHeight()==null || "".equals( immsCabinets.getDevHeight())){
                        errorMap.put("error", "第" + number + "行：高度是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getResponsibilityMan()==null || "".equals( immsCabinets.getResponsibilityMan())){
                        errorMap.put("error", "第" + number + "行：责任人是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getDevType()==null || "".equals( immsCabinets.getDevType())){
                        errorMap.put("error", "第" + number + "行：类型是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getRoomId()==null || "".equals( immsCabinets.getRoomId())){
                        errorMap.put("error", "第" + number + "行：所属机房是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getLocationX()==null || "".equals( immsCabinets.getLocationX())){
                        errorMap.put("error", "第" + number + "行：位置X值为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getLocationY()==null || "".equals( immsCabinets.getLocationY())){
                        errorMap.put("error", "第" + number + "行：位置Y值是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getBelongsPartition()==null || "".equals( immsCabinets.getBelongsPartition())){
                        errorMap.put("error", "第" + number + "行：所属分区为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsCabinets.getRoomName()!=null && !"".equals( immsCabinets.getRoomName())) {
                        List<ImmsCabinets> list1 = immsCabinetsMapper.getImmsRoomMapper(immsCabinets);
                        if (list1 != null && list1.size() > 0) {
                            immsCabinets.setRoomId(list1.get(0).getRoomId());
                        } else {
                            errorMap.put("error", "第" + number + "行：未找到所属机房。");
                            errorMessages.add(errorMap);
                            isRemove = true;
                        }
                    }else {
                        code.add(immsCabinets.getDevCode());
                        // 判断序列号是否重复
                        ImmsCabinets immsCabinetsNew = immsCabinetsMapper.getImmsCabinetsMapperByCode(immsCabinets.getDevCode(),null);
                        if(immsCabinetsNew !=null  ){
                            errorMap.put("error", "第" + number + "行：编码已经存在。更新数据!" );
                            errorMessages.add(errorMap);
                            isRemove = true;
                            immsCabinets.setId(immsCabinetsNew.getId());
                            updateList.add(immsCabinets);
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
                }else {
                    String value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    immsCabinets.setRemarks("自动导入");
                    immsCabinets.setCreateTime(value);
                }
            }
        }
        return errorMessages;
    }
    @Override
    public List<ImmsCabinets> getImmsRoom() {
        return immsCabinetsMapper.getImmsRoomMapper(new ImmsCabinets()) ;
    }
}

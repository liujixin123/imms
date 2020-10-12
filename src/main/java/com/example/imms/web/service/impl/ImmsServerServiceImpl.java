package com.example.imms.web.service.impl;

import com.example.imms.web.common.utils.DateUtils;
import com.example.imms.web.common.utils.ImmsExcelUtils;
import com.example.imms.web.common.utils.TwoUtils;
import com.example.imms.web.dao.ImmsServerMapper;
import com.example.imms.web.model.ImmsServer;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.ImmsServerService;
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
public class ImmsServerServiceImpl implements ImmsServerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ImmsServerMapper immsServerMapper;

    @Autowired
    public ImmsMsgService immsMsgService;

    @Override
    public PageDataResult getImmsServerList(ImmsServer immsServer, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        PageDataResult pageDataResult = new PageDataResult();
        List<ImmsServer> immsServers = immsServerMapper.getImmsServerList(immsServer);

        if(immsServers.size() != 0){
            PageInfo<ImmsServer> pageInfo = new PageInfo<>(immsServers);
            pageDataResult.setList(immsServers);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public Map<String, Object> addImmsServer(ImmsServer immsServer, String uploadRoot, String returnUrl, int WIDTH, int HEIGHT) {
        Map<String,Object> data = new HashMap();
        try {
            String value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            immsServer.setRemarks("手动添加");
            immsServer.setCreateTime(value);
            int result = immsServerMapper.insert(immsServer);
            if(result == 0){
                data.put("code",0);
                data.put("msg","新增失败！");
                logger.error("服务器设备[新增]，结果=新增失败！");
                return data;
            }

            //生成二维码
            TwoUtils.creatTow("1", immsServer.getId().toString(), "immsServer",   uploadRoot,  returnUrl ,WIDTH , HEIGHT);

            data.put("code",1);
            data.put("msg","新增成功！");
            logger.info("服务器设备[新增]，结果=新增成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器设备[新增]异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public Map<String, Object> updateImmsServer(ImmsServer immsServer) {
        Map<String,Object> data = new HashMap();
        Integer id = immsServer.getId();
        try {
            int result = immsServerMapper.update(immsServer);
            if(result == 0){
                data.put("code",0);
                data.put("msg","更新失败！");
                logger.error("服务器设备[更新]，结果=更新失败！");
                return data;
            }
            data.put("code",1);
            data.put("msg","更新成功！");
            logger.info("服务器设备[更新]，结果=更新成功！");
            return data;

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器设备[修改]异常！", e);
            return data;
        }
    }
    @Override
    public Map<String, Object> delDevById(Integer id) {
        Map<String, Object> data = new HashMap<>();
        try {
            // 删除服务器设备
            int result =  immsServerMapper.delete(id);
            if(result == 0){
                data.put("code",0);
                data.put("msg","删除服务器设备失败");
                logger.error("删除服务器设备失败");
                return data;
            }
            data.put("code",1);
            data.put("msg","删除服务器设备成功");
            logger.info("删除服务器设备成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除服务器设备异常！", e);
        }
        return data;
    }

    @Override
    public ImmsServer queryById(String id)  {
        return immsServerMapper.queryById(Integer.valueOf(id));
    }

    @Override
    public Map<String, Object> deleteServerBatch(String ids) {
        Map<String,Object> data = new HashMap();
        try {
            List<String> list = new ArrayList<>();
            String[] idsAry = ids.split(",");
            for(int i=0;i< idsAry.length;i++){
                list.add(idsAry[i]);
            }
            int result = immsServerMapper.deleteBath(list);
            if(result == 0){
                data.put("code",0);
                data.put("msg","批量删除服务器设备失败！");
                logger.error("批量删除服务器设备 ，结果= 失败！");
                return data;
            }

            data.put("code",1);
            data.put("msg","批量删除服务器设备成功！");
            logger.info("批量删除服务器设备 ，结果= 成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("批量删除服务器设备异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public List<Map<String, Object>> saveExcel(MultipartFile staffInfo,String uploadRoot,String  returnUrl, int WIDTH, int HEIGHT) {
        //获得文件名
        List<ImmsServer>    list = new ArrayList<>();
        List<Map<String, Object>> listMsg = new ArrayList<>();
        Map<String, Object> result = new HashMap<String, Object>();
        Workbook workbook = null;
        String msg =  "";
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
            if(rowHead.getPhysicalNumberOfCells() != 21)
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
            ImmsServer immsServer;
            Cell cell;

            for(int i = 1; i<= rows; i++) {

                Map<String,Object> errorMap = new HashMap<>();
                row = sheet.getRow(i);
                immsServer = new ImmsServer();
                if(row != null) {
                    cell=row.getCell(0);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setPowerRate(cell.getStringCellValue());
                    }
                    cell=row.getCell(1);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setPowerNum(cell.getStringCellValue());
                    }
                    cell=row.getCell(2);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setSystemType(cell.getStringCellValue());
                    }
                    cell=row.getCell(3);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setSystemVersion(cell.getStringCellValue());
                    }
                    cell=row.getCell(4);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setCpuNum(cell.getStringCellValue());
                    }
                    cell=row.getCell(5);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setCpuXh(cell.getStringCellValue());
                    }
                    cell=row.getCell(6);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setCpuZp(cell.getStringCellValue());
                    }
                    cell=row.getCell(7);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setCpuHs(cell.getStringCellValue());
                    }
                    cell=row.getCell(8);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setRomNum(cell.getStringCellValue());
                    }
                    cell=row.getCell(9);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setRomSize(cell.getStringCellValue());
                    }
                    cell=row.getCell(10);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setDiskType(cell.getStringCellValue());
                    }
                    cell=row.getCell(11);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setDiskNum(cell.getStringCellValue());
                    }
                    cell=row.getCell(12);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setDiskSize(cell.getStringCellValue());
                    }
                    cell=row.getCell(13);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setHbaNum(cell.getStringCellValue());
                    }
                    cell=row.getCell(14);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setNetcardNum(cell.getStringCellValue());
                    }
                    cell=row.getCell(15);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setRaidDetail(cell.getStringCellValue());
                    }
                    cell=row.getCell(16);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setExternalRom(cell.getStringCellValue());
                    }
                    cell=row.getCell(17);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setDoubleVersion(cell.getStringCellValue());
                    }
                    cell=row.getCell(18);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setGks(cell.getStringCellValue());
                    }
                    cell=row.getCell(19);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setServerPwd(cell.getStringCellValue());
                    }
                    cell=row.getCell(20);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsServer.setRoomName(cell.getStringCellValue());
                    }
                    list.add(immsServer);
                }
            }
            workbook.close();

            int countAll =list.size();
            listMsg = filterOrder(list); // 根据条件过滤筛选
            if(list !=null && list.size() > 0 ){
                immsServerMapper.insertBatchs(list);
            }
            int insertNum = list.size();
            int failNum = countAll-insertNum;

            String msgs=  "总结：一共导入："+countAll+"条数据，新增："+insertNum+"条，失败："
                    +failNum+"条！";
            result.put("error", msgs);
            listMsg.add(result);

            if(list !=null && list.size() > 0 ){
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getId()!= null && !"".equals(list.get(i).getId())){
                        //生成二维码
                        TwoUtils.creatTow("1", list.get(i).getId().toString(), "immsServer",   uploadRoot,  returnUrl, WIDTH, HEIGHT);
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listMsg;
    }

    @Override
    public List<ImmsServer> queryImmsServerList(ImmsServer immsServer) {
        return  immsServerMapper.getImmsServerList(immsServer);
    }

    private List<Map<String,Object>> filterOrder(List<ImmsServer> list) {
        List<Map<String, Object>> errorMessages = new ArrayList<>();
        Map<String, Object> errorMap = null;
        boolean isRemove;
        int number = 1;
        ImmsServer immsServer;
        if(list !=null && list.size() > 0){
            for(int i=0; i<list.size();i++){
                number ++;
                isRemove = false;
                errorMap = new HashMap<>();
                immsServer = list.get(i);
                try {
                    // 判断字段是否为空
                    if( immsServer.getRoomName()==null || "".equals( immsServer.getRoomName())){
                        errorMap.put("error", "第" + number + "行：所属机房是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getPowerRate()==null || "".equals( immsServer.getPowerRate())){
                        errorMap.put("error", "第" + number + "行：电源额定功率是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getPowerNum()==null || "".equals( immsServer.getPowerNum())){
                        errorMap.put("error", "第" + number + "行：电源数量是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getSystemType()==null || "".equals( immsServer.getSystemType())){
                        errorMap.put("error", "第" + number + "行：操作系统类型是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getSystemVersion()==null || "".equals( immsServer.getSystemVersion())){
                        errorMap.put("error", "第" + number + "行：操作系统版本是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getCpuNum()==null || "".equals( immsServer.getCpuNum())){
                        errorMap.put("error", "第" + number + "行：CPU数量是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getCpuXh()==null || "".equals( immsServer.getCpuXh())){
                        errorMap.put("error", "第" + number + "行：CPU型号为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getCpuZp()==null || "".equals( immsServer.getCpuZp())){
                        errorMap.put("error", "第" + number + "行：CPU主频是空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getCpuHs()==null || "".equals( immsServer.getCpuHs())){
                        errorMap.put("error", "第" + number + "行：单CPU核数为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getRomNum()==null || "".equals( immsServer.getRomNum())){
                        errorMap.put("error", "第" + number + "行：内存条数量为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getRomSize()==null || "".equals( immsServer.getRomSize())){
                        errorMap.put("error", "第" + number + "行：内存大小为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getDiskType()==null || "".equals( immsServer.getDiskType())){
                        errorMap.put("error", "第" + number + "行：硬盘类型为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getDiskNum()==null || "".equals( immsServer.getDiskNum())){
                        errorMap.put("error", "第" + number + "行：硬盘数量为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getDiskSize()==null || "".equals( immsServer.getDiskSize())){
                        errorMap.put("error", "第" + number + "行：硬盘容量为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getHbaNum()==null || "".equals( immsServer.getHbaNum())){
                        errorMap.put("error", "第" + number + "行：HBA卡数为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getNetcardNum()==null || "".equals( immsServer.getNetcardNum())){
                        errorMap.put("error", "第" + number + "行：网卡数为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getRaidDetail()==null || "".equals( immsServer.getRaidDetail())){
                        errorMap.put("error", "第" + number + "行：RAID方式为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getExternalRom()==null || "".equals( immsServer.getExternalRom())){
                        errorMap.put("error", "第" + number + "行：外接存储为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getDoubleVersion()==null || "".equals( immsServer.getDoubleVersion())){
                        errorMap.put("error", "第" + number + "行：双机版本为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getGks()==null || "".equals( immsServer.getGks())){
                        errorMap.put("error", "第" + number + "行：光口数为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else if( immsServer.getServerPwd()==null || "".equals( immsServer.getServerPwd())){
                        errorMap.put("error", "第" + number + "行：用户名/口令为空。" );
                        errorMessages.add(errorMap);
                        isRemove = true;
                    }else {
                        List<ImmsServer> list1 = immsServerMapper.getImmsRoomMapper(immsServer);
                        if(list1 !=null && list1.size()>0 ){
                            immsServer.setRoomId(list1.get(0).getRoomId());
                        }else {
                            errorMap.put("error", "第" + number + "行：未找到所属机房。" );
                            errorMessages.add(errorMap);
                            isRemove = true;
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
                    immsServer.setRemarks("自动导入");
                    immsServer.setCreateTime(value);
                }
            }
        }
        return errorMessages;
    }
    @Override
    public List<ImmsServer> getImmsRoom() {
        return immsServerMapper.getImmsRoomMapper(new ImmsServer()) ;
    }
}

package com.example.imms.web.common.utils;

import com.example.imms.web.model.ImmsSRoom;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImmsExcelUtils {


    public final static String XLS = "xls";
    public final static String XLSX = "xlsx";
    @SuppressWarnings({ "resource", "unused" })
    public static List<ImmsSRoom> importExcel(MultipartFile staffInfo) throws Exception {
        //获得文件名
        List<ImmsSRoom>    list = new ArrayList<>();
        Workbook workbook = null;
        try {
            String fileName = staffInfo.getOriginalFilename();
            if(fileName.endsWith(XLS)) {
                workbook = new HSSFWorkbook(staffInfo.getInputStream());
            } else if(fileName.endsWith(XLSX)) {
                workbook = new XSSFWorkbook(staffInfo.getInputStream());
            } else {
//            throw new Exception("文件不是Excel文件");
                return null;
            }

            Sheet sheet = workbook.getSheet("Sheet1");
            //获得表头
            Row rowHead = sheet.getRow(0);

            //判断表头是否正确
            if(rowHead.getPhysicalNumberOfCells() != 14)
            {
//            throw new Exception("表头的数量不对!");
                return null;
            }

            int rows = sheet.getLastRowNum(); //总行数
            if(rows == 0) {
                return null;
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
                        immsSRoom.setInRoomTime(cell.getStringCellValue());
                    }
                    cell=row.getCell(4);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setUseType(cell.getStringCellValue());
                    }
                    cell=row.getCell(5);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setReceiver(cell.getStringCellValue());
                    }
                    cell=row.getCell(6);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setDevAddress(cell.getStringCellValue());
                    }
                    cell=row.getCell(7);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setResponsibilityDpt(cell.getStringCellValue());
                    }
                    cell=row.getCell(8);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setResponsibilityMan(cell.getStringCellValue());
                    }

                    cell=row.getCell(9);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setProject(cell.getStringCellValue());
                    }
                    cell=row.getCell(10);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if("库存".equals(cell.getStringCellValue())) {
                            immsSRoom.setDevStatus("IN");
                        } else if("出库".equals(cell.getStringCellValue())) {
                            immsSRoom.setDevStatus("OUT");
                        }

                    }
                    cell=row.getCell(11);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setOutRoomTime(cell.getStringCellValue());
                    }
                    cell=row.getCell(12);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setOutRoomUser(cell.getStringCellValue());
                    }
                    cell=row.getCell(13);
                    if(null!=cell){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        immsSRoom.setRemarks(cell.getStringCellValue());
                    }

                    immsSRoom.setVersion(DateUtils.getVersion());
                    list.add(immsSRoom);
                }
            }
            workbook.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}

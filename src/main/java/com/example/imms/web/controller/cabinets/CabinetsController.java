package com.example.imms.web.controller.cabinets;

import com.example.imms.web.common.utils.ToZIPUtil;
import com.example.imms.web.common.utils.TwoUtils;
import com.example.imms.web.model.BaseSysdictionary;
import com.example.imms.web.model.ImmsCabinets;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.BaseSysdictionaryService;
import com.example.imms.web.service.ImmsCabinetsService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 屏柜管理
 */
@Controller
@RequestMapping("cabinets")
public class CabinetsController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImmsCabinetsService immsCabinetsService;

    @Autowired
    public BaseSysdictionaryService baseSysdictionaryService;


    @RequestMapping("/importCabinets")
    public String importCabinets() {
        return "importCabinets";
    }

    @Value("${uploadRoot}")
    private String uploadRoot;

    /**
     * 进入屏柜管理页面
     * @param model
     * @return
     */
    @RequestMapping("/cabinetsManage")
    public String userManage(Model model) {
        Object roleCode=  SecurityUtils.getSubject().getSession().getAttribute("roleCode");
        model.addAttribute("roleCode",  roleCode==null?"":(String)roleCode);
        return "cabinets/cabinetsManage";
     }
    @RequestMapping(value = "/getImmsCabinetsList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getImmsCabinetsList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,ImmsCabinets immsCabinets) {
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = immsCabinetsService.getImmsCabinetsList(immsCabinets, pageNum ,pageSize);
            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    @RequestMapping(value = "/setImmsCabinets", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setImmsCabinets(HttpServletRequest request,ImmsCabinets immsCabinets) {
        logger.info("设置屏柜设备[新增或更新]！immsCabinets:" + immsCabinets);
        Map<String,Object> data = new HashMap();
        int WIDTH=0;
        int HEIGHT=0;
        if(immsCabinets.getId() == null || "".equals(immsCabinets.getId())){
            List<BaseSysdictionary> dics =  baseSysdictionaryService.getDics( "TWO");
            if(dics !=null && dics.size()>0){
                for(BaseSysdictionary dic:dics){
                    if("WIDTH".equals(dic.getLable())){
                        WIDTH = Integer.parseInt(dic.getCodevalue());
                    }else if("HEIGHT".equals(dic.getLable())){
                        HEIGHT = Integer.parseInt(dic.getCodevalue());
                    }
                }
            }
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/";//存储路径
            data = immsCabinetsService.addImmsCabinets(immsCabinets, uploadRoot,  returnUrl,WIDTH , HEIGHT);
        }else{
            data = immsCabinetsService.updateImmsCabinets(immsCabinets);
        }
        return data;
    }

    @RequestMapping(value = "/delCabinets", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delCabinets(@RequestParam("id") Integer id) {
        logger.info("删除屏柜设备！id:" + id );
        String path = "1_"+id + ".png";
        String url = uploadRoot +"immsCabinets" + "/" + path;
        File file = new File(url);
        if (file.exists() == true){
            file.delete();
        }
        return immsCabinetsService.delDevById(id);
    }

    @RequestMapping("/two/dimension/code/query")
    public String query(HttpServletRequest request, Model model, @RequestParam("id") String id) {
        try {
            //相对路径
            String path = "1_"+id + ".png";
            String module = "immsCabinets";
            String relativePath =  module;

            String url = relativePath;
            path = url+ "/" + path;
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/";//存储路径

            model.addAttribute("path", returnUrl+path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "base/two_dimension_code_view";
    }

    @RequestMapping("/two/dimension/code/create")
    public String createPhoto(HttpServletRequest request, Model model, @RequestParam("id") String id) {
        try {
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/";//存储路径
            int WIDTH=0;
            int HEIGHT=0;
            List<BaseSysdictionary>  dics =  baseSysdictionaryService.getDics( "TWO");
            if(dics !=null && dics.size()>0){
                for(BaseSysdictionary dic:dics){
                    if("WIDTH".equals(dic.getLable())){
                        WIDTH = Integer.parseInt(dic.getCodevalue());
                    }else  if("HEIGHT".equals(dic.getLable())){
                        HEIGHT = Integer.parseInt(dic.getCodevalue());
                    }
                }
            }
            String path = TwoUtils.creatTow("1", id, "immsCabinets",   uploadRoot,  returnUrl,WIDTH,HEIGHT );
            model.addAttribute("path", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "base/two_dimension_code_view";
    }

    @RequestMapping(value = "/deleteCabinetsBatch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteCabinetsBatch(  String ids ) {
        logger.info("===>>>批量删除屏柜设备！ids:" +ids);
        String[] idsAry = ids.split(",");
        for(int i=0;i< idsAry.length;i++){
            String path = "1_"+idsAry[i] + ".png";
            String url = uploadRoot +"immsCabinets" + "/" + path;
            File file = new File(url);
            if (file.exists() == true){
                file.delete();
            }
        }

        Map<String,Object> data =   immsCabinetsService.deleteCabinetsBatch(ids);
        return data;
    }

    @RequestMapping(value="/import" , method = RequestMethod.POST)
    @ResponseBody
    public  String   importExcel(HttpServletRequest request,@RequestParam("multipartFile") MultipartFile multipartFile) {
        try {
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/";//存储路径
            int WIDTH=0;
            int HEIGHT=0;
            List<BaseSysdictionary>  dics =  baseSysdictionaryService.getDics( "TWO");
            if(dics !=null && dics.size()>0){
                for(BaseSysdictionary dic:dics){
                    if("WIDTH".equals(dic.getLable())){
                        WIDTH = Integer.parseInt(dic.getCodevalue());
                    }else  if("HEIGHT".equals(dic.getLable())){
                        HEIGHT = Integer.parseInt(dic.getCodevalue());
                    }
                }
            }
            List<Map<String,Object>> list = immsCabinetsService.saveExcel(multipartFile,  uploadRoot,  returnUrl, WIDTH, HEIGHT);
            Map<String,Object> map=new HashMap<>();
            String msg="<span style='color:red'>导入结果如下：</span><br/><br/>";
            if(list !=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    map = list.get(i);
                    msg +="<span style='color:#2F4056;font-size:14px'>"+map.get("error")+"</span><br/>";
                }
                return msg;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传文件失败");
        }
        return null;
    }


    @RequestMapping(value = "getImmsCabinetsList/export", method = RequestMethod.GET)
    public void export2003(HttpServletRequest request, HttpServletResponse response, ImmsCabinets immsCabinets) {
        List<ImmsCabinets> list = immsCabinetsService.queryImmsCabinetsList(immsCabinets);

        HSSFWorkbook wb = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel_templates/enginecabinets.xls");
            InputStream inputStream =classPathResource.getInputStream();
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
            // 读取了模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            // 在相应的单元格进行赋值
            int rowIndex = 1;
            for (ImmsCabinets isr : list) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (null == row) {
                    row = sheet.createRow(rowIndex);
                }
                HSSFCell cell0 = row.getCell(0);
                if (null == cell0) {
                    cell0 = row.createCell(0);
                }
                cell0.setCellValue(isr.getCabinetName());// 屏柜名称

                HSSFCell cell1 = row.getCell(1);
                if (null == cell1) {
                    cell1 = row.createCell(1);
                }
                cell1.setCellValue(isr.getDevCode());// 编码

                HSSFCell cell2 = row.getCell(2);
                if (null == cell2) {
                    cell2 = row.createCell(2);
                }
                cell2.setCellValue(isr.getDevHeight());// 高度

                HSSFCell cell3 = row.getCell(3);
                if (null == cell3) {
                    cell3 = row.createCell(3);
                }
                cell3.setCellValue(isr.getResponsibilityMan());// 责任人

                HSSFCell cell4 = row.getCell(4);
                if (null == cell4) {
                    cell4 = row.createCell(4);
                }
                cell4.setCellValue(isr.getDevType());//类型

                HSSFCell cell5 = row.getCell(5);
                if (null == cell5) {
                    cell5 = row.createCell(5);
                }
                cell5.setCellValue(isr.getRoomName());//所属机房

                HSSFCell cell6 = row.getCell(6);
                if (null == cell6) {
                    cell6 = row.createCell(6);
                }
                cell6.setCellValue(isr.getLocation());//位置

                HSSFCell cell7 = row.getCell(7);
                if (null == cell7) {
                    cell7 = row.createCell(7);
                }
                cell7.setCellValue(isr.getBelongsPartition());//所属分区

                HSSFCell cell8 = row.getCell(8);
                if (null == cell8) {
                    cell8 = row.createCell(8);
                }
                cell8.setCellValue(isr.getRemarks());//备注

                HSSFCell cell9 = row.getCell(9);
                if (null == cell9) {
                    cell9 = row.createCell(9);
                }
                cell9.setCellValue(isr.getDevDescribe());//描述

                HSSFCell cell10 = row.getCell(10);
                if (null == cell10) {
                    cell10 = row.createCell(10);
                }
                cell10.setCellValue(isr.getCreateTime());//创建时间
                rowIndex++;
            }
            String fileName = "enginecabinets";
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
            ServletOutputStream sout = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(sout);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                logger.error("导出excel出现异常:", e);
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }

        } catch (Exception e) {
            logger.error("导出excel出现异常:", e);
        }

    }

    @RequestMapping(value = "/getImmsCabinetsList/download", method = RequestMethod.GET )
    public void downExcel2( HttpServletResponse response)  {
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel_templates/import_enginecabinets_template.xls");
            InputStream inputStream =classPathResource.getInputStream();

            String fileName = "import_enginecabinets_template";


            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
            OutputStream  bos = response.getOutputStream();
            try {
                int b = 0;
                byte[] buffer = new byte[1000000];
                while (b != -1) {
                    b = inputStream.read(buffer);
                    if(b!=-1) bos.write(buffer, 0, b);
                }

            } catch (Exception e) {
                logger.error("导出excel出现异常:", e);
            } finally {
                if (inputStream != null)
                    inputStream.close();
                if (bos != null)
                    bos.close();
                bos.flush();
            }

        } catch (Exception e) {
            logger.error("导出excel出现异常:", e);
        }
    }

    @RequestMapping(value = "/export/two", method = {RequestMethod.GET })
    public void download (HttpServletRequest request,HttpServletResponse response) throws Exception {
        String path = uploadRoot + "/" + "immsCabinets"+"/";
        String zippath=uploadRoot + "/" + "immsCabinetsZip"+"/";
        File file = new File(zippath);
        if (!file.exists()) {
            file.mkdirs();//创建目录
        }
        FileOutputStream outputStream = new FileOutputStream(new File(zippath+"/"+"immsCabinets.zip"));

        ToZIPUtil.toZip(path, outputStream, true);

        File fileZip = new File(zippath+"/"+"immsCabinets.zip");
        if (!fileZip.exists()) {
            throw new FileNotFoundException("下载文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) fileZip.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + (zippath+"/"+"immsCabinets.zip"));

        OutputStream  bos = response.getOutputStream();
        InputStream inputStream = new FileInputStream(fileZip);
        try {
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if(b!=-1) bos.write(buffer, 0, b);
            }
        } catch (Exception e) {
            logger.error("导出excel出现异常:", e);
        } finally {
            if (outputStream != null){
                outputStream.close();
                bos.flush();
            }
            if (inputStream != null){
                inputStream.close();
            }
            if (bos != null){
                bos.close();
                bos.flush();
            }
        }
    }
    @GetMapping("getImmsRoom")
    @ResponseBody
    public List<ImmsCabinets> getImmsRoom(){
        logger.info("获取机房列表");
        return immsCabinetsService.getImmsRoom();
    }
}

package com.example.imms.web.controller.electric;


import com.example.imms.web.common.utils.*;
import com.example.imms.web.model.*;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.AdminRoleService;
import com.example.imms.web.service.BaseSysdictionaryService;
import com.example.imms.web.service.ImmsSRoomService;
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
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: UserController
 * @Description: 系统用户管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Controller
@RequestMapping("device")
public class DevController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BaseSysdictionaryService baseSysdictionaryService;

    @Autowired
    private AdminRoleService roleService;

    @Autowired
    public ImmsSRoomService immsSRoomService;

    @Value("${uploadRoot}")
    private String uploadRoot;


    @RequestMapping("/devManageInner")
    public String devManageInner(Model model,HttpServletRequest request) {
        model.addAttribute("sid", request.getParameter("id"));
        model.addAttribute("version", request.getParameter("version"));
        return "device/devManageInner";
    }

    @RequestMapping("/devManage")
    public String devManage(Model model) {
        Object roleCode=  SecurityUtils.getSubject().getSession().getAttribute("roleCode");
        model.addAttribute("roleCode",  roleCode==null?"":(String)roleCode);
        return "device/devManage";

    }

    @RequestMapping("/importWarehouse")
    public String importWarehouse() {
        return "device/importWarehouse";
    }

    @RequestMapping("/out")
    public String devManageOut(Model model) {
        Object roleCode=  SecurityUtils.getSubject().getSession().getAttribute("roleCode");
        model.addAttribute("roleCode",  roleCode==null?"":(String)roleCode);
        return "device/devManageOut";
    }


    /**
     * 功能描述: 分页查询用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/getImmsSRoomList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getImmsSRoomList(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ ImmsSRoom immsSRoom
                                         ) {
        immsSRoom.setDevStatus("IN");
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = immsSRoomService.getImmsSRoomList(immsSRoom, pageNum, pageSize);
            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }

    @RequestMapping(value = "/getImmsSRoomOutList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getImmsSRoomOutList(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize, ImmsSRoom immsSRoom
    ) {
        immsSRoom.setDevStatus("OUT");

        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = immsSRoomService.getImmsSRoomList(immsSRoom, pageNum, pageSize);
            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    @RequestMapping(value = "/getImmsSRoomListInner", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getImmsSRoomListInner(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ ImmsSRoom immsSRoom
                                               ) {
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = immsSRoomService.getImmsSRoomList(immsSRoom, pageNum, pageSize);
            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }

    @RequestMapping("/two/dimension/code/query")
    public String query(HttpServletRequest request, Model model, @RequestParam("id") String id) {
        try {
            //相对路径
            String path = "2_"+id + ".png";
            String module = "immsSRoom";
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
            ImmsSRoom immsSRoom = immsSRoomService.queryById(id);
//            String path = this.creatTow(immsSRoom, request);
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
            String path = TwoUtils.creatTow("2", id, "immsSRoom",   uploadRoot,  returnUrl,WIDTH,HEIGHT );
            model.addAttribute("path", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "base/two_dimension_code_view";
    }

    private String creatTow(ImmsSRoom immsSRoom, HttpServletRequest request) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("2_");
        buffer.append(immsSRoom.getId());

        //保存图片  File位置 （全路径）   /upload/fileName.jpg
//        String url = request.getSession().getServletContext().getRealPath(FILE_PATH);


        String fileSptr = "/";
        String module = "immsSRoom";
        String relativePath =  module;

		String url = uploadRoot + fileSptr + relativePath;

        //相对路径
        String path = "2_"+immsSRoom.getId() + ".png";
        File file = new File(url);
        if (!file.exists()) {
            file.mkdirs();
        }
        File upfile = new File(file.getPath() + "/" + path);
//        String note = "设备：" + immsSRoom.getDevModel();
        int WIDTH=0;
        int HEIGHT=0;
            List<BaseSysdictionary>  dics =  baseSysdictionaryService.getDics( "TWO");
            if(dics !=null && dics.size()>0){
                for(BaseSysdictionary dic:dics){
                    if("WIDTH".equals(dic.getLable())){
                        WIDTH = Integer.parseInt(dic.getCodevalue());
                    }else if("HEIGHT".equals(dic.getLable())){
                        HEIGHT = Integer.parseInt(dic.getCodevalue());
                    }
                }
            }
        TwoDimensionCodeUtils.drawLogoQRCode(upfile, buffer.toString(), null,WIDTH, HEIGHT);

        String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/";//存储路径


        path = module+ "/" + path;

        return returnUrl + path;
//        return FILE_PATH + path;
    }

    @RequestMapping(value = "/getImmsSRoomList/export", method = RequestMethod.GET)
     public void export(HttpServletResponse response, ImmsSRoom immsSRoom) throws IOException {
        List<ImmsSRoomExcel> resultList = immsSRoomService.queryImmsSRoomExcelList(immsSRoom);
        long t1 = System.currentTimeMillis();
        ExcelUtils.writeExcel(response, resultList, ImmsSRoomExcel.class);
        long t2 = System.currentTimeMillis();
        System.out.println("=====>>>" + String.format("write over! cost:%sms", (t2 - t1)));
    }

    @RequestMapping(value = "/getImmsSRoomList/export2", method = RequestMethod.GET)
    public void export2003(HttpServletRequest request, HttpServletResponse response, ImmsSRoom immsSRoom, @RequestParam("status") String status) {
        immsSRoom.setDevStatus(status);
        List<ImmsSRoom> list = immsSRoomService.queryImmsSRoomList(immsSRoom);

        HSSFWorkbook wb = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel_templates/warehouse.xls");
            InputStream inputStream =classPathResource.getInputStream();
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
            // 读取了模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            // 在相应的单元格进行赋值
            int rowIndex = 1;
            for (ImmsSRoom isr : list) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (null == row) {
                    row = sheet.createRow(rowIndex);
                }
                HSSFCell cell0 = row.getCell(0);
                if (null == cell0) {
                    cell0 = row.createCell(0);
                }
                cell0.setCellValue(isr.getDevType());// 设备类型

                HSSFCell cell1 = row.getCell(1);
                if (null == cell1) {
                    cell1 = row.createCell(1);
                }
                cell1.setCellValue(isr.getDevManu());// 制造商

                HSSFCell cell2 = row.getCell(2);
                if (null == cell2) {
                    cell2 = row.createCell(2);
                }
                cell2.setCellValue(isr.getDevModel());// 设备型号

                HSSFCell cell3 = row.getCell(3);
                if (null == cell3) {
                    cell3 = row.createCell(3);
                }
                cell3.setCellValue(isr.getDevNumber());// 设备序列号

                HSSFCell cell4 = row.getCell(4);
                if (null == cell4) {
                    cell4 = row.createCell(4);
                }
                cell4.setCellValue(isr.getInRoomTime());// 入库时间

                HSSFCell cell5 = row.getCell(5);
                if (null == cell5) {
                    cell5 = row.createCell(5);
                }
                cell5.setCellValue(isr.getUseType());//类别

                HSSFCell cell6 = row.getCell(6);
                if (null == cell6) {
                    cell6 = row.createCell(6);
                }
                cell6.setCellValue(isr.getReceiver());//接收人

                HSSFCell cell7 = row.getCell(7);
                if (null == cell7) {
                    cell7 = row.createCell(7);
                }
                cell7.setCellValue(isr.getDevAddress()); // 存放地点

                HSSFCell cell8 = row.getCell(8);
                if (null == cell8) {
                    cell8 = row.createCell(8);
                }
                cell8.setCellValue(isr.getResponsibilityDpt());//责任处室

                HSSFCell cell9 = row.getCell(9);
                if (null == cell9) {
                    cell9 = row.createCell(9);
                }
                cell9.setCellValue(isr.getResponsibilityMan());//责任人

                HSSFCell cell10 = row.getCell(10);
                if (null == cell10) {
                    cell10 = row.createCell(10);
                }
                cell10.setCellValue(isr.getProject());//所属项目

                HSSFCell cell11 = row.getCell(11);
                if (null == cell11) {
                    cell11 = row.createCell(11);
                }
                cell11.setCellValue(isr.getTwo());

           /*     HSSFCell cell12 = row.getCell(12);
                if (null == cell12) {
                    cell12 = row.createCell(12);
                }
                cell12.setCellValue(isr.getId());
                */
                rowIndex++;
            }

            String fileName = "warehouse";
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

    @RequestMapping(value = "/getImmsSRoomList/download", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String downExcel()  {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        String filename = "warehouse-2.xls";
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        path = path + "/static/upload/" + filename;
        // 如果文件名不为空，则进行下载
        if (filename != null) {
            File file = new File(path);
            if (!file.exists()) {
                return "下载文件不存在";
            }
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                byte[] buff = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                logger.error("{}", e);
                return "下载失败";
            }
        }
        return "下载成功";
    }

    @RequestMapping(value = "/getImmsSRoomList/download2", method = RequestMethod.GET)
    public void downExcel2( HttpServletResponse response)  {
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel_templates/import_warehouse_template.xls");
            InputStream inputStream =classPathResource.getInputStream();

            String fileName = "import_warehouse_template";

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

    @RequestMapping(value = "/getImmsSRoomList/downExcel2Old", method = RequestMethod.GET)
    public String downExcel2Old()  {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        String filename = "import_warehouse_template.xls";
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        logger.info("=====>>path="+path);
        path = path + "/excel_templates/" + filename;
        logger.info("=====>>path="+path);
        // 如果文件名不为空，则进行下载
        if (filename != null) {
            File file = new File(path);
            if (!file.exists()) {
                return "下载文件不存在";
            }
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                byte[] buff = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                logger.error("{}", e);
                return "下载失败";
            }
        }
        return "下载成功";
    }

    @RequestMapping(value = "/setImmsSRoom", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setUser(HttpServletRequest request,ImmsSRoom immsSRoom) {
        logger.info("设置库房设备[新增或更新]！immsSRoom:" + immsSRoom);
        Map<String,Object> data = new HashMap();
        int WIDTH=0;
        int HEIGHT=0;
        if(immsSRoom.getId() == null || "".equals(immsSRoom.getId())){
            List<BaseSysdictionary>  dics =  baseSysdictionaryService.getDics( "TWO");
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
            data = immsSRoomService.addImmsSRoom(immsSRoom, uploadRoot,  returnUrl,WIDTH , HEIGHT);
        }else{
            data = immsSRoomService.updateImmsSRoom(immsSRoom);
        }
        return data;
    }

    @RequestMapping(value = "/delDev", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delDev(@RequestParam("id") Integer id) {
        logger.info("删除库房设备！id:" + id );
        String path = "2_"+id + ".png";
        String url = uploadRoot +"immsSRoom" + "/" + path;
        File file = new File(url);
        if (file.exists() == true){
            file.delete();
        }
        return immsSRoomService.delDevById(id);
    }

    @RequestMapping(value = "/news", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> news(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize)
    {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> dev = new HashMap<>();
        //简单数据库中新闻
        List<Map<String ,Object>> newsList = new ArrayList<Map<String ,Object>>();
        for (int i = 0; i < 55; i++)
        {
            dev = new HashMap<>();
            dev.put("id",i);
            dev.put("title","admin,新增一条库房数据，序列号：78131231234" + i);
            newsList.add(dev);
        }
        //总页数
        int pages =(int) Math.ceil((double)55 / pageSize);
        data.put("total",pages);
        data.put("rows",newsList);
        return data;
    }

     @RequestMapping("/importOld")
     @ResponseBody
      public void importExcelOld(@RequestParam MultipartFile multipartFile){
         try {
             List<ImmsSRoom> list = ImmsExcelUtils.importExcel(multipartFile);
             if(list !=null && list.size()>0){
                 immsSRoomService.insertBath(list);
             }else{
                 logger.info("=====>>>导入的excel模板不对！");
             }
         } catch (Exception e) {
             e.printStackTrace();
             logger.error("上传文件失败");
         }

     }

    @RequestMapping(value="/import" , method = RequestMethod.POST)
    @ResponseBody
    public  String   importExcel(HttpServletRequest request,@RequestParam("multipartFile") MultipartFile multipartFile) {
        try {
//            return (String) immsSRoomService.saveExcel(multipartFile).get(0).get("error");
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
            List<Map<String,Object>> list = immsSRoomService.saveExcel(multipartFile,  uploadRoot,  returnUrl, WIDTH, HEIGHT);
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

    @RequestMapping(value = "/export/two", method = {RequestMethod.GET })
    public void download (HttpServletRequest request,HttpServletResponse response) throws Exception {
        String path = uploadRoot + "/" + "immsSRoom"+"/";
        String zippath=uploadRoot + "/" + "immsSRoomZip"+"/";
        File file = new File(zippath);
        if (!file.exists()) {
            file.mkdirs();//创建目录
        }
        FileOutputStream outputStream = new FileOutputStream(new File(zippath+"/"+"immsSRoom.zip"));

        ToZIPUtil.toZip(path, outputStream, true);

        File fileZip = new File(zippath+"/"+"immsSRoom.zip");
        if (!fileZip.exists()) {
            throw new FileNotFoundException("下载文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) fileZip.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + (zippath+"/"+"immsSRoom.zip"));

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

    @RequestMapping(value = "/deleteDevBatch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteDevBatch(  String ids ) {
        logger.info("===>>>批量删除库房设备！ids:" +ids);
        String[] idsAry = ids.split(",");
        for(int i=0;i< idsAry.length;i++){
             String path = "2_"+idsAry[i] + ".png";
            String url = uploadRoot +"immsSRoom" + "/" + path;
            File file = new File(url);
            if (file.exists() == true){
                file.delete();
            }
        }

        Map<String,Object> data =   immsSRoomService.deleteDevBatch(ids);
        return data;
    }

}
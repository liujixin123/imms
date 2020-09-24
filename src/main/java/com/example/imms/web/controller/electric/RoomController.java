package com.example.imms.web.controller.electric;

import com.example.imms.web.common.utils.ToZIPUtil;
import com.example.imms.web.common.utils.TwoUtils;
import com.example.imms.web.model.BaseSysdictionary;
import com.example.imms.web.model.ImmsCRoom;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.BaseSysdictionaryService;
import com.example.imms.web.service.ImmsCRoomService;

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
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("room")
public class RoomController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImmsCRoomService immsCRoomService;

    @Autowired
    public BaseSysdictionaryService baseSysdictionaryService;


    @RequestMapping("/importWarehouse")
    public String importWarehouse() {
        return "room/importWarehouse";
    }

    @Value("${uploadRoot}")
    private String uploadRoot;

    @RequestMapping("/roomManage")
    public String userManage(Model model) {
        Object roleCode=  SecurityUtils.getSubject().getSession().getAttribute("roleCode");
        model.addAttribute("roleCode",  roleCode==null?"":(String)roleCode);
        return "room/roomManage";
     }

    @RequestMapping("/roomManageInner")
    public String roomManageInner(Model model,HttpServletRequest request) {
        model.addAttribute("sid", request.getParameter("id"));
        model.addAttribute("version", request.getParameter("version"));
        return "room/roomManageInner";
    }


    @RequestMapping(value = "/getImmsCRoomList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getImmsCRoomList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ ImmsCRoom immsCRoom) {
        /*logger.info("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",pageNum:" + page.getPageNum()
                + ",每页记录数量pageSize:" + page.getPageSize());*/
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = immsCRoomService.getImmsCRoomList(immsCRoom, pageNum ,pageSize);
            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    @RequestMapping(value = "/setImmsCRoom", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setImmsCRoom(HttpServletRequest request,ImmsCRoom immsCRoom) {
        logger.info("设置机房设备[新增或更新]！immsCRoom:" + immsCRoom);
        Map<String,Object> data = new HashMap();
        int WIDTH=0;
        int HEIGHT=0;
        if(immsCRoom.getId() == null || "".equals(immsCRoom.getId())){
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
            data = immsCRoomService.addImmsCRoom(immsCRoom, uploadRoot,  returnUrl,WIDTH , HEIGHT);
        }else{
            data = immsCRoomService.updateImmsCRoom(immsCRoom);
        }
        return data;
    }

    @RequestMapping(value = "/delRoom", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delRoom(@RequestParam("id") Integer id) {
        logger.info("删除机房设备！id:" + id );
        String path = "1_"+id + ".png";
        String url = uploadRoot +"immsCRoom" + "/" + path;
        File file = new File(url);
        if (file.exists() == true){
            file.delete();
        }
        return immsCRoomService.delDevById(id);
    }

    @RequestMapping("/two/dimension/code/query")
    public String query(HttpServletRequest request, Model model, @RequestParam("id") String id) {
        try {
            //相对路径
            String path = "1_"+id + ".png";
            String module = "immsCRoom";
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
            ImmsCRoom immsCRoom = immsCRoomService.queryById(id);
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
            String path = TwoUtils.creatTow("1", id, "immsCRoom",   uploadRoot,  returnUrl,WIDTH,HEIGHT );
            model.addAttribute("path", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "base/two_dimension_code_view";
    }

    @RequestMapping(value = "/deleteRoomBatch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteRoomBatch(  String ids ) {
        logger.info("===>>>批量删除机房设备！ids:" +ids);
        String[] idsAry = ids.split(",");
        for(int i=0;i< idsAry.length;i++){
            String path = "1_"+idsAry[i] + ".png";
            String url = uploadRoot +"immsCRoom" + "/" + path;
            File file = new File(url);
            if (file.exists() == true){
                file.delete();
            }
        }

        Map<String,Object> data =   immsCRoomService.deleteRoomBatch(ids);
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
            List<Map<String,Object>> list = immsCRoomService.saveExcel(multipartFile,  uploadRoot,  returnUrl, WIDTH, HEIGHT);
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


    @RequestMapping(value = "getImmsCRoomList/export", method = RequestMethod.GET)
    public void export2003(HttpServletRequest request, HttpServletResponse response, ImmsCRoom immsCRoom) {
        List<ImmsCRoom> list = immsCRoomService.queryImmsCRoomList(immsCRoom);

        HSSFWorkbook wb = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel_templates/engineroom.xls");
            InputStream inputStream =classPathResource.getInputStream();
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
            // 读取了模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            // 在相应的单元格进行赋值
            int rowIndex = 1;
            for (ImmsCRoom isr : list) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (null == row) {
                    row = sheet.createRow(rowIndex);
                }
                HSSFCell cell0 = row.getCell(0);
                if (null == cell0) {
                    cell0 = row.createCell(0);
                }
                cell0.setCellValue(isr.getDevName());// 设备名称

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
                cell4.setCellValue(isr.getSystem());

                HSSFCell cell5 = row.getCell(5);
                if (null == cell5) {
                    cell5 = row.createCell(5);
                }
                cell5.setCellValue(isr.getSafeArea());

                HSSFCell cell6 = row.getCell(6);
                if (null == cell6) {
                    cell6 = row.createCell(6);
                }
                cell6.setCellValue(isr.getUseTime());//投运日期

                HSSFCell cell7 = row.getCell(7);
                if (null == cell7) {
                    cell7 = row.createCell(7);
                }
                cell7.setCellValue(isr.getResponsibilityDpt());

                HSSFCell cell8 = row.getCell(8);
                if (null == cell8) {
                    cell8 = row.createCell(8);
                }
                cell8.setCellValue(isr.getResponsibilityMan());

                HSSFCell cell9 = row.getCell(9);
                if (null == cell9) {
                    cell9 = row.createCell(9);
                }
                cell9.setCellValue(isr.getMaintainManu());

                HSSFCell cell10 = row.getCell(10);
                if (null == cell10) {
                    cell10 = row.createCell(10);
                }
                cell10.setCellValue(isr.getMaintainMan());

                HSSFCell cell11 = row.getCell(11);
                if (null == cell11) {
                    cell11 = row.createCell(11);
                }
                cell11.setCellValue(isr.getMaintainManPhone());


                HSSFCell cell12 = row.getCell(12);
                if (null == cell12) {
                    cell12 = row.createCell(12);
                }
                cell12.setCellValue(isr.getIp()==null?null:isr.getIp().replaceAll(";","\n"));

                HSSFCell cell13 = row.getCell(13);
                if (null == cell13) {
                    cell13 = row.createCell(13);
                }
                cell13.setCellValue(isr.getRoom());

                HSSFCell cell14 = row.getCell(14);
                if (null == cell14) {
                    cell14 = row.createCell(14);
                }
                cell14.setCellValue(isr.getTwo());

          /*      HSSFCell cell15 = row.getCell(15);
                if (null == cell15) {
                    cell15 = row.createCell(15);
                }
                cell15.setCellValue(isr.getId());
*/
                rowIndex++;
            }

            String fileName = "engineroom";
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

    @RequestMapping(value = "/getImmsCRoomList/download", method = RequestMethod.GET )
    public void downExcel2( HttpServletResponse response)  {
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel_templates/import_engineroom_template.xls");
            InputStream inputStream =classPathResource.getInputStream();

            String fileName = "import_engineroom_template";


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

    @RequestMapping(value = "/getImmsCRoomList/downExcel2Old", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String downExcel2Old()  {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        String filename = "import_engineroom_template.xls";
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        path = path + "/excel_templates/" + filename;
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

    @RequestMapping(value = "/export/two", method = {RequestMethod.GET })
    public void download (HttpServletRequest request,HttpServletResponse response) throws Exception {
        String path = uploadRoot + "/" + "immsCRoom"+"/";
        String zippath=uploadRoot + "/" + "immsCRoomZip"+"/";
        File file = new File(zippath);
        if (!file.exists()) {
            file.mkdirs();//创建目录
        }
        FileOutputStream outputStream = new FileOutputStream(new File(zippath+"/"+"immsCRoom.zip"));

        ToZIPUtil.toZip(path, outputStream, true);

        File fileZip = new File(zippath+"/"+"immsCRoom.zip");
        if (!fileZip.exists()) {
            throw new FileNotFoundException("下载文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) fileZip.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + (zippath+"/"+"immsCRoom.zip"));

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


    @RequestMapping(value = "/export/twoOld", method = {RequestMethod.GET, RequestMethod.POST })
    public String downloadOld (HttpServletRequest request,HttpServletResponse response) throws Exception {
        String path = uploadRoot + "/" + "immsCRoom"+"/";
        String zippath=uploadRoot + "/" + "immsCRoomZip"+"/";
        File file = new File(zippath);
        if (!file.exists()) {
            file.mkdirs();//创建目录
        }
        FileOutputStream outputStream = new FileOutputStream(new File(zippath+"/"+"immsCRoom.zip"));

        ToZIPUtil.toZip(path, outputStream, true);

        File fileZip = new File(zippath+"/"+"immsCRoom.zip");
        if (!fileZip.exists()) {
            return "下载文件不存在";
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) fileZip.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + (zippath+"/"+"immsCRoom.zip"));

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileZip));

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
        return "下载成功！";
    }
}

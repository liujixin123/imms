package com.example.imms.web.controller.equipment;

import com.example.imms.web.common.utils.ToZIPUtil;
import com.example.imms.web.common.utils.TwoUtils;
import com.example.imms.web.model.BaseSysdictionary;
import com.example.imms.web.model.ImmsServer;
import com.example.imms.web.response.PageDataResult;
import com.example.imms.web.service.BaseSysdictionaryService;
import com.example.imms.web.service.ImmsServerService;
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
 * @Description: 服务器管理
 */
@Controller
@RequestMapping("server")
public class ServerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImmsServerService immsServerService;

    @Autowired
    public BaseSysdictionaryService baseSysdictionaryService;


    @RequestMapping("/importServer")
    public String importServer() {
        return "equipment/importServer";
    }

    @Value("${uploadRoot}")
    private String uploadRoot;

    /**
     * 进入服务器管理页面
     * @param model
     * @return
     */
    @RequestMapping("/serverManage")
    public String userManage(Model model) {
        Object roleCode=  SecurityUtils.getSubject().getSession().getAttribute("roleCode");
        model.addAttribute("roleCode",  roleCode==null?"":(String)roleCode);
        return "equipment/serverManage";
     }
    @RequestMapping(value = "/getImmsServerList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getImmsServerList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,ImmsServer immsServer) {
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = immsServerService.getImmsServerList(immsServer, pageNum ,pageSize);
            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    @RequestMapping(value = "/setImmsServer", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setImmsServer(HttpServletRequest request,ImmsServer immsServer) {
        logger.info("设置服务器设备[新增或更新]！immsServer:" + immsServer);
        Map<String,Object> data = new HashMap();
        int WIDTH=0;
        int HEIGHT=0;
        if(immsServer.getId() == null || "".equals(immsServer.getId())){
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
            data = immsServerService.addImmsServer(immsServer, uploadRoot,  returnUrl,WIDTH , HEIGHT);
        }else{
            data = immsServerService.updateImmsServer(immsServer);
        }
        return data;
    }

    @RequestMapping(value = "/delServer", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delServer(@RequestParam("id") Integer id) {
        logger.info("删除服务器设备！id:" + id );
        String path = "1_"+id + ".png";
        String url = uploadRoot +"immsServer" + "/" + path;
        File file = new File(url);
        if (file.exists() == true){
            file.delete();
        }
        return immsServerService.delDevById(id);
    }

    @RequestMapping("/two/dimension/code/query")
    public String query(HttpServletRequest request, Model model, @RequestParam("id") String id) {
        try {
            //相对路径
            String path = "1_"+id + ".png";
            String module = "immsServer";
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
            String path = TwoUtils.creatTow("1", id, "immsServer",   uploadRoot,  returnUrl,WIDTH,HEIGHT );
            model.addAttribute("path", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "base/two_dimension_code_view";
    }

    @RequestMapping(value = "/deleteServerBatch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteServerBatch(  String ids ) {
        logger.info("===>>>批量删除服务器设备！ids:" +ids);
        String[] idsAry = ids.split(",");
        for(int i=0;i< idsAry.length;i++){
            String path = "1_"+idsAry[i] + ".png";
            String url = uploadRoot +"immsServer" + "/" + path;
            File file = new File(url);
            if (file.exists() == true){
                file.delete();
            }
        }

        Map<String,Object> data =   immsServerService.deleteServerBatch(ids);
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
            List<Map<String,Object>> list = immsServerService.saveExcel(multipartFile,  uploadRoot,  returnUrl, WIDTH, HEIGHT);
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


    @RequestMapping(value = "getImmsServerList/export", method = RequestMethod.GET)
    public void export2003(HttpServletRequest request, HttpServletResponse response, ImmsServer immsServer) {
        List<ImmsServer> list = immsServerService.queryImmsServerList(immsServer);

        HSSFWorkbook wb = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel_templates/engineserver.xls");
            InputStream inputStream =classPathResource.getInputStream();
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
            // 读取了模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            // 在相应的单元格进行赋值
            int rowIndex = 1;
            for (ImmsServer isr : list) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (null == row) {
                    row = sheet.createRow(rowIndex);
                }
                HSSFCell cell0 = row.getCell(0);
                if (null == cell0) {
                    cell0 = row.createCell(0);
                }
                cell0.setCellValue(isr.getPowerRate());

                HSSFCell cell1 = row.getCell(1);
                if (null == cell1) {
                    cell1 = row.createCell(1);
                }
                cell1.setCellValue(isr.getPowerNum());

                HSSFCell cell2 = row.getCell(2);
                if (null == cell2) {
                    cell2 = row.createCell(2);
                }
                cell2.setCellValue(isr.getSystemType());

                HSSFCell cell3 = row.getCell(3);
                if (null == cell3) {
                    cell3 = row.createCell(3);
                }
                cell3.setCellValue(isr.getSystemVersion());

                HSSFCell cell4 = row.getCell(4);
                if (null == cell4) {
                    cell4 = row.createCell(4);
                }
                cell4.setCellValue(isr.getCpuNum());

                HSSFCell cell5 = row.getCell(5);
                if (null == cell5) {
                    cell5 = row.createCell(5);
                }
                cell5.setCellValue(isr.getCpuXh());

                HSSFCell cell6 = row.getCell(6);
                if (null == cell6) {
                    cell6 = row.createCell(6);
                }
                cell6.setCellValue(isr.getCpuZp());

                HSSFCell cell7 = row.getCell(7);
                if (null == cell7) {
                    cell7 = row.createCell(7);
                }
                cell7.setCellValue(isr.getCpuHs());

                HSSFCell cell8 = row.getCell(8);
                if (null == cell8) {
                    cell8 = row.createCell(8);
                }
                cell8.setCellValue(isr.getRomNum());

                HSSFCell cell9 = row.getCell(9);
                if (null == cell9) {
                    cell9 = row.createCell(9);
                }
                cell9.setCellValue(isr.getRomSize());

                HSSFCell cell10 = row.getCell(10);
                if (null == cell10) {
                    cell10 = row.createCell(10);
                }
                cell10.setCellValue(isr.getDiskType());
                HSSFCell cell11 = row.getCell(11);
                if (null == cell11) {
                    cell11 = row.createCell(11);
                }
                cell11.setCellValue(isr.getDiskNum());
                HSSFCell cell12 = row.getCell(12);
                if (null == cell12) {
                    cell12 = row.createCell(12);
                }
                cell12.setCellValue(isr.getDiskSize());
                HSSFCell cell13 = row.getCell(13);
                if (null == cell13) {
                    cell13 = row.createCell(13);
                }
                cell13.setCellValue(isr.getHbaNum());
                HSSFCell cell14 = row.getCell(14);
                if (null == cell14) {
                    cell14 = row.createCell(14);
                }
                cell14.setCellValue(isr.getNetcardNum());
                HSSFCell cell15 = row.getCell(15);
                if (null == cell15) {
                    cell15 = row.createCell(15);
                }
                cell15.setCellValue(isr.getRaidDetail());
                HSSFCell cell16 = row.getCell(16);
                if (null == cell16) {
                    cell16 = row.createCell(16);
                }
                cell16.setCellValue(isr.getExternalRom());
                HSSFCell cell17 = row.getCell(17);
                if (null == cell17) {
                    cell17 = row.createCell(17);
                }
                cell17.setCellValue(isr.getDoubleVersion());
                HSSFCell cell18 = row.getCell(18);
                if (null == cell18) {
                    cell18 = row.createCell(18);
                }
                cell18.setCellValue(isr.getGks());
                HSSFCell cell19 = row.getCell(19);
                if (null == cell19) {
                    cell19 = row.createCell(19);
                }
                cell19.setCellValue(isr.getServerPwd());
                HSSFCell cell20 = row.getCell(20);
                if (null == cell20) {
                    cell20 = row.createCell(20);
                }
                cell20.setCellValue(isr.getRoomName());
                HSSFCell cell21 = row.getCell(21);
                if (null == cell21) {
                    cell21 = row.createCell(21);
                }
                cell21.setCellValue(isr.getRemarks());
                HSSFCell cell22 = row.getCell(22);
                if (null == cell22) {
                    cell22 = row.createCell(22);
                }
                cell22.setCellValue(isr.getCreateTime());
                rowIndex++;
            }
            String fileName = "engineserver";
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

    @RequestMapping(value = "/getImmsServerList/download", method = RequestMethod.GET )
    public void downExcel2( HttpServletResponse response)  {
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel_templates/import_engineserver_template.xls");
            InputStream inputStream =classPathResource.getInputStream();

            String fileName = "import_engineserver_template";


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
        String path = uploadRoot + "/" + "immsServer"+"/";
        String zippath=uploadRoot + "/" + "immsServerZip"+"/";
        File file = new File(zippath);
        if (!file.exists()) {
            file.mkdirs();//创建目录
        }
        FileOutputStream outputStream = new FileOutputStream(new File(zippath+"/"+"immsServer.zip"));

        ToZIPUtil.toZip(path, outputStream, true);

        File fileZip = new File(zippath+"/"+"immsServer.zip");
        if (!fileZip.exists()) {
            throw new FileNotFoundException("下载文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) fileZip.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + (zippath+"/"+"immsServer.zip"));

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
    public List<ImmsServer> getImmsRoom(){
        logger.info("获取机房列表");
        return immsServerService.getImmsRoom();
    }
}

package com.example.imms.web.common.utils;

import java.io.File;

public class TwoUtils {

    public static String creatTow(String type, String id, String module, String uploadRoot,String returnUrl,  int WIDTH, int HEIGHT ) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(type+"_");
        buffer.append(id);
        String picContent =  buffer.toString();

         String url = uploadRoot +  "/" + module;
        //相对路径
        String filename = type+"_"+ id+ ".png";
        File file = new File(url);
        if (!file.exists()) {
            file.mkdirs();
        }
        File upfile = new File(file.getPath() + "/" + filename);
        TwoDimensionCodeUtils.drawLogoQRCode(upfile, picContent, null,WIDTH, HEIGHT);

        return returnUrl + module+ "/" + filename;
    }
}

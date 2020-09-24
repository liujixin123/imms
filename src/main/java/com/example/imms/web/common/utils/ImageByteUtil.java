package com.example.imms.web.common.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ImageByteUtil{
    private static float QUALITY = 0.6f;
    public static void compressZip(List<File> listfiles, OutputStream output,  boolean compress,String alias){
        ZipOutputStream zipStream = null;
        try {
            zipStream = new ZipOutputStream(output);
            for (File file : listfiles){
                compressZip(file, zipStream, compress,alias+"_"+(listfiles.indexOf(file)+1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (zipStream != null) {
                    zipStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void compressZip(File file, ZipOutputStream zipStream,
                                    boolean compress,String alias) throws Exception{
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            //zip(input, zipStream, file.getName(), compress);
            zip(input, zipStream, alias+"."+file.getName().substring(file.getName().lastIndexOf(".")+1), compress);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void zip(InputStream input, ZipOutputStream zipStream,
                            String zipEntryName, boolean compress) throws Exception{
        byte[] bytes = null;
        BufferedInputStream bufferStream = null;
        try {
            if(input == null)
                throw new Exception("获取压缩的数据项失败! 数据项名为：" + zipEntryName);
            // 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样
            ZipEntry zipEntry = new ZipEntry("图片/"+zipEntryName);
            // 定位到该压缩条目位置，开始写入文件到压缩包中
            zipStream.putNextEntry(zipEntry);
            if (compress) {
                bytes = ImageByteUtil.compressOfQuality(input, 0);
                zipStream.write(bytes, 0, bytes.length);
            } else {
                bytes = new byte[1024 * 5];// 读写缓冲区
                bufferStream = new BufferedInputStream(input);// 输入缓冲流
                int read = 0;
                while ((read = bufferStream.read(bytes)) != -1) {
                    zipStream.write(bytes, 0, read);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferStream)
                    bufferStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] compressOfQuality(File file, float quality) throws Exception{
        byte[] bs = null;
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            bs = compressOfQuality(input,quality);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bs;
    }

    public static byte[] compressOfQuality(InputStream input, float quality)
            throws Exception {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            if(quality == 0){
                Thumbnails.of(input).scale(1f).outputQuality(QUALITY)
                        .toOutputStream(output);
            } else {
                Thumbnails.of(input).scale(1f).outputQuality(quality).toOutputStream(output);
            }
            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
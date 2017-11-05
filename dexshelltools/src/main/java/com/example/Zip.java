package com.example;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * function :
 * Created by 韦国旺 on 2017/10/27.
 * Copyright (c) 2017 All Rights Reserved.
 */


public class Zip {

    public static void unZip(File zip, File dir) {
        try {
            dir.delete();
            ZipFile zipFile = new ZipFile(zip);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                if (name.equals("META-INF/CERT.RSA")
                        || name.equals("META-INF/CERT.SF")
                        || name.equals("META-INF/MANIFEST.MF")) {
                    continue;

                }
                if (!zipEntry.isDirectory()) {
                    File file = new File(dir, name);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    InputStream is = zipFile.getInputStream(zipEntry);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    is.close();
                    fos.close();
                }
            }
            zipFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static void zip(File apkDir, File unsignedApk) {
//    }


    public static void zip(File srcFile, File desFile) throws IOException {
        GZIPOutputStream zos = null;
        FileInputStream fis = null;
        try {
            //创建压缩输出流,将目标文件传入
            zos = new GZIPOutputStream(new FileOutputStream(desFile));
            //创建文件输入流,将源文件传入
            fis = new FileInputStream(srcFile);
            byte[] buffer = new byte[1024];
            int len = -1;
            //利用IO流写入写出的形式将源文件写入到目标文件中进行压缩
            while ((len = (fis.read(buffer))) != -1) {
                zos.write(buffer, 0, len);
            }
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (fis != null) {
                fis.close();
            }

        }
    }
}

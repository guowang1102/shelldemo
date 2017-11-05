package com.github.shell;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * function :
 * Created by 韦国旺 on 2017/10/26.
 * Copyright (c) 2017 All Rights Reserved.
 */


public class MyApplication extends Application {
    //解密新的dex
    //1 找到dex  2解壳  3 解密得到所有的dex 运行所有的dex
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);

        AES.init(AES.DEFAULT_PWD);
        File apkFile = new File(getApplicationInfo().sourceDir);
        File unZipFile = getDir("fake.apk", MODE_PRIVATE);
        File app = new File(unZipFile, "app");


        if (!app.exists()) {
            Zip.unZip(apkFile,app);
            File[] files = app.listFiles();
            for (File file : files) {
                String name = file.getName();
                if (name.equals("classes.dex")) {
                    try {
                        byte[] bytes = Utils.getBytes(file);
                        byte[] len = new byte[4];
                        System.arraycopy(bytes, bytes.length - 4, len, 0, 4);
                        int mainLen = Utils.bytes2Int(len);
                        byte[] mainDex = new byte[mainLen];
                        System.arraycopy(bytes, bytes.length - 4 - mainLen, mainDex, 0, mainLen);

                        byte[] decryptResult = AES.decrypt(mainDex);
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(decryptResult);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                } else if (name.endsWith(".dex")) {

                    try {
                        byte[] bytes = Utils.getBytes(file);
                        byte[] decryptResult = AES.decrypt(bytes);
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(decryptResult);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        //loading
        List<File> list = new ArrayList();
        for (File file : app.listFiles()) {
            if (file.getName().equals("classes.dex")) {
                list.add(file);
            }
        }
        try {
            AppUtils.install(getClassLoader(), unZipFile, list);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

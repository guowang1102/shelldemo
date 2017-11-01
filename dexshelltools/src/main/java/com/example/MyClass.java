package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class MyClass {

    public static void main(String[] args) throws IOException, InterruptedException {

        File aarFile = new File("dexshelltools/resource/shell-release.aar");
        File fakeFile = new File("dex/resource/tmp", "fakeDex");
        //Zip.unZip(aarFile,fakeFile)  //解压

        File[] files = fakeFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.equals("classes.jar");
            }
        });

        if (files == null || files.length <= 0) {
            throw new RuntimeException("the aar is invalidate");
        }

        File classes_jar = files[0];

        File aarDex = new File(classes_jar.getParentFile(), "classes.dex");
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd /C dx --dex --output=" + aarDex.getAbsolutePath() + " " + classes_jar.getAbsolutePath());


        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }

        if (process.exitValue() != 0) {
            throw new RuntimeException("dx run failed");
        }
        process.destroy();

        AES.init(AES.DEFAULT_PWD);
        File apkFile = new File("dexshelltools/resource/app-debug.apk");
        File apkDir = new File("dexshelltools/resource/tmp", "apk");
        File[] dexFiles = apkDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".dex");
            }
        });

        File mainDexFile = null;
        byte[] mainDexData = null;
        for (File dexFile : dexFiles) {
            byte[] buffer = new byte[0];
            try {
                buffer = Utils.getBytes(dexFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] encryptBytes = AES.encrypt(buffer);
            if (dexFile.getName().endsWith("classes.dex")){
                mainDexData = encryptBytes;
                mainDexFile = dexFile;
            }


            FileOutputStream fos = new FileOutputStream(dexFile);
            fos.write(encryptBytes);
            fos.flush();
            fos.close();
        }

        System.out.print("create new dex");
        byte[] aarData = new byte[0];
        try {
            aarData = Utils.getBytes(aarDex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] newDex = new byte[mainDexData.length+aarData.length+4];

        System.out.print("new main dex length:"+newDex.length);


        System.arraycopy(aarData,0,newDex,0,aarData.length);
        System.arraycopy(mainDexData,0,newDex,aarData.length,mainDexData.length);

        //拷贝main dex的长度
        //Utils.changeFileSize(mainDexData,newDex,aarData);
        //Utils.changeSignature(newDex)
        //Utils.changeCheckSum(newDex)

        FileOutputStream fos = new FileOutputStream(mainDexFile);
        fos.write(newDex);
        fos.flush();
        fos.close();

        File unsignedApk = new File("dexshelltools/resource/outputs/apk-unsigned.apk");
        unsignedApk.getParentFile().mkdirs();
//        zip.zip(apkDir,unsignedApk);

        File signedApk = new File("dexshelltools/resource/outputs/apk-signed.apk");
//        Signature.signature(unsignedApk,signedApk);

    }

}

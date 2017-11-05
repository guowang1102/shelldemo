package com.github.shell;

import java.io.File;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

/**
 * function :
 * Created by 韦国旺 on 2017/10/27.
 * Copyright (c) 2017 All Rights Reserved.
 */


public class Utils {


    public static byte[] getBytes(File dexFile) throws Exception{
        RandomAccessFile fis = new RandomAccessFile(dexFile,"r");
        byte[] buffer = new byte[(int)fis.length()];
        fis.readFully(buffer);
        fis.close();
        return buffer;
    }

    public static int bytes2Int( byte[] src){
        int value;
        value = (int)((src[0]&0xFF)
                |((src[1]&0xFF)<<8)
                |((src[2]&0xFF)<<16)
                |((src[3]&0xFF)<<24)
        );
        return value;
    }

    public static void changeSignature(byte[] newDex) throws NoSuchAlgorithmException{
        System.out.print("更换dex文件 签名信息");
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(newDex,32,newDex.length-32);
        byte[] sha1 = md.digest();
        System.arraycopy(sha1,0,newDex,12,20);
        System.out.print("更换dex文件 checkSum");
    }

    public static void changeCheckSum(byte[] newDex){
        Adler32 adler32 = new Adler32();
        adler32.update(newDex,12,newDex.length-12);
        int value = (int) adler32.getValue();
        byte[] checkSum = Utils.int2Bytes(value);
        System.arraycopy(checkSum,0,newDex,8,4);
    }



    public static void changeFileSize(byte[] mainDexData,byte[] newDex,byte[] aarData){
        byte[] bytes = Utils.int2Bytes(mainDexData.length);
        System.out.print("拷贝原来dex长度到新的额dex"+Utils.bytes2Int(bytes));
        System.arraycopy(Utils.int2Bytes(mainDexData.length),0,newDex,aarData.length+mainDexData.length,4);

        System.out.println("更换dex 文件投长度信息...");
    }

    public static byte[] int2Bytes(int value)
    {
        return new byte[]{
                (byte) ((value >> 24) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF)
        };
    }


}

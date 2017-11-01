package com.example;

import java.io.File;
import java.io.RandomAccessFile;

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
}

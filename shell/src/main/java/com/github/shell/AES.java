package com.github.shell;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * function : AEC加解密
 * Created by 韦国旺 on 2017/10/27.
 * Copyright (c) 2017 All Rights Reserved.
 */


public class AES {

    public static final String DEFAULT_PWD = "abcdefghijklmnop";
    private static final String algorithmStr = "AES/ECB/PKCS5Padding";

    private static Cipher encryptCipher;
    private static Cipher decryptCipher;

    public static void init(String password){
            try {
                encryptCipher = Cipher.getInstance(algorithmStr);
                decryptCipher = Cipher.getInstance(algorithmStr);
                byte[] keyStr = password.getBytes();
                SecretKeySpec key = new SecretKeySpec(keyStr,"AES");
                encryptCipher.init(Cipher.ENCRYPT_MODE,key);
                decryptCipher.init(Cipher.DECRYPT_MODE,key);
            }  catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static byte[] encrypt(byte[] content){
        byte[] result = new byte[0];
        try {
            result = encryptCipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] decrypt(byte[] content){
        byte[] result = new byte[0];
        try {
            result = decryptCipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}

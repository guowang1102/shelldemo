package com.github.shell;

import java.io.File;
import java.io.IOException;

/**
 * Created by WeiGuoWang on 2017/10/27.
 */

public class Signature {

    public static void signature(File unsignedApk,File signedApk) throws InterruptedException,IOException{
        String cmd[] = {"cmd" ,"/C","jarsigner","-sigalg","MD5withRSA",
                "-digestalg","SHA1",
                "-keystore","dexshelltools/resource/keystore/debug.keystore",
                "-storepass","android",
                "-keypass","android",
                "-signerjar",signedApk.getAbsolutePath(),
                unsignedApk.getAbsolutePath(),


        };
    }
}

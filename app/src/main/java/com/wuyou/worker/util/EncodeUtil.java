package com.wuyou.worker.util;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Solang on 2018/5/29.
 */

public class EncodeUtil {
    /**
     * @param str 需要加密的文字
     * @param sign
     * @return 加密后的文字
     * @throws Exception 加密失败
     */
    public static String get3DES(final String str, String sign) throws Exception
    {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(sign.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory
                .getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS7Padding");
        IvParameterSpec ips = new IvParameterSpec(sign.getBytes(),0,8);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(str.getBytes("utf-8"));
        return Base64.encodeToString(encryptData, Base64.NO_WRAP);
    }

//    /**
//     * 3DES解密
//     *
//     * @param encryptText 加密文本
//     * @return
//     * @throws Exception
//     */
//    public static String decode3DES(String encryptText) throws Exception {
//        Key deskey = null;
//        DESedeKeySpec spec = new DESedeKeySpec(Constant.secretKey.getBytes());
//        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Constant.algorithm);
//        deskey = keyfactory.generateSecret(spec);
//        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//        IvParameterSpec ips = new IvParameterSpec(Constant.secretKey.getBytes());
//        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
//        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
//
//        return new String(decryptData, Constant.encoding);
//    }
}

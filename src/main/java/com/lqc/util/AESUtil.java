package com.lqc.util;

import cn.hutool.crypto.symmetric.AES;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;

/**
 * author : liqinchao
 * CreateTime : 2019/3/1 18:50
 * Description :AES加解密工具类
 */
public class AESUtil {

    /**
     * 加密方式
     */
    public static String KEY_ALGORITHM = "AES";
    /**
     * 数据填充方式
     */
    public static String algorithmStr = "AES/CBC/PKCS7Padding";
    /**
     * 避免重复new生成多个BouncyCastleProvider对象，因为GC回收不了，会造成内存溢出
     * 只在第一次调用decrypt()方法时才new 对象
     */
    public static boolean initialized = false;

    /**
     * @param originalContent
     * @param encryptKey
     * @param ivByte
     * @return
     */
    public static byte[] encrypt(byte[] originalContent, byte[] encryptKey, byte[] ivByte) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance(algorithmStr);
            SecretKeySpec skeySpec = new SecretKeySpec(encryptKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(ivByte));
            byte[] encrypted = cipher.doFinal(originalContent);
            return encrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        byte[] key = "4b93dc38a9bdca37ce4bb8aa03b69efe".getBytes();
        String mode = "ECB";
        String padding = "PKCS7Padding";
        String content = "Zhang123456";
        String encryptStr = encrypt(mode, padding, key, content);
        decrypt(mode, padding, key, encryptStr);
    }

    static String encrypt(String mode, String padding, byte[] key, String content) {
        //构建
        AES aes = new AES(mode, padding, key);
        // 加密
        String encryptStr = aes.encryptBase64(content);
        System.out.println("AA======" + encryptStr);
        return encryptStr;
    }

    static String decrypt(String mode, String padding, byte[] key, String encryptStr) {
        //构建
        AES aes = new AES(mode, padding, key);
        // 解密
        String decryptStr = aes.decryptStr(encryptStr);
        System.out.println("BB======" + decryptStr);
        return decryptStr;
    }

    /**
     * AES解密
     * 填充模式AES/CBC/PKCS7Padding
     * 解密模式128
     *
     * @param content 目标密文
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     */
    public static byte[] decrypt(byte[] content, byte[] aesKey, byte[] ivByte) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance(algorithmStr);
            Key sKeySpec = new SecretKeySpec(aesKey, "AES");
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BouncyCastle作为安全提供，防止我们加密解密时候因为jdk内置的不支持改模式运行报错。
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    /**
     * 生成iv
     *
     * @param iv
     * @return
     * @throws Exception
     */
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }
}

package com.may.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName : RSAUtils
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : RSA加密工具类
 */
public class RSAUtils {
    /**
     * 生成公钥和私钥,返回字符串类型的key map
     *
     * @return Map<String, String>
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> generateKeyString() throws NoSuchAlgorithmException {
        Map<String, String> map = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom((System.currentTimeMillis() + "").getBytes());
        keyPairGen.initialize(2048, secureRandom);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String pub = new String(Base64.encode(publicKey.getEncoded()));
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String prv = new String(Base64.encode(privateKey.getEncoded()));
        map.put("public", pub);
        map.put("private", prv);
        return map;
    }

    /**
     * 生成公钥和私钥,返回对象类型的key map
     *
     * @return Map<String, Object>
     * @throws NoSuchAlgorithmException
     */
    public static HashMap<String, Object> generateKeyObject() throws NoSuchAlgorithmException {
        HashMap<String, Object> map = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom((System.currentTimeMillis() + "").getBytes());
        keyPairGen.initialize(2048, secureRandom);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，
     * 不同JDK默认的补位方式可能不同，如Android默认是RSA/None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey generatePublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用base64字符串生成RSA公钥
     *
     * @return
     */
    public static RSAPublicKey generatePublicKey(String base64Key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(base64Key.getBytes()));
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey generatePrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用base64字符串生成RSA公钥
     *
     * @return
     */
    public static RSAPrivateKey generatePrivateKey(String base64Key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(base64Key.getBytes()));
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长
        String[] datas = splitString(data, key_len);
        String mi = "";
        //如果明文长度大于模长则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, PublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String mi = bcd2Str(cipher.doFinal(data.getBytes()));
        return mi;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
//        System.err.println(bcd.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, PrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = data.getBytes();
        String ming = new String(cipher.doFinal(bytes));
        return ming;
    }

    /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int strLen = string.length();
        len = len / 3;//中文字符长度为3
        int y = strLen % len;
        int x = strLen / len;
        x = (y > 0) ? ++x : x;
        String[] strings = new String[x];
        for (int i = 0; i < x; i++) {
            String str = "";
            if (i == x - 1 && y > 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static void main(String[] args) throws Exception {
//        HashMap<String, Object> map = RSAUtils.generateKeyObject();
//        //生成公钥和私钥
//        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
//        String pub = new String(Base64.encode(publicKey.getEncoded()));
//        System.out.println("公钥：" + pub);
//        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
//        String prv = new String(Base64.encode(privateKey.getEncoded()));
//        System.out.println("私钥：" + prv);
////        //模
////        String modulus = publicKey.getModulus().toString();
////        //公钥指数
////        String public_exponent = publicKey.getPublicExponent().toString();
////        //私钥指数
////        String private_exponent = privateKey.getPrivateExponent().toString();
//        //使用模和指数生成公钥和私钥
////        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
////        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
//        RSAPublicKey pubKey = RSAUtils.generatePublicKey(pub);
//        RSAPrivateKey priKey = RSAUtils.generatePrivateKey(prv);
//        //明文
////        String org = "事实上，这大概是人类已经分解的最大整数（232个十进制位，768个二进制位）。比它更大的因数分解，还没有被报道过，因此目前被破解的最长RSA密钥就是768位。八、加密和解密有了公钥和密钥，就能进行加密和解密了。（1）加密要用公钥 (n,e)假设鲍勃要向爱丽丝发送加密信息m，他就要用爱丽丝的公钥 (n,e) 对m进行加密。这里需要注意，m必须是整数（字符串可以取ascii值或unicode值），且m必须小于n所谓\"加密\"，就是算出下式的c：爱丽丝的公钥是 (3233, 17)，鲍勃的m假设是65，那么可以算出下面的等式：于是，c等于2790，鲍勃就把2790发给了爱丽丝。（2）解密要用私钥(n,d)爱丽丝拿到鲍勃发来的2790以后，就用自己的私钥(3233, 2753) 进行解密。可以证明，下面的等式一定成立：也就是说，c的d次方除以n的余数为m。现在，c等于2790，私钥是(3233, 2753)，那么，爱丽丝算出27902753 ≡ 65 (mod 3233)因此，爱丽丝知道了鲍勃加密前的原文就是65。至此，\"加密--解密\"的整个过程全部完成。我们可以看到，如果不知道d，就没有办法从c求出m。而前面已经说过，要知道d就必须分解n，这是极难做到的，所以RSA算法保证了通信安全。你可能会问，公钥(n,e) 只能加密小于n的整数m，那么如果要加密大于n的整数，该怎么办？有两种解决方法：一种是把长信息分割成若干段短消息，每段分别加密；另一种是先选择一种\"对称性加密算法\"（比如DES），用这种算法的密钥加密信息，再用RSA公钥加密DES密钥。";
//        String org = "事实上。";
//        //加密后的密文
//        String mi = RSAUtils.encryptByPublicKey(org, pubKey);
//        System.err.println("密文：" + mi);
//        //解密后的明文
//        String ming = RSAUtils.decryptByPrivateKey(mi, priKey);
//        System.err.println("明文:" + ming);
        Map<String, String> keyMap = generateKeyString();
        System.out.println("公钥：" + keyMap.get("public"));
        System.out.println("私钥：" + keyMap.get("private"));
    }

}

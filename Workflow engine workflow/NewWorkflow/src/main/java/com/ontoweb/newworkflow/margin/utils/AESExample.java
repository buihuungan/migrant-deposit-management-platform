package com.ontoweb.newworkflow.margin.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESExample {

    /** 加密模式之 ECB，算法/模式/补码方式 */
    private static final String AES_ECB = "AES/ECB/PKCS5Padding";

    /***
     * <h2>空校验</h2>
     * @param str 需要判断的值
     */
    public static boolean isEmpty(Object str) {
        return null == str || "".equals(str);
    }

    /***
     * <h2>String 转 byte</h2>
     * @param str 需要转换的字符串
     */
    public static byte[] getBytes(String str){
        if (isEmpty(str)) {
            return null;
        }

        try {
            return str.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * <h2>获取一个 AES 密钥规范</h2>
     */
    public static SecretKeySpec getSecretKeySpec(String key){
        SecretKeySpec secretKeySpec = new SecretKeySpec(getBytes(key), "AES");
        return secretKeySpec;
    }

    /**
     * <h2>加密 - 模式 ECB</h2>
     * @param text 需要加密的文本内容
     * @param key 加密的密钥 key
     * */
    public static byte[] encrypt(String text, String key){
        if (isEmpty(text) || isEmpty(key)) {
            return null;
        }

        try {
            // 创建AES加密器
            Cipher cipher = Cipher.getInstance(AES_ECB);
            SecretKeySpec secretKeySpec = getSecretKeySpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // 加密字节数组
            byte[] encryptedBytes = cipher.doFinal(getBytes(text));
            return encryptedBytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h2>解密 - 模式 ECB</h2>
     * @param text 需要解密的文本内容
     * @param key 解密的密钥 key
     * */
    public static String decrypt(byte[] text, String key){

        try {
            Cipher cipher = Cipher.getInstance(AES_ECB);
            SecretKeySpec secretKeySpec = getSecretKeySpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(text);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

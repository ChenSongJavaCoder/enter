package com.cs.common.util;

import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * 加密
 * RSA 公钥 -> Base64.encode
 * 解密
 * Base64.decode -> RSA 私钥
 */

/**
 * RSA公钥/私钥/签名工具包
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 *
 * @author cs
 */
public class RSAUtils {
    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * 常量0
     */
    private static final int ZERO = 0;
    /**
     * RSA最大加密明文最大大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文最大大小
     * 当密钥位数为1024时,解密密文最大是 128
     * 当密钥位数为2048时需要改为 256 不然会报错（Decryption error）
     */
    private static final int MAX_DECRYPT_BLOCK = 256;
    /**
     * 默认key大小
     */
    private static final int DEFAULT_KEY_SIZE = 1024;

    private static final String PRI_KEY =
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQD1fwdcXwiAdDbW\n" +
                    "GCQKSyyuT2diVZEGkA9/JGmXCA6i1925YkU2ffqz0UsntrAy7D9t+jWzgLwRw1Nl\n" +
                    "0qsx+ch/c8d6AV1Yh7n4Ntr1O/CPq5bxaTG031Dw0L5Len5MOLq05xkdyLNAMlw5\n" +
                    "cRtOv5HFUbj5kufa1Z7tVzIAAY1XS2CPEvzQktrB8Cuk4Be4o/cCazC/pXCowPTC\n" +
                    "QBvbjpk5HPwTx6qU+0K8TZ9SyQcKuYkM5M0eZpe1bVFkuNkE0hcPeb4iqtzVhWjh\n" +
                    "vY0h5V0K56qwUvW8TS83pVDMAVofieepx6kuw/Dq3gQ/YCXNSsz3Ong4LQcwEw1e\n" +
                    "Ko2qTGrfAgMBAAECggEAM7CedSWRXbKahTOqCvzPGJihdQP9ODuG98dL8EADDOiU\n" +
                    "x97YIeQesFPmFiOSL/asWFSxj+0QLFFLMwpLf4dPXv6axqaY5/YUAx5Rtgb73NRN\n" +
                    "QOFcVPAcU8gv+SZ+hz/+l+58zFeg3mh8IKAfFtKhN8HAR9RVuy71epW8cmHhR7qu\n" +
                    "tEs6EqLauMpjD0KkbI5Zb5r1AH+kYU7o/t2VDGy0kdcAnWEodeMeuDUwiz0Q11x0\n" +
                    "LQI8KRoHeBsmdGYzLsyNNLX9LhqnbqCxSBnYdXWuugKSqyrVedOHJOxoIUnHMm8Z\n" +
                    "1lcxJcM+iES1++Y2528N3bHvYHqh5+pHQkG+4W7qwQKBgQD8ukjcexSVCuL6wSQp\n" +
                    "TisVuVL5OWmdd6x87XpSQb1b6l1aUiOPGAOFbtr873C9cILRfyLMeWi5W+bSo1wd\n" +
                    "tCMaCx/TgoTbukDZz2msN/sBGwCaAtPDCl0EsfDxbVBXv3Jk0j+FQfsdCTGsxXXg\n" +
                    "EttyaBXBmjofhe7r0ehiurVbPwKBgQD4rMYmPG5GF7luYLAVF+HwxwANA1r+pD7t\n" +
                    "mpUlRDkAVdyoTFcsxyDNb87S/I4bB15fEgIFxvT9dYJKvmG63rReTJbhcMnA5H/y\n" +
                    "Gzrz58eDz6wOlg5odxwGE+CeRF4lkJAYMkm43J5NBFxu6iuGDggIHlA8M+exPcGs\n" +
                    "B3Zx5O0oYQKBgQDqqzc4s3jbJC67VFh1mkXbeCgZjzwIobUik4h/lNd139srIRYv\n" +
                    "qsaqQdNKBjOTVEPEr97qKtoZM8LTWCkS08+8QRQeXBVtyjapiTTAW4LQ9ffLClED\n" +
                    "zD2vPGNUaoKt1/EquSeOX0QDJCRiH0Bi/l+Sjh+Vk9Xgq4a8pj64XlJEFQKBgQD4\n" +
                    "J7I1RejlmZc6RtJwujyGgo/SbJ7Jri39/l0Hq2UWqJhieowj6zMYz5KdidplrpzG\n" +
                    "Uroh+cFRjYpP/FyaltN3pwaKQnuTOnNTr0jNTWcUapFQnP4T0Yjtp7hnN+Kbqf+h\n" +
                    "pc1b5Q0z3j4/kP+N70Kkl1uKOGHlxbMXcPsJ/WGNAQKBgCU0ul2aD2rFL44K/heS\n" +
                    "czNrwJ7llJixkPIQpU5+tzDHAbjrxEXATqpWvLW7rfThdgjr3o040dqJa04EhKC3\n" +
                    "gc9wK5yXnKnsaWlMcw5sFRUlXr9tPcQnqbOfr7xF9xa3dCVQElqrNFDstM1Daa2n\n" +
                    "5j+loN4fK3H/8OyYYw0BLmLL";


    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        return initKey(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey(int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }


    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return encrypt(data, KeyFactory.getInstance(KEY_ALGORITHM), keyFactory.generatePublic(x509KeySpec));
    }


    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        return encrypt(data, keyFactory, privateK);
    }


    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        privateKey = PRI_KEY;
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return decrypt(encryptedData, keyFactory, keyFactory.generatePrivate(pkcs8KeySpec));
    }


    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        return decrypt(encryptedData, keyFactory, publicK);

    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.toBase64String(signature.sign());
    }


    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign));
    }


    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.toBase64String(key.getEncoded());
    }


    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.toBase64String(key.getEncoded());
    }

    /**
     * 解密公共方法
     */
    private static byte[] decrypt(byte[] data, KeyFactory keyFactory, Key key) throws Exception {

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        return encryptAndDecrypt(data, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * 加密公共方法
     */
    private static byte[] encrypt(byte[] data, KeyFactory keyFactory, Key key) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptAndDecrypt(data, cipher, MAX_ENCRYPT_BLOCK);
    }


    /**
     * 加密解密分段处理公共方法
     */
    private static byte[] encryptAndDecrypt(byte[] data, Cipher cipher, int maxSize) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = ZERO;
        byte[] cache;
        int i = ZERO;
        // 对数据分段加密
        while (inputLen - offSet > ZERO) {
            if (inputLen - offSet > maxSize) {
                cache = cipher.doFinal(data, offSet, maxSize);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, ZERO, cache.length);
            i++;
            offSet = i * maxSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

}
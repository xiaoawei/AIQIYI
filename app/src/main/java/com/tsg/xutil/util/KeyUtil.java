package com.tsg.xutil.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.security.*;

/**
 * Created by a on 2017/6/2.
 */
public class KeyUtil {

    private static final String AESTYPE = "AES/ECB/PKCS5Padding";
    public static final String AESKEY = "UITN25LMUQC436IM";

    public static String encryptWithAes(String keyStr, String plainText) throws Exception {
        Key key = generateKey(keyStr);
        Cipher cipher = Cipher.getInstance(AESTYPE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypt = cipher.doFinal(HexStringToByteArray(plainText));
        return ByteArrayToHexString(encrypt);
    }

    private static Key generateKey(String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        return keySpec;
    }

    public static String decryptForAes(String keyStr, String encryptData) throws Exception {
        Key key = generateKey(keyStr);
        Cipher cipher = Cipher.getInstance(AESTYPE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypt = cipher.doFinal(HexStringToByteArray(encryptData));
        return ByteArrayToHexString(decrypt);
    }

    /**
     * 字节数组，转成十六进制字串
     *
     * @param bytes 传入一个byte数组
     * @return 返回一个byte数组所对应的字符串
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 十六进制字串转数组
     *
     * @param s 传入一个十六进制的字符串
     * @return 返回十六进制字串所对应的字节数组
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


}

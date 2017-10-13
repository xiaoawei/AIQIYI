package com.tsg.xutil.util.net;

import java.io.UnsupportedEncodingException;

/**
 * url转码、解码
 */
public class UrlUtil {
    private final static String ENCODE = "UTF-8";

    /**
     * URL 解码
     *
     * @return String
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL 转码
     *
     * @return String
     */
    public static String getURLEncoderString(String str) {
       String result = "";
      /*   if (null == str) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (str.contains("http://")) {
            result = str.substring(str.indexOf("http://") + 7);
            stringBuffer.append("http://");
        }
        if (str.contains("https://")) {
            result = str.substring(str.indexOf("https://") + 8);
            stringBuffer.append("https://");
        }
        String[] results = result.split("/");
        for (int i = 0; i < results.length; i++) {
            try {
                if (i != results.length - 1) {
                    stringBuffer.append(java.net.URLEncoder.encode(results[i], ENCODE) + "/");
                } else {
                    stringBuffer.append(java.net.URLEncoder.encode(results[i], ENCODE));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
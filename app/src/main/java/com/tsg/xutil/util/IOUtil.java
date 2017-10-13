package com.tsg.xutil.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by xiaoAwei on 2017/10/11.
 */
public class IOUtil {
    /**
     * 　　* 将一个字符串转化为输入流
     */
    public static InputStream getStringStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")) {
            try {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
                return tInputStringStream;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}

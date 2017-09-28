package com.tsg.xutil.util;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * 文本工具类
 */
public class TextUtil {

    /**
     * 字符串不能空
     *
     * @param value 要验证的字符串
     * @return
     */
    public static boolean stringIsNotNull(String value) {
        return value != null && value.trim().length() > 0;
    }

    /**
     * 字符串为空
     *
     * @param value 要验证的字符串
     * @return
     */
    public static boolean stringIsNull(String value) {
        return value == null || value.trim().length() <= 0;
    }

    /**
     * 字符串的值是否为 0
     *
     * @param value 要验证的字符串
     * @return
     */
    public static boolean stringIsLing(String value) {
        return value.equals("0") || value.equals("0.0") || value.equals("0.") || value.equals("0.00");
    }

    /**
     * 字符串的值是否没超过100亿
     *
     * @param value 要验证的字符串
     * @return
     */
    public static boolean stringIsNormalLength(String value) {
        value = value.trim();
        BigDecimal bd = new BigDecimal(value);
        value = NumberUtil.getDecimalStr(Double.parseDouble(value));
        if (value.contains(".")) {
            value = value.substring(0, value.indexOf("."));
        }
        if (value.length() > 0 && value.length() <= 11) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取文本内容的长度，中文算一个字符，英文算半个字符，包括标点符号
     *
     * @param str
     * @return
     */
    public static int getTextLengthes(String str) {
        int number = getTextLength(str);
        int length = number / 2;
        if (number % 2 != 0) {
            length += 1;
        }
        str = null;
        return length;
    }

    /**
     * 获取文本内容的长度(中文算两个字符，英文算一个字符)
     *
     * @param str
     * @return
     */
    public static int getTextLength(String str) {
        int length = 0;
        try {
            str = new String(str.getBytes("GBK"), "ISO8859_1");
            length = str.length();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return length;
    }

    public static String F = "%.2f";

    /**
     * 加法
     *
     * @param a
     * @param b
     * @return
     */
    public static String addition(String a, String b) {
        if (TextUtil.stringIsNull(a)) {
            return b;
        }
        if (TextUtil.stringIsNull(b)) {
            return a;
        }
        double value = Double.parseDouble(a) + Double.parseDouble(b);
        return format(value);
    }

    /**
     * @param value
     * @return
     * @author caibing.zhang
     * @createdate 2013-4-10 下午6:41:45
     * @Description: 格式化，保留二位小数
     */
    public static String format(double value) {
        return String.format(F, value);
    }

    /**
     * 验证金额
     *
     * @param str
     * @return
     */
    public static boolean isSum(String str) {
        String s = "^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,7})?$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(s); // 判断小数点后一位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }


}

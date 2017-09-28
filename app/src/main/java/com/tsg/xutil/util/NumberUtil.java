package com.tsg.xutil.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by xiaoAwei on 2017/9/7.
 */
public class NumberUtil {
    public static String getDecimalStr(double number) {
        DecimalFormat df = new DecimalFormat("#.00");
        BigDecimal bigDecimal = new BigDecimal(df.format(number));
        return bigDecimal.toString();
    }

    public static Long getTwoMult(String s1) {
        s1 = new BigDecimal(s1).toPlainString();
        if (!s1.contains(".")) {
            s1 = s1 + ".0";
        }
        System.out.println(s1);
        int m = 0;
        String s2 = "" + 1000.0;
        m += s1.split("\\.")[1].length();
        m += s2.split("\\.")[1].length();
        long num1 = Long.parseLong(s1.replace(".", ""));
        long num2 = Long.parseLong(s2.replace(".", ""));

        return new BigDecimal("" + num1 * num2 / Math.pow(10, m)).longValue();
    }

    /**
     * 贴现率专用
     *
     * @param s1
     * @return
     */
    public static String getBankRate(String s1) {
        s1 = new BigDecimal(s1).toPlainString();
        if (!s1.contains(".")) {
            s1 = s1 + ".0";
        }
        System.out.println(s1);
        int m = 0;
        String s2 = "" + 100.0;
        m += s1.split("\\.")[1].length();
        m += s2.split("\\.")[1].length();
        long num1 = Long.parseLong(s1.replace(".", ""));
        long num2 = Long.parseLong(s2.replace(".", ""));

        String resultValue = new BigDecimal("" + num1 * num2 / Math.pow(10, m)).stripTrailingZeros().toPlainString();
        return resultValue + "%";
    }

}

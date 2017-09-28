package com.tsg.xutil.util;

/**
 * Created by xiaoAwei on 2017/9/5.
 */
public class BankNumUtil {
    /**
     * 根据信用卡格式添加空格，每4位添加一个空格
     *
     * @param content
     * @return
     */
    public static String addSpeaceByCredit(String content) {
        content = getNumberStr(content);
        StringBuilder newString = new StringBuilder();
        for (int i = 1; i <= content.length(); i++) {
            if (i % 4 == 0 && i != content.length()) {//每4位添加一个空格
                newString.append(content.charAt(i - 1) + " ");
            } else {//正常添加
                newString.append(content.charAt(i - 1));
            }
        }
        return newString.toString();
    }

    // 判断一个字符串是否都为数字
    public boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    //提取数字
    public static String getNumberStr(String content) {
        content = content.trim();
        String str2 = "";
        if (content != null && !"".equals(content)) {
            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) >= 48 && content.charAt(i) <= 57) {
                    str2 += content.charAt(i);
                }
            }
        }
        return str2;
    }
}

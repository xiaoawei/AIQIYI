package com.tsg.xutil.util;

import android.content.Context;
import android.text.TextUtils;


import com.tsg.xutil.constant.Constant;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/20.
 */

public class LoginUtil {

    public static boolean isLogin(Context context) {
        if ("".equals(SPUtil.getStringData(context, Constant.Token))) {
            return false;
        }
        return true;
    }

    /**
     * 手机号的正则
     *
     * @param username
     * @return
     */
    public static boolean userMatch(String username) {
        return Pattern.compile("^[1][3,4,5,7,8]{1}[0-9]{9}$").matcher(username).matches();
    }

    /**
     * 六位验证码的正则
     *
     * @param verfyCode
     * @return
     */
    public static boolean verfyCodeMatch(String verfyCode) {
        return Pattern.compile("^[0-9]{6}$").matcher(verfyCode).matches();
    }

    /**
     * 6-15位的密码的正则
     *
     * @param password
     * @return
     */
    public static boolean passMatch(String password) {
//        return ((password.length() > 5) && (password.length() < 16)) && Pattern.compile("^[a-zA-Z0-9]{6,15}$").matcher(password).matches();
        if ((password.length() > 5) && (password.length() < 16)) {
            return true;
        } else {
            return false;
        }
    }

    public static String checkPhoneNum(String phoneNumStr) {
        if (!TextUtils.isEmpty(phoneNumStr.trim())) {
            if (userMatch(phoneNumStr.trim())) {
                return "OK";
            } else {
                return "请输入正确的手机号";
            }
        } else {
            return "手机号不能为空";
        }
    }

    public static String checkUserAndPass(String username, String password) {
        if (!TextUtils.isEmpty(username.trim()) && !TextUtils.isEmpty(password.trim())) {
            if (userMatch(username.trim())) {
                if (passMatch(password)) {
                    return "OK";
                } else {
                    return "密码须为6-15位字符";
                }
            } else {
                return "请输入正确的手机号";
            }
        } else {
            return "用户名或密码不能为空";
        }
    }

    public static String checkTwoPass(String password, String passwordAgain) {
        if (!TextUtils.isEmpty(password.trim()) || !TextUtils.isEmpty(passwordAgain.trim())) {
            if (password.equals(passwordAgain)) {
                if (passMatch(password.trim()) && passMatch(passwordAgain.trim())) {
                    return "OK";
                } else {
                    return "密码须为6-15位字符";
                }
            } else {
                return "两次密码输入不一致";
            }
        } else {
            return "密码不能为空";
        }
    }

    /**
     * 判断用户名和验证码的格式是否正确
     *
     * @param username
     * @param verfyCode
     * @return
     */
    public static String checkUserAndVerfyCode(String username, String verfyCode) {
        if (!TextUtils.isEmpty(username.trim()) && !TextUtils.isEmpty(verfyCode.trim())) {
            if (userMatch(username.trim())) {
                if (verfyCodeMatch(verfyCode)) {
                    return "OK";
                } else {
                    return "请输入6位数字的验证码";
                }
            } else {
                return "请输入正确的手机号";
            }
        } else {
            return "用户名或验证码不能为空";
        }
    }

    /**
     * 判断注册信息的格式是否正确
     *
     * @param username
     * @param verfyCode
     * @return
     */
    public static String checkRegisterInfo(String username, String verfyCode, String password, String passwordAgain) {
        if (TextUtils.isEmpty(username.trim())) {
            return "用户名不能为空";
        } else if (!userMatch(username.trim())) {
            return "请输入正确的手机号";
        } else if (TextUtils.isEmpty(verfyCode.trim())) {
            return "验证码不能为空";
        } else if (!verfyCodeMatch(verfyCode)) {
            return "请输入6位数字的验证码";
        } else if (TextUtils.isEmpty(password.trim())) {
            return "密码不能为空";
        } else if (!password.equals(passwordAgain)) {
            return "两次密码输入不一致";
        } else if (!passMatch(password.trim())) {
            return "密码须为6-15位字符";
        } else {
            return "OK";
        }
    }
}

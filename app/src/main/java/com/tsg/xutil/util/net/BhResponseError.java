package com.tsg.xutil.util.net;

import android.content.Context;

import com.tsg.xutil.util.T;

import org.json.JSONException;
import org.json.JSONObject;


public class BhResponseError {
    private int statusCode;
    private String message = "";

    public BhResponseError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    /**
     * 获取错误异常消息体
     *
     * @return
     */
    public String getMessage() {
        try {
            JSONObject jsonObject = new JSONObject(message);
            message = jsonObject.getString("error");
            if (message.equals("Token错误")) {
                message = "会话过期,请重新登录";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 获取网络状态码
     *
     * @return
     */
    public int getStatusCode() {
        return statusCode;
    }

    public void showToast(Context context) {
        T.showShort(context, getMessage());
    }

}

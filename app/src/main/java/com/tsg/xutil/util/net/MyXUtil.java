package com.tsg.xutil.util.net;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.common.reflect.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsg.xutil.base.CoreApplication;
import com.tsg.xutil.util.DensityUtil;
import com.tsg.xutil.util.L;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

/**
 * Created by xiaoAwei on 2017/9/18.
 */
public abstract class MyXUtil {

    private final Context context;
    private ProgressDialog dialog;
    private Gson gson;

    public MyXUtil(Context context) {
        this.context = context;
    }

    /**
     * 网络加载成功(200-299)
     */
    public abstract void loadSuccess(Object responseInfo);


    /**
     * 网络加载失败，服务器异常
     */
    public abstract void loadError(BhResponseError error);

    /**
     * 网络完成时，都会走的方法
     */
    public abstract void finish();

    public void get(String url, Map map, boolean jsonStr, final Class<?> dataClass, boolean showDialog, String dialogMessage) {
        RequestParams params = initNet(url, map, jsonStr, showDialog, dialogMessage);
        L.e("我是get请求：" + params.getUri());
        L.e("提交的内容：" + params.getBodyContent());
        params.addHeader("Cookie", "__uuid=20c034b8-efaa-c82a-0788-5eafd10ab9a9; player_forcedType=h5_VOD; QC005=cca97bcd391a3e18d36b5b28a6d3a615; P00004=2093986194.1495773490.e37b56ffb1; QC118=%7B%22color%22%3A%22FFFFFF%22%2C%22channelConfig%22%3A0%2C%22hadTip%22%3A1%2C%22isOpen%22%3A0%2C%22speed%22%3A18%2C%22density%22%3A40%2C%22opacity%22%3A68%2C%22isFilterColorFont%22%3A0%2C%22proofShield%22%3A1%2C%22forcedFontSize%22%3A24%2C%22isFilterImage%22%3A0%2C%22isset%22%3A1%7D; QC159=%7B%22color%22%3A%22FFFFFF%22%2C%22channelConfig%22%3A1%2C%22speed%22%3A10%2C%22density%22%3A30%2C%22opacity%22%3A68%2C%22isFilterColorFont%22%3A1%2C%22proofShield%22%3A0%2C%22forcedFontSize%22%3A24%2C%22isFilterImage%22%3A1%2C%22isOpen%22%3A0%2C%22isset%22%3A1%2C%22hadTip%22%3A1%7D; QP001=1; T00700=EgcIkMDtIRAB; QC001=1; T00404=b0d90a98e76f34d01bbe11d28da10ba2; QC008=1493177890.1506057064.1506057065.24; JSESSIONID=aaauJEACesK4B4FQ6Ta6v; QC010=11567351; Hm_lvt_53b7374a63c37483e5dd97d78d9bb36e=1505905323,1506057065; Hm_lpvt_53b7374a63c37483e5dd97d78d9bb36e=1506079258; QC007=DIRECT; QC006=suh7oqre46taviavwshc574v; QC021=%5B%7B%22key%22%3A%22%E9%BE%99%E7%8F%A0%E8%B6%85%22%7D%5D; QC124=1%7C0; __dfp=a0f023c041ce37449bb8dbee45e158e5acc0aa694544355b7475d8f7639768ed3a@1508497332163@1505905332163");
        params.addHeader("Upgrade-Insecure-Requests","1");
        params.addHeader("Host", "so.iqiyi.com");
        params.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                parseSuccess(result, dataClass);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                parseError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                L.e("网络请求取消 cancelled");
            }

            @Override
            public void onFinished() {
                parseFinish();
            }
        });
    }

    public void post(String url, Map map, boolean jsonStr, final Class<?> dataClass, boolean showDialog, String dialogMessage) {
        RequestParams params = initNet(url, map, jsonStr, showDialog, dialogMessage);
        L.e("我是post请求：" + params.getUri());
        L.e("提交的内容：" + params.getBodyContent());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                parseSuccess(result, dataClass);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                parseError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                L.e("网络请求取消 cancelled");
            }

            @Override
            public void onFinished() {
                parseFinish();
            }
        });
    }

    /**
     * 网络加载初始化
     *
     * @param url
     * @param map
     * @param jsonStr
     * @param showDialog
     * @param dialogMessage
     * @return
     */
    private RequestParams initNet(String url, Map map, boolean jsonStr, boolean showDialog, String dialogMessage) {
        if (showDialog) {
            dialog = showNetDialog(context, dialogMessage);
        }
        if (map == null) {
            map = new HashMap();
        }
        RequestParams params = new RequestParams(url);
        SSLContext sslContext = MySSLContext.getSSLContext();
        params.setSslSocketFactory(sslContext.getSocketFactory()); // 设置ssl
        params.setConnectTimeout(1000 * 40);//设置超时时间40s
        if (jsonStr) {
            JSONObject jsonObject = new JSONObject();
            for (Object entry : map.entrySet()) {
                Map.Entry<Object, Object> myEntry = (Map.Entry<Object, Object>) entry;
                try {
                    jsonObject.put("" + myEntry.getKey(), myEntry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
            params.setAsJsonContent(jsonStr);
            params.setBodyContent(jsonObject.toString());
        } else {
            for (Object entry : map.entrySet()) {
                Map.Entry<Object, Object> myEntry = (Map.Entry<Object, Object>) entry;
                params.addBodyParameter("" + myEntry.getKey(), "" + myEntry.getValue());
            }
        }
        return params;
    }

    /**
     * 网络成功的解析与处理
     *
     * @param result
     * @param dataClass
     */
    private void parseSuccess(String result, Class<?> dataClass) {
//        L.e("响应: " + result);
        try {
            if (dataClass != null) {
                Object data = getGson().fromJson(result, dataClass);
                loadSuccess(data);
            } else {
                loadSuccess(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            L.e("我是走的是Success解析异常");
        }
    }

    /**
     * 网络结束后，都会走的方法
     */
    private void parseFinish() {
        if (dialog != null) {
            dialog.dismiss();
        }
        finish();
        L.e("结束啦");
    }

    /**
     * 网络错误的解析与处理
     *
     * @param ex
     */
    private void parseError(Throwable ex) {
        if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            try {
                String responseMsg = httpEx.getMessage();
                String errorResult = httpEx.getResult();
                JSONObject jsonObject = new JSONObject(errorResult);
                loadError(new BhResponseError(responseCode, jsonObject.getString("error")));
                L.e("responseMsg: " + responseMsg + "\nstatusCode: " + responseCode + "\nerrorResult: " + jsonObject.getString("error"));
            } catch (JSONException e) {
                e.printStackTrace();
                L.e("statusCode: " + responseCode + "\n我是走的是Error解析异常");
            }
        } else { // 其他错误
            // ...
        }
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }

    /**
     * ProgressDialog
     */
    public ProgressDialog showNetDialog(Context context, String messageBody) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage(messageBody);
        pd.getWindow().setLayout(DensityUtil.dp2px(context, 30), DensityUtil.dp2px(context, 30));
        pd.show();
        return pd;
    }
}

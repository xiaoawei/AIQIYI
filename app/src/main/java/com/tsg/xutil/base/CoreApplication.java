package com.tsg.xutil.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsg.xutil.util.SPNameUtil;
import com.tsg.xutil.util.TextUtil;

import org.xutils.x;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class CoreApplication extends Application {

    private static Context context;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        context = CoreApplication.this;
        init();
    }

    public static Context getContext() {
        return context;
    }

    /**
     * @Description: 初始化
     */
    private void init() {
        x.Ext.init(this);
        x.Ext.setDebug(false);
        x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        String getHostString = SPNameUtil.getStringData(this, "host");
        if (TextUtil.stringIsNull(getHostString)) {
        } else {
//            RequestApi.host = getHostString;
        }


    }

    public Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

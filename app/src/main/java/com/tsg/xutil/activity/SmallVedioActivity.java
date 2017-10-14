package com.tsg.xutil.activity;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;

import com.tsg.xutil.R;
import com.tsg.xutil.adapter.JiAdapter;
import com.tsg.xutil.base.BaseActivity;
import com.tsg.xutil.base.BaseFragment;
import com.tsg.xutil.bean.VideoInfo;
import com.tsg.xutil.constant.Constant;
import com.tsg.xutil.constant.RequestApi;
import com.tsg.xutil.fragment.HomeFragment;
import com.tsg.xutil.fragment.MineFragment;
import com.tsg.xutil.fragment.SearchFragment;
import com.tsg.xutil.util.ADFilterTool;
import com.tsg.xutil.util.L;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class SmallVedioActivity extends BaseActivity {

    @ViewInject(R.id.smallVideo_webView_ri005)
    private WebView webView;
    private ArrayList fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reSetContentView(R.layout.activity_small_vedio);
        setActionBarTitle("小视频");
        x.view().inject(this);
        init();
    }

    private void init() {
        webView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        webView.setBackgroundColor(getResources().getColor(android.R.color.black));
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setDomStorageEnabled(true);
        }
//        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        VideoInfo videoInfo = (VideoInfo) getIntent().getSerializableExtra(Constant.VIDEOBEAN);
        //加载需要显示的网页
        videoInfo.getJiList();
        loadCookiesUrl(webView, RequestApi.host_smallVideo);
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {  //表示按返回键时的操作
                        webView.goBack();   //后退
                        //webview.goForward();//前进
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        //设置Web视图
        webView.setWebViewClient(new HelloWebViewClient());
    }

    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            L.e(url);
            loadCookiesUrl(view, url);
            return false;
        }

        @Override
        public void onLoadResource(WebView webView, String s) {
            L.e("onLoadResource:  " + s);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            L.e("shouldInterceptRequest:  " + url);
            url = url.toLowerCase();
            if (!ADFilterTool.hasAd(SmallVedioActivity.this, url)) {
                return super.shouldInterceptRequest(view, url);
            } else {
                return new WebResourceResponse(null, null, null);
            }

        }
    }

    private void loadCookiesUrl(WebView webView, String urlStr) {
        String[] cookies = "UM_distinctid=15f1937653ba2-000b951c483c7c-464a0129-100200-15f1937653c1f9; PHPSESSID=7oqiodchjarjqc3r73uibg2sn2; kt_qparams=id%3D33255%26dir%3Dmv6; video_log=33255%3A1507966184%3B45723%3A1507966394%3B; CNZZDATA1260629125=929730047-1507955254-%7C1507965364; kt_tcookie=1; __atuvc=3%7C41; __atuvs=59e1bda9ea2d87a5002; kt_is_visited=1".split(";");
        syncCookie(this, urlStr, cookies);
        webView.loadUrl(urlStr);
    }

    /**
     * 同步一下cookie
     */
    // 设置cookie
    public static void syncCookie(Context context, String urlStr, String[] cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        for (int i = 0; i < cookies.length; i++) {
            cookieManager.setCookie(urlStr, cookies[i]);
        }
        CookieSyncManager.getInstance().sync();
    }

    @Event(R.id.smallVideo_webView_goBack)
    private void goBack(View view) {
        webView.goBack();
    }

    @Event(R.id.smallVideo_webView_flush)
    private void flush(View view) {
        webView.reload();
    }

    @Event(R.id.smallVideo_webView_goForward)
    private void goForword(View view) {
        webView.goForward();
    }

}

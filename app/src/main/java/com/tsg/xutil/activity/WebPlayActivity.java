package com.tsg.xutil.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tsg.xutil.R;
import com.tsg.xutil.adapter.JiAdapter;
import com.tsg.xutil.base.BaseActivity;
import com.tsg.xutil.bean.VideoInfo;
import com.tsg.xutil.constant.Constant;
import com.tsg.xutil.constant.RequestApi;
import com.tsg.xutil.util.ADFilterTool;
import com.tsg.xutil.util.DensityUtil;
import com.tsg.xutil.util.L;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


public class WebPlayActivity extends BaseActivity {

    @ViewInject(R.id.WebPlayActivity_webView)
    private WebView webViewPlayer;
    @ViewInject(R.id.WebPlayActivity_full_layout)
    private RelativeLayout fullLayout;
    @ViewInject(R.id.WebPlayActivity_full_img)
    private ImageView fullScreen;
    @ViewInject(R.id.WebPlayActivity_gridView)
    private GridView gridView;
    private JiAdapter jiAdapter;
    private String mCurrentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reSetContentView(R.layout.activity_web_play);
        setActionBarTitle("播放页");
        x.view().inject(this);
        init();
    }

    private void init() {
        webViewPlayer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        webViewPlayer.setBackgroundColor(getResources().getColor(android.R.color.black));
        webViewPlayer.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webViewPlayer.getSettings().setPluginState(WebSettings.PluginState.ON);
            webViewPlayer.getSettings().setAllowFileAccessFromFileURLs(true);
            webViewPlayer.getSettings().setDomStorageEnabled(true);
        }
//        webViewPlayer.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        VideoInfo videoInfo = (VideoInfo) getIntent().getSerializableExtra(Constant.VIDEOBEAN);
        //加载需要显示的网页
        videoInfo.getJiList();
//        webViewPlayer.loadUrl(RequestApi.host + videoInfo.getJiList().get(0));
        loadCookiesUrl(webViewPlayer, RequestApi.host + videoInfo.getJiList().get(0));
        mCurrentUrl = RequestApi.host + videoInfo.getJiList().get(0);
        jiAdapter = new JiAdapter(this, videoInfo.getJiList());
        gridView.setAdapter(jiAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href = (String) jiAdapter.getData().get(position);
//                webViewPlayer.loadUrl(RequestApi.host + href);
                loadCookiesUrl(webViewPlayer, RequestApi.host + href);
                mCurrentUrl = RequestApi.host + href;
//                jumpChrom(mCurrentUrl);
            }
        });
        //设置Web视图
        webViewPlayer.setWebViewClient(new HelloWebViewClient());
    }

    @Event(R.id.WebPlayActivity_webView)
    private void webPlayBt(View view) {
        if (fullScreen.getVisibility() == View.GONE) {
            fullScreen.setVisibility(View.VISIBLE);
        } else {
            fullScreen.setVisibility(View.GONE);
        }
    }

    @Event(R.id.WebPlayActivity_full_img)
    private void fullScreenBt(View view) {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(this, 200));
            fullLayout.setLayoutParams(layoutParams);
            setStatusBar(this, true);
            baseLayout.setVisibility(View.VISIBLE);
            baseStatus.setVisibility(View.VISIBLE);
        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            fullLayout.setLayoutParams(layoutParams);
            setStatusBar(this, false);
            baseLayout.setVisibility(View.GONE);
            baseStatus.setVisibility(View.GONE);
        }
    }

    protected void setStatusBar(Activity activity, boolean isShow) {
        Window window = activity.getWindow();
        if (isShow) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View statusBarView = mContentView.getChildAt(0);
        //移除假的 View
        if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == getStatusBarHeight()) {
            mContentView.removeView(statusBarView);
        }
        //不预留空间
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
        }

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
            url = mCurrentUrl.substring(mCurrentUrl.lastIndexOf("http://"));
            L.e("url : " + url);
            view.loadUrl("javascript:WMXZ('/A/index.php?url=" + url + "')");
            view.loadUrl("javascript:WMXZ('/A/index.php?url=" + url + "')");
//            view.loadUrl("javascript:function setTop(){document.getElementById('yyjmww15343259').style.display=\"none\";}setTop();");
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            L.e("shouldInterceptRequest:  " + url);
            url = url.toLowerCase();
            if (!mCurrentUrl.contains(url)) {
                if (!ADFilterTool.hasAd(WebPlayActivity.this, url)) {
                    return super.shouldInterceptRequest(view, url);
                } else {
                    return new WebResourceResponse(null, null, null);
                }
            } else {
                return super.shouldInterceptRequest(view, url);
            }

        }
    }

    private void loadCookiesUrl(WebView webView, String urlStr) {
        String[] cookies = "RK=gtmihH27cS; pgv_pvi=7100999680; tvfe_boss_uuid=56071fd07b47a754; _gscu_661903259=95705206tsevk818; pac_uid=1_1085581171; mobileUV=1_15ce750bbe2_4a29e; _ga=GA1.2.347278918.1502416471; tvfe_search_uid=0d9ae87c-6ea4-4ebc-ba8c-8eb7cd03d61f; ts_refer=www.baidu.com/link; pgv_si=s5657426944; _qpsvr_localtk=0.5306263165368099; pt2gguin=o1085581171; uin=o1085581171; skey=@876zfi5fJ; luin=o1085581171; lskey=00010000b85fdf44aefd2d8182cf10f79dc1c05db1773414628a9567621a26725587d7eed9b9f04eab948da7; ptcz=5a4c87910091e26b84a4901a5b8fbdd2a1be98247a047254ab2e5cd22287cef5; login_remember=qq; main_login=qq; encuin=ad8e68c1e89b1e4b28019ebd7c7bd47c|1085581171; lw_nick=%E5%B0%8F%E9%98%BF%E5%A8%81|1085581171|//thirdqq.qlogo.cn/g?b=sdk&k=icaDAWTCgjL3woVgz8ib1tIA&s=40&t=1483375703|1; ptag=www_baidu_com|x; o_cookie=1085581171; qv_als=ywoS40kYdL5pudwSA11507526120uspLbQ==; pgv_info=ssid=s1182061250;".split(";");
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
//        cookieManager.setCookie(urlStr, cookies);//cookies是在HttpClient中获得的cookie
        for (int i = 0; i < cookies.length; i++) {
            cookieManager.setCookie(urlStr, cookies[i]);
        }
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void onBackPressed() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(this, 200));
            fullLayout.setLayoutParams(layoutParams);
            setStatusBar(this, true);
            baseLayout.setVisibility(View.VISIBLE);
            baseStatus.setVisibility(View.VISIBLE);
        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            super.onBackPressed();
        }
    }

    private void jumpChrom(String flash_url) {
        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(flash_url));
        startActivity(intent2);
    }
}

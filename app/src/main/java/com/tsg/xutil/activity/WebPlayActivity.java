package com.tsg.xutil.activity;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tsg.xutil.R;
import com.tsg.xutil.adapter.JiAdapter;
import com.tsg.xutil.base.BaseActivity;
import com.tsg.xutil.bean.VideoInfo;
import com.tsg.xutil.constant.Constant;
import com.tsg.xutil.constant.RequestApi;
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
        webViewPlayer.loadUrl(RequestApi.host + videoInfo.getJiList().get(0));
        mCurrentUrl = RequestApi.host + videoInfo.getJiList().get(0);
        jiAdapter = new JiAdapter(this, videoInfo.getJiList());
        gridView.setAdapter(jiAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href = (String) jiAdapter.getData().get(position);
                webViewPlayer.loadUrl(RequestApi.host + href);
                mCurrentUrl = RequestApi.host + href;
                jumpChrom(mCurrentUrl);
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
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            url = mCurrentUrl.substring(mCurrentUrl.lastIndexOf("http://"));
            L.e("url : " + url);
            view.loadUrl("javascript:WMXZ('/A/index.php?url=" + url + "')");
        }

        /*@Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
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

        }*/
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

package com.tsg.xutil.base;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tsg.xutil.MainActivity;
import com.tsg.xutil.R;
import com.tsg.xutil.util.CoreUtil;
import com.tsg.xutil.util.DensityUtil;

import java.lang.reflect.Field;

/**
 * Created by xiaoAwei on 2017/3/29.
 */
public class BaseActivity extends AppCompatActivity {

    public BaseFragment mPreFragmentContent;
    public BaseFragment mFragmentContent;
    protected CoreApplication myApplication;
    public TextView tvTitle;
    public ActionBar mActionBar;
    public ImageView left;
    public TextView rightText;
    public RelativeLayout relativeLayout;
    public ImageView rightImg;
    public RelativeLayout baseLayout;
    public TextView baseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        myApplication = getMyApplication();
        relativeLayout = findView(R.id.baseView);
        setStatusBar(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置屏幕方向
        CoreUtil.addAppActivity(this);//所有实例装进list集合
        mActionBar = getSupportActionBar();
        mActionBar.hide();
        left = (ImageView) findViewById(R.id.actionbar_left);
        tvTitle = (TextView) findViewById(R.id.actionbar_title);
        rightText = (TextView) findViewById(R.id.actionbar_righttext);
        rightImg = (ImageView) findViewById(R.id.actionbar_rightimg);
        baseStatus = (TextView) findViewById(R.id.baseStatus);
        baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        rightText.setVisibility(View.INVISIBLE);
        rightImg.setVisibility(View.INVISIBLE);
        if (this instanceof MainActivity) {   //首页不需要返回键
            left.setVisibility(View.INVISIBLE);
        } else {
            if (getSupportActionBar() != null) {
                left.setVisibility(View.VISIBLE);
            }
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();
            }
        });
        rightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightImgClick();
            }
        });
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightTextClick();
            }
        });
    }

    public CoreApplication getMyApplication() {
        return (CoreApplication) getApplication();
    }

    /**
     * 切换Fragment
     *
     * @param to
     */
    public void switchFragmentContent(int id, BaseFragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mFragmentContent != null) {
            if (mFragmentContent != to) {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.hide(mFragmentContent).add(id, to); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.hide(mFragmentContent).show(to); // 隐藏当前的fragment，显示下一个
                }
            }
        } else {
            transaction.add(id, to);
        }
        transaction.commitAllowingStateLoss();  //推荐使用此方法，更安全，更方便
        mPreFragmentContent = mFragmentContent;
        mFragmentContent = to;
    }

    /**
     * replace即将显示的Fragment
     *
     * @param to
     */
    public void replaceFragmentContent(int id, BaseFragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (mFragmentContent != null) {
            if (mFragmentContent != to) {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.hide(mFragmentContent).add(id, to); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.remove(to); // 如果add过，就先移除了，再添加
                    transaction.hide(mFragmentContent).add(id, to);
                }
            }
        } else {
            transaction.add(id, to);
        }
        transaction.commitAllowingStateLoss();  //推荐使用此方法，更安全，更方便
        mFragmentContent = to;
    }

    /**
     * replace当前Fragment
     *
     * @param to
     */
    public void replace(int id, BaseFragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (mFragmentContent != null) {
            if (mFragmentContent != to) {
                transaction.hide(mFragmentContent).replace(id, to);
            }
        } else {
            transaction.replace(id, to);
        }
        transaction.commitAllowingStateLoss();  //推荐使用此方法，更安全，更方便
        mFragmentContent = to;
    }

    /**
     * 设置正标题
     *
     * @param titleName
     */
    public void setActionBarTitle(String titleName) {
        tvTitle.setText(titleName);
    }

    /**
     * 设置标题
     *
     * @param strResId
     */
    public void setActionBarTitle(int strResId) {
        tvTitle.setText(getString(strResId));
    }

    /**
     * 设置副标题
     *
     * @param subtitleName
     */
    public void setActionBarSubtitleName(String subtitleName) {
        getSupportActionBar().setSubtitle(subtitleName);
    }

    /**
     * 设置副标题
     *
     * @param subtitleName
     */
    public void setActionBarSubtitleName(int subtitleName) {
        getSupportActionBar().setSubtitle(subtitleName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取手机IMEI号
     */
    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneInfo() {
        //1. 获取手机厂商： String carrier = android.os.Build.MANUFACTURER;
        //2. 获取手机型号：
        String model = Build.MODEL;
        return model;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ProgressDialog
     */
    public ProgressDialog showDialog(Context context, String messageBody) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage(messageBody);
        pd.getWindow().setLayout(DensityUtil.dp2px(this, 30), DensityUtil.dp2px(this, 30));
        pd.show();
        return pd;
    }

    public AlertDialog showUpdataDialog(final Activity context, String title, String messageBody) {
        AlertDialog.Builder builer = new AlertDialog.Builder(context);
        builer.setTitle(title);
        builer.setMessage(messageBody);
        builer.setCancelable(false);
        //确定
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //取消
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
        return dialog;
    }

    /**
     * 设置状态栏的颜色
     *
     * @param activity
     */
    protected void setStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//FLAG_TRANSLUCENT_STATUS//FLAG_FULLSCREEN

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

    /**
     * 模板添加View
     *
     * @param resource
     */
    protected void reSetContentView(@LayoutRes int resource) {
        View view = LayoutInflater.from(this).inflate(resource, null);
        relativeLayout.addView(view);
    }

    /**
     * 通过反射获取状态栏的高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        if (statusBarHeight == 0) {
            Class<?> c;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 查找view
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoreUtil.removeAppActivity(this);
    }

    /**
     * toolbar的右边文字点击事件
     */
    protected void rightTextClick() {
    }

    /**
     * toolbar的右边图片点击事件
     */
    protected void rightImgClick() {
    }
}

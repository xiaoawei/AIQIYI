package com.tsg.xutil.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tsg.xutil.R;
import com.tsg.xutil.base.BaseActivity;
import com.tsg.xutil.bean.MyJsoup;
import com.tsg.xutil.constant.Constant;
import com.tsg.xutil.util.SPNameUtil;
import com.tsg.xutil.util.T;
import com.tsg.xutil.util.TextUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.login_phonenum)
    EditText phonenum;
    @ViewInject(R.id.login_password)
    EditText password;
    @ViewInject(R.id.login_select)
    TextView select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reSetContentView(R.layout.activity_login);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String phoneNumStr = SPNameUtil.getStringData(this, Constant.PHONENUM);
        if (TextUtil.stringIsNotNull(phoneNumStr)) {
            phonenum.setText(phoneNumStr);
            phonenum.setSelection(phoneNumStr.length());
        }
    }

    private void init() {
        x.view().inject(this);
        setActionBarTitle("登录");
        left.setVisibility(View.INVISIBLE);
        left.setOnClickListener(null);
    }

    @Event(R.id.login_bt)
    private void loginBt(View view) {//登录
     /*   MyJsoup myJsoup = new MyJsoup();
        myJsoup.parse(this);*/
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Event(R.id.login_select)
    private void Select(View view) {//选环境
        Intent intent = new Intent(this, TestHostActivity.class);
        startActivity(intent);
    }

}

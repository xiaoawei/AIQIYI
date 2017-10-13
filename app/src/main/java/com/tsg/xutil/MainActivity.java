package com.tsg.xutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.tsg.xutil.activity.TestHostActivity;
import com.tsg.xutil.base.BaseActivity;
import com.tsg.xutil.base.BaseFragment;
import com.tsg.xutil.constant.Constant;
import com.tsg.xutil.fragment.HomeFragment;
import com.tsg.xutil.fragment.MineFragment;
import com.tsg.xutil.fragment.SearchFragment;
import com.tsg.xutil.util.CoreUtil;
import com.tsg.xutil.util.SPUtil;
import com.tsg.xutil.util.T;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private long firstTime = 0;
    @ViewInject(R.id.main_bottomNavigationBar)
    private BottomNavigationBar bottomNavigationBar;
    private ArrayList fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reSetContentView(R.layout.activity_main);
        x.view().inject(this);
        init();
    }

    private void init() {
        rightText.setText("选环境");
        rightText.setTextColor(getResources().getColor(R.color.blue));
        rightText.setVisibility(View.VISIBLE);
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestHostActivity.class);
                startActivity(intent);
            }
        });
        setFragments();
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.home, "Home"))
                .addItem(new BottomNavigationItem(R.mipmap.search, "Search"))
                .addItem(new BottomNavigationItem(R.mipmap.mine, "Mine"))
                .setActiveColor(R.color.blue)
                .setFirstSelectedPosition(Constant.HOMEFRAGMENT)
                .initialise();
        switchFragmentContent(R.id.frame_main, (BaseFragment) fragments.get(Constant.HOMEFRAGMENT));
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case Constant.HOMEFRAGMENT: {
                        switchFragmentContent(R.id.frame_main, (BaseFragment) fragments.get(Constant.HOMEFRAGMENT));
                        break;
                    }
                    case Constant.SEARCHFRAGMENT: {
                        switchFragmentContent(R.id.frame_main, (BaseFragment) fragments.get(Constant.SEARCHFRAGMENT));
                        break;
                    }
                    case Constant.MINEFRAGMENT: {
                        switchFragmentContent(R.id.frame_main, (BaseFragment) fragments.get(Constant.MINEFRAGMENT));
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    /**
     * 初始化页面数据源
     *
     * @return
     */
    public List<BaseFragment> setFragments() {
        fragments = new ArrayList();
        fragments.add(new HomeFragment());
        fragments.add(new SearchFragment());
        fragments.add(new MineFragment());
        return fragments;
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {  //如果两次按键时间间隔大于2秒，则不退出
            T.showShort(this, "再按一次退出程序");
            firstTime = secondTime;//更新firstTime
        } else {
            exip();
            super.onBackPressed();
        }
    }

    private void exip() {
        SPUtil.removeAllData(this);
        CoreUtil.exitApp();
        finish();
    }
}

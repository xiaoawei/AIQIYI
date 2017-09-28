package com.tsg.xutil.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsg.xutil.util.L;

/**
 * Created by xiaoAwei on 2017/6/5.
 */
public abstract class BaseViewPagerFragment extends Fragment {
    protected View rootView;

    private boolean isFragmentVisible;
    //是否是第一次开启网络加载
    public boolean isFirst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResource(), null);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (isFragmentVisible && !isFirst) {
            onFragmentVisibleChange(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isFragmentVisible = true;
        }
        if (rootView == null) {
            return;
        }
        //可见，并且没有加载过
        if (!isFirst && isFragmentVisible) {
            onFragmentVisibleChange(true);
            return;
        }
        //由可见——>不可见 已经加载过
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    //初始化view
    protected abstract void initView(View view);

    //获取布局文件
    protected abstract int getLayoutResource();

    protected abstract void fragmentVisible();

    protected void onFragmentVisibleChange(boolean b) {
        L.e(getClass().getSimpleName() + ": " + b);
        if (b) {
            isFirst = true;
            fragmentVisible();
        } else {
            isFirst = false;
        }
    }
}

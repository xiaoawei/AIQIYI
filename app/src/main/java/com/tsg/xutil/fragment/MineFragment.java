package com.tsg.xutil.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.tsg.xutil.MainActivity;
import com.tsg.xutil.R;
import com.tsg.xutil.base.BaseFragment;
import com.tsg.xutil.util.T;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by xiaoAwei on 2017/9/25.
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    protected void initView(View view) {
        activity = (MainActivity) getActivity();
    }

    @Override
    protected void show() {
        super.show();
        activity.setActionBarTitle("我的");
    }
}

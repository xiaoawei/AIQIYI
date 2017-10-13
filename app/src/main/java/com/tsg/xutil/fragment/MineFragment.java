package com.tsg.xutil.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.tsg.xutil.MainActivity;
import com.tsg.xutil.R;
import com.tsg.xutil.activity.WebPlayActivity;
import com.tsg.xutil.base.BaseFragment;
import com.tsg.xutil.bean.VideoInfo;
import com.tsg.xutil.constant.Constant;
import com.tsg.xutil.util.T;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoAwei on 2017/9/25.
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

    private MainActivity activity;
    @ViewInject(R.id.mine_testPlay)
    private Button testPlay;

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

    @Event(R.id.mine_testPlay)
    private void testPlay(View view) {
        VideoInfo videoInfo = new VideoInfo();
        ArrayList jiList = new ArrayList();
        jiList.add("https://v.qq.com/x/cover/3mctyqewi1dit2p/z0024luwsv4.html");
        videoInfo.setJiList(jiList);
        Intent intent = new Intent(activity, WebPlayActivity.class);
        intent.putExtra(Constant.VIDEOBEAN, videoInfo);
        startActivity(intent);
    }
}

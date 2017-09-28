package com.tsg.xutil.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.jude.rollviewpager.hintview.TextHintView;
import com.tsg.xutil.MainActivity;
import com.tsg.xutil.R;
import com.tsg.xutil.adapter.LooperPictureAdapter;
import com.tsg.xutil.base.BaseFragment;
import com.tsg.xutil.util.DensityUtil;
import com.tsg.xutil.util.T;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by xiaoAwei on 2017/9/25.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    private MainActivity activity;
    @ViewInject(R.id.home_rollpagerView)
    public RollPagerView rollPagerView;
    @ViewInject(R.id.home_player)
    public JCVideoPlayerStandard playerStandard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    protected void show() {
        super.show();
        rollPagerView.resume();
        activity.setActionBarTitle("主页");
    }

    @Override
    protected void hide() {
        super.hide();
        rollPagerView.pause();
    }

    @Override
    protected void initView(View view) {
        activity = (MainActivity) getActivity();
        ColorPointHintView colorPointHintView = new ColorPointHintView(activity, R.color.gray, R.color.white);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(activity, 50));
        colorPointHintView.setLayoutParams(layoutParams);
        rollPagerView.setHintView(colorPointHintView);
        rollPagerView.setHintView(new TextHintView(activity));
        List images = new ArrayList();
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1506330468150&di=d9c0737aa8c1b8c68a87f4e00f1a2ec9&imgtype=0&src=http%3A%2F%2Fimg3.100bt.com%2Fupload%2Fttq%2F20121202%2F1354443812118.png");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1506330468150&di=5229ebbf3f70825bcebcbac19934f8ed&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160519%2F7ec97d54b33e4a64be76fa76dfeb7052_th.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1506330468150&di=706f7d4bf838b1c73d997dc1f9f3f28b&imgtype=0&src=http%3A%2F%2Fimg686.ph.126.net%2FR20OxhV2EdITY7GoqkJigw%3D%3D%2F2857815438544549879.jpg");
        LooperPictureAdapter looperPictureAdapter = new LooperPictureAdapter(activity, images);
        rollPagerView.setAdapter(looperPictureAdapter);
        rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                T.showLong(activity, "我是第" + position + "张图片");
            }
        });

        playerStandard.setUp("http://58.222.29.129/videos/v0/20151128/ba/07/ad5396fdb9df6e5ece87f75d05793286.mp4", playerStandard.SCREEN_LAYOUT_NORMAL, "视频标题");
    }
}

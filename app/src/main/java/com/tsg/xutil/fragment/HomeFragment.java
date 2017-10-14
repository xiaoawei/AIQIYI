package com.tsg.xutil.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsg.xutil.MainActivity;
import com.tsg.xutil.R;
import com.tsg.xutil.base.BaseFragment;
import com.tsg.xutil.util.L;
import com.tsg.xutil.util.VideoUtil;
import com.tsg.xutil.util.net.UrlUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by xiaoAwei on 2017/9/25.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    private MainActivity activity;
    @ViewInject(R.id.home_player)
    public JCVideoPlayerStandard playerStandard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    protected void show() {
        super.show();
        activity.setActionBarTitle("主页");
    }

    @Override
    protected void hide() {
        super.hide();
    }

    @Override
    protected void initView(View view) {
        activity = (MainActivity) getActivity();

//        playerStandard.setUp("http://58.222.29.129/videos/v0/20151128/ba/07/ad5396fdb9df6e5ece87f75d05793286.mp4", playerStandard.SCREEN_LAYOUT_NORMAL, "视频标题");
        String url ="";
        Bitmap bitmap= VideoUtil.getFirstVideoBitmap(activity,true,url);
        playerStandard.setUp(url, playerStandard.SCREEN_LAYOUT_NORMAL, "视频标题");
        playerStandard.thumbImageView.setImageBitmap(bitmap);

        L.e(UrlUtil.getURLEncoderString("http://dl141.80s.im:920/1709/%E8%9C%98zx%EF%BC%9Ay%E9%9B%84g%E6%9D%A5/%E8%9C%98zx%EF%BC%9Ay%E9%9B%84g%E6%9D%A5.mp4"))
        ;
    }
}

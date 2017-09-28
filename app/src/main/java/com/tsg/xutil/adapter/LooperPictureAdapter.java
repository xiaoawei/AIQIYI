package com.tsg.xutil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.tsg.xutil.R;
import com.tsg.xutil.base.BaseCustomAdapter;
import com.tsg.xutil.util.net.ImageLoader;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.List;

/**
 * Created by xiaoAwei on 2017/9/25.
 */
public class LooperPictureAdapter extends StaticPagerAdapter {
    private final Context context;
    private final List data;
    private View convertView;

    public LooperPictureAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

  /*  @Override
    public View getView(ViewGroup container, int position) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_looper_picture, null);
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String imgUrl = (String) data.get(position);
        ImageLoader.bindNetImage(viewHolder.imageView, imgUrl);
        return convertView;
    }*/

    @Override
    public int getCount() {
        return data.size();
    }

    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        String imgUrl = (String) data.get(position);
        ImageLoader.bindNetImage(view, imgUrl);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

}

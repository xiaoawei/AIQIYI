package com.tsg.xutil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsg.xutil.R;
import com.tsg.xutil.base.BaseCustomAdapter;
import com.tsg.xutil.bean.VideoInfo;
import com.tsg.xutil.util.net.ImageLoader;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by xiaoAwei on 2017/9/25.
 */
public class SearchAdapter extends BaseCustomAdapter {
    public SearchAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_search, null);
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        VideoInfo videoInfo = (VideoInfo) data.get(position);
        ImageLoader.bindNetImage(viewHolder.imageView, videoInfo.getImgUrl());
        viewHolder.title.setText(videoInfo.getTitle());
        viewHolder.content.setText(videoInfo.getProduce());
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.adapter_search_img)
        private ImageView imageView;
        @ViewInject(R.id.adapter_search_title)
        private TextView title;
        @ViewInject(R.id.adapter_search_content)
        private TextView content;
    }
}

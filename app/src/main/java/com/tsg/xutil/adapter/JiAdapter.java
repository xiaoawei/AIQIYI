package com.tsg.xutil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class JiAdapter extends BaseCustomAdapter {
    public JiAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_ji, null);
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.numBt.setText("第" + (position + 1) + "集");
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.adapter_ji_numBt)
        private Button numBt;
    }
}

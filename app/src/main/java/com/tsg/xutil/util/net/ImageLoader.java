package com.tsg.xutil.util.net;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.tsg.xutil.R;
import com.tsg.xutil.base.CoreApplication;
import com.tsg.xutil.util.T;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by xiaoAwei on 2017/9/19.
 */
public class ImageLoader {

    public static void bindNetImage(ImageView imageView, String url) {
        //通过ImageOptions.Builder().set方法设置图片的属性
        ImageOptions imageOptions = new ImageOptions.Builder().
                setFadeIn(true)//淡入效果
                //ImageOptions.Builder()的一些其他属性：
                .setLoadingDrawableId(R.mipmap.img_core_loading) //设置加载中的动画
                .setIgnoreGif(false) //忽略Gif图片
                .setSize(200, 200) //设置大小
                .setFailureDrawableId(R.mipmap.img_def_error) //以资源id设置加载失败的动画
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();
        x.image().bind(imageView, url, imageOptions);
    }
}

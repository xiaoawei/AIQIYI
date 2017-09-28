package com.tsg.xutil.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by xiaoAwei on 2017/9/26.
 */
public class VideoUtil {
    /**
     * 获取视频第一帧
     */
    public static Bitmap getFirstVideoBitmap(Context context, boolean isNet, String videoUrl) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (isNet) {
                retriever.setDataSource(videoUrl, new HashMap());//根据url获取第一帧
            } else {
                retriever.setDataSource("/sdcard/03.mp4");// 获取本地视频的第一帧
            }
            bitmap = retriever.getFrameAtTime();//  获得第一帧图片
            FileOutputStream outStream = null;// 视频第一帧的压缩
            outStream = new FileOutputStream(new File(context.getExternalCacheDir().getAbsolutePath() + "/" + "视频" + ".jpg"));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outStream);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }
}

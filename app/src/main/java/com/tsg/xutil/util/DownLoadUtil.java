package com.tsg.xutil.util;

import android.app.ProgressDialog;

import com.tsg.xutil.base.BaseActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaoAwei on 2017/7/10.
 */
public class DownLoadUtil {

    public static File getFileFromServer(BaseActivity context, String path) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        //获取到文件的大小 pd.setMax(conn.getContentLength());
        InputStream is = conn.getInputStream();
        File file = new File(context.getFilesDir().getPath() + "/turingcard.cap");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        int total = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
        }
        fos.close();
        bis.close();
        is.close();
        return file;
    }

    /**
     * 从服务器中下载APK
     */
    public static void downLoadCap(final BaseActivity context, final String url) {
        final ProgressDialog progressDialog = context.showDialog(context, "正在加载,请稍后...");
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(context, url);
                    L.e("下载成功");
                } catch (Exception e) {
                    L.e("下载失败");

                } finally {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }.start();
    }
}

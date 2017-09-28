package com.tsg.xutil.util;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * @Description: Toast工具类
 */
public class T {
    public static boolean isShow = true;
    public  static Toast toast = null;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param view
     */
    public static void showShortMyView(Context context, View view) {
        if (isShow) {
            if (toast != null) {
                toast.cancel();
                toast = null;
                toast = new Toast(context);
            } else {
                toast = new Toast(context);
            }
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(view);
            toast.show();
            toast = null;
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, String message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, String message, int duration) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, duration);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, duration);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }

    /**
     * @param activity
     * @param message
     * @Description: 显示Toast消息, 当在非UI线程中需要显示消息时调用此方法
     */
    public static void showToastMsgOnUiThread(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                if (isShow) {
                    if (toast == null) {
                        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                    } else {
                        toast.setText(message);
                    }
                    toast.show();
                }
            }
        });
    }
}
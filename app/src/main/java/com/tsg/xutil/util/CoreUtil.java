package com.tsg.xutil.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * @Description: 核心工具类
 */
public class CoreUtil {

    public static ArrayList<Activity> ALL_ACTIVITY = new ArrayList<Activity>();

    /**
     * @param activity
     * @Description: 添加Activity到列表中
     */
    public static void addAppActivity(Activity activity) {
        if (!ALL_ACTIVITY.contains(activity)) {
            ALL_ACTIVITY.add(activity);
        }
    }

    /**
     * @param activity
     * @Description: 从列表移除Activity
     */
    public static void removeAppActivity(Activity activity) {
        if (ALL_ACTIVITY.contains(activity)) {
            ALL_ACTIVITY.remove(activity);
            activity.finish();
        }
    }

    /**
     * @Description: 退出应用程序
     */
    public static void exitApp() {
        L.e("--- 销毁 Activity size--->>:" + ALL_ACTIVITY.size());
        for (Activity ac : ALL_ACTIVITY) {
            if (!ac.isFinishing()) {
                ac.finish();
            }
        }
        ALL_ACTIVITY.clear();

        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

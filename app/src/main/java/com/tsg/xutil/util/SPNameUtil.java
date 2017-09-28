package com.tsg.xutil.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

public class SPNameUtil {

    //文件存储的位置
    private static final String FILE_NAME = "USER";

    /**
     * 写入String型的数据到。data/data/包名/share_prefs下。map类型存储
     *
     * @param context
     * @param key     String
     * @param value   String
     */
    public static void putStringData(Context context, String key, String value) {
        SharedPreferences pre = context.getSharedPreferences(context.getPackageName() + FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 写入boolean型的数据到。data/data/包名/share_prefs下。map类型存储
     *
     * @param context
     * @param key     String
     */
    public static void putBooleanData(Context context, String key, boolean value) {
        SharedPreferences pre = context.getSharedPreferences(context.getPackageName() + FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 读取String型数据。
     *
     * @param context
     * @param key     String
     */
    public static String getStringData(Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(context.getPackageName() + FILE_NAME, Context.MODE_PRIVATE);
        return pre.getString(key, "");
    }

    /**
     * 读取boolean型数据。
     *
     * @param context
     * @param key     String
     */
    public static boolean getBooleanData(Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(context.getPackageName() + FILE_NAME, Context.MODE_PRIVATE);
        return pre.getBoolean(key, false);
    }

    /**
     * 得到全部数据。return Map<String,?>
     * Map<String,?> map=new HashMap();
     */
    public static Map getAllData(Context context) {
        SharedPreferences pre = context.getSharedPreferences(context.getPackageName() + FILE_NAME, Context.MODE_PRIVATE);
        return pre.getAll();
    }

    /**
     * 判断是否存在Key
     */
    public static boolean isExist(Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(context.getPackageName() + FILE_NAME, Context.MODE_PRIVATE);
        return pre.contains(key);
    }

    /**
     * 移除对应key值得数据
     *
     * @param context
     * @param key     String
     */
    public static void removeData(Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(context.getPackageName() + FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 移除全部数据。
     */
    public static void removeAllData(Context context) {
        SharedPreferences pre = context.getSharedPreferences(context.getPackageName() + FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.clear();
        editor.commit();
    }
}


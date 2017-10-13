package com.tsg.xutil.constant;

import android.content.Context;

import com.tsg.xutil.util.CollectionUtil;
import com.tsg.xutil.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoAwei on 2017/4/19.
 */
public class RequestApi {
    public static List list;

    public static List initList() {
        if (CollectionUtil.listIsNull(list)) {
            list = new ArrayList();
        }
        list.add("http://api.662820.com/xnflv/index.php?url=");//
        list.add("http://api.dj6u.com/dj6u/index.php?url=");//
        list.add("http://www.wmxz.wang/video.php?url=");//
        return list;
    }

        public static String host = "http://api.662820.com/xnflv/index.php?url=";//乐乐解析
    //    public static String host = "http://api.dj6u.com/dj6u/index.php?url=";//全网
//    public static String host = "http://www.wmxz.wang/video.php?url=";//无名2https://www.55523355.com/index.php?url=http://www.iqiyi.com/v_19rroopiy0.html
    //        public static String host = "http://www.sfsft.com/admin.php?url=";//无名
//    public static String host = "http://api.xfsub.com/index.php?url=";//旋风
    public static String forwardSlashUrl = "/";


    //*****************************************************get********************************************************
    public static String getSearchResultForAi = "http://so.iqiyi.com/so/q_";//获取爱奇艺的搜索结果
    //*****************************************************post********************************************************

    /**
     * 拼接url
     *
     * @param context
     * @param startUrl
     * @param endUrl
     * @return
     */
    public static String getUrl(Context context, String startUrl, boolean boo, String endUrl) {
        if (CollectionUtil.listIsNull(list)) {
            initList();
        }
        if (boo) {
            String token = SPUtil.getStringData(context, Constant.Token);
            String url = host + startUrl + token + endUrl;
            return url;
        } else {
            String url = host + startUrl + endUrl;
            return url;
        }
    }
}

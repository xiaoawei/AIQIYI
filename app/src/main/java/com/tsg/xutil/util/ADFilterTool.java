package com.tsg.xutil.util;

import android.content.Context;
import android.content.res.Resources;

import com.tsg.xutil.R;

/**
 * Created by xiaoAwei on 2017/9/26.
 */
public class ADFilterTool {
    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }
}

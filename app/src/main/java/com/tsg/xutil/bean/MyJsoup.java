package com.tsg.xutil.bean;

import android.content.Context;
import android.os.AsyncTask;

import com.tsg.xutil.constant.RequestApi;
import com.tsg.xutil.util.L;
import com.tsg.xutil.util.T;
import com.tsg.xutil.util.net.BhResponseError;
import com.tsg.xutil.util.net.MyXUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoAwei on 2017/9/22.
 */
public class MyJsoup {

    public void parse(Context context) {
        
        MyXUtil myXUtil = new MyXUtil(context) {
            @Override
            public void loadSuccess(Object responseInfo) {
                L.e(responseInfo);
                try {
                    //从一个URL加载一个Document对象。
                    Document doc = Jsoup.parse((String) responseInfo);
                    //选择“美食天下”所在节点
                    Elements elements = doc.getElementsByTag("body");
                    Element elementsTwo = elements.get(0);
                    Elements elementsThree = elementsTwo.select("div.search_result_tip");
                    Element elementsFore = elementsThree.get(0);
                    Elements elementsFive = elementsFore.getElementsByClass("mod_result");
                    Elements elementsSix = elementsFive.select("ul[class=\"mod_result_list\"]");
                    Elements elementsSeven = elementsSix.select("li[class=\"list_item\"]");
                    String imgHerf = elementsSeven.select("a").select("img").attr("src");
//                    Elements elements = doc.select("div.mod_result");
                    //打印 <a>标签里面的title
                    String picture = elements.select("a").select("img").attr("src");
                    L.e("mytag", picture);
                } catch (Exception e) {
                    L.e("mytag", e.toString());
                }
            }

            @Override
            public void loadError(BhResponseError error) {
            }

            @Override
            public void finish() {

            }
        };
        myXUtil.get("http://so.iqiyi.com/so/q_%E9%BE%99%E7%8F%A0%E8%B6%85",
                new HashMap(), false, null, true, "正在登录，请稍后...");
    }
}

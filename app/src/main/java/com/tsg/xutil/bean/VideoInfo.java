package com.tsg.xutil.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaoAwei on 2017/9/25.
 */
public class VideoInfo implements Serializable {

    private String imgUrl;
    private String title;
    private String produce;
    private ArrayList jiList;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public ArrayList getJiList() {
        return jiList;
    }

    public void setJiList(ArrayList jiList) {
        this.jiList = jiList;
    }

}

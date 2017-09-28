package com.tsg.xutil.constant;

/**
 * Created by xiaoAwei on 2017/4/7.
 */
public interface Constant {
    boolean IS_DEBUG = true;   //debug环境日志控制
    boolean IsPerson = false; //无票据
    boolean IsCompany = true;//对公付


    String Token = "Token";//token

    String USERNAME = "userName";
    String PHONENUM = "phoneNum";
    String SERIALNO = "serialNo";
    String NAME = "name";
    String IDNUM = "idNum";
    String COMPANYNAME = "companyName";

    //webPlay所需要的VideoBean
    String VIDEOBEAN="videoBean";
    //MainActivity
    String WITCHKEYFRAGMENTFORMAINACTIVITY = "witchKeyFragmentForMainActivity";//键分辨是公司还是个人开卡
    int HOMEFRAGMENT = 0;//主页面
    int SEARCHFRAGMENT = 1;//搜索页面
    int MINEFRAGMENT = 2;//我的页面
}

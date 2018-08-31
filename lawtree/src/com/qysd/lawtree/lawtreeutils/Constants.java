package com.qysd.lawtree.lawtreeutils;


import com.qysd.lawtree.BuildConfig;

/**
 * Created by QYSD_AD on 2017/2/16.
 * 常量的管理
 */

public class Constants {

    //上线网址
    //private static final String baseUrlOfOnline = "http://39.106.154.149/";
    private static final String baseUrlOfOnline = "http://39.106.154.149:90/";
    //测试网址
    //private static final String baseUrlOfDebug = "http://39.106.154.149/";

    private static final String baseUrlOfDebug = "http://192.168.1.185:80/";//145

    public static final String baseUrl = BuildConfig.DEBUG ? baseUrlOfOnline : baseUrlOfOnline;
}

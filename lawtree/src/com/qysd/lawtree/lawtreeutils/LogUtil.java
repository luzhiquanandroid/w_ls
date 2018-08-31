package com.qysd.lawtree.lawtreeutils;

import android.util.Log;

/**
 * Created by QYSD_AD on 2017/6/9.
 */

public class LogUtil {
    private String tag;
    public static boolean debug = false;

    public LogUtil(String tag) {
        this.tag = tag;
    }

    public LogUtil(Class<?> clazz) {
        this.tag = clazz.getName() + "-" + clazz.hashCode();
    }

    public void d(String msg) {
        if (debug) {
            Log.d(tag, msg);
        }
    }

    public void i(String msg) {
        if (debug) {
            Log.i(tag, msg);
        }
    }

    public void w(String msg) {
        if (debug) {
            Log.w(tag, msg);
        }
    }

    public void e(String msg) {
        if (debug) {
            Log.e(tag, msg);
        }
    }

    public void e(Throwable e) {
        if (debug) {
            Log.e(tag, "", e);
        }
    }

    public void json(String json) {
        if (debug) {
            Log.i(tag, json);
        }
    }
}

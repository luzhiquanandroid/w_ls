package com.qysd.lawtree.lawtreeutil.httputils;


import android.support.v7.util.SortedList;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by QYSD_GT on 2017/2/27.
 */

public abstract class HttpsCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        return response.body().string();
    }
}




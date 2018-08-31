package com.qysd.lawtree.lawtreeutil.httputils;


import android.util.Log;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by QYSDAND on 2016/8/26.
 */
public abstract class UserCallback extends Callback<String> {

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        return response.body().string();
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }
}

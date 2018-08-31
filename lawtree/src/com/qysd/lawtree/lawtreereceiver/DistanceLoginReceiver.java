package com.qysd.lawtree.lawtreereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qysd.lawtree.lawtreeactivity.DistanceLoginHandleActivity;

/**
 * 异地登录的广播
 */

public class DistanceLoginReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /* 获取Intent对象中的数据 */
        String msg = intent.getStringExtra("msg");
        Intent intentLog = new Intent(context, DistanceLoginHandleActivity.class);
        intentLog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentLog.putExtra("msg", msg);
        context.startActivity(intentLog);
    }
}

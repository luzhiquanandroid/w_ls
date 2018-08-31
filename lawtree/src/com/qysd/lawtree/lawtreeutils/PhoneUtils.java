package com.qysd.lawtree.lawtreeutils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PhoneUtils {
    /**
     * 打电话
     *
     * @param context
     * @param number
     */
    public static void openDial(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }
}

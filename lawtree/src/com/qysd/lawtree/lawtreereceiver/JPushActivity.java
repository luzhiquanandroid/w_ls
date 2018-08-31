package com.qysd.lawtree.lawtreereceiver;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class JPushActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

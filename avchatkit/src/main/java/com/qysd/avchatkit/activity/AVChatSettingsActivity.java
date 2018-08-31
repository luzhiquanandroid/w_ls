package com.qysd.avchatkit.activity;

import android.os.Bundle;

import com.qysd.avchatkit.R;
import com.qysd.avchatkit.common.activity.NimToolBarOptions;
import com.qysd.avchatkit.common.activity.ToolBarOptions;
import com.qysd.avchatkit.common.activity.UI;

/**
 * Created by liuqijun on 7/19/16.
 * 注意:全局配置,不区分用户
 */
public class AVChatSettingsActivity extends UI {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.avchat_settings_layout);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.nrtc_settings;
        setToolBar(R.id.toolbar, options);


    }

}

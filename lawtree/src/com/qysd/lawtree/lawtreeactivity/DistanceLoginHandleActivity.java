package com.qysd.lawtree.lawtreeactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.config.preference.Preferences;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.main.activity.WelcomeActivity;

public class DistanceLoginHandleActivity extends Activity {
    private Dialog dialog = null;
    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_grash_order);
        msg = getIntent().getStringExtra("msg");
        mDialog();
    }

    /**
     * 弹出dialog
     */
    public void mDialog() {
        dialog = new Dialog(this, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_distance_login, null);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        if (msg != null && !"".equals(msg)) {
            tv_content.setText(msg);
        }
        //点击确定进行登录页面的跳转
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserInfo.reset(DistanceLoginHandleActivity.this);
                Preferences.saveUserAccount("");
                Preferences.saveUserToken("");
                WelcomeActivity.setIsFist(true);
                dialog.dismiss();
                Intent intentEn = new Intent(DistanceLoginHandleActivity.this,
                        WelcomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentEn);
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (dialog != null) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}


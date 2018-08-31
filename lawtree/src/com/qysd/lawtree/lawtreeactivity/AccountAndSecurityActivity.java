package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;

public class AccountAndSecurityActivity extends BaseActivity {

    private RelativeLayout rl_update_password;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_account_and_security);
        initTitle(R.drawable.ic_left_jt,"账号与安全");
    }

    @Override
    protected void initView() {
        rl_update_password = findViewById(R.id.rl_update_password);
    }

    @Override
    protected void bindListener() {
        rl_update_password.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_update_password:
                Intent intent = new Intent(AccountAndSecurityActivity.this,UpdatePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}

package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.login.LoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends BaseActivity {

    private EditText et_password,et_passwordtwo;
    private TextView tv_register;
    
    @Override
    protected void bindView() {
        setContentView(R.layout.activity_pass_reset);
    }

    @Override
    protected void initView() {
        et_password = findViewById(R.id.et_password);
        et_passwordtwo = findViewById(R.id.et_passwordtwo);
        tv_register = findViewById(R.id.tv_register);
    }

    @Override
    protected void bindListener() {
        tv_register.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                if (et_password.getText().toString().equals(et_passwordtwo.getText().toString())){
                    register();
                }else {
                    Toast.makeText(ResetPasswordActivity.this,"两次密码输入不一样",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void register(){
        OkHttpUtils.post()
                .url(Constants.baseUrl+"userapp/updatePassWord")
                .addParams("mobile",getIntent().getStringExtra("account"))
                .addParams("password",et_password.getText().toString().trim())
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        log(response.toString());
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            if (object.optString("code").equals("1")) {
                                Toast.makeText(ResetPasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}

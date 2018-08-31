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
import com.qysd.lawtree.lawtreeutils.SystemUtil;
import com.qysd.lawtree.lawtreeutils.VerificationUtil;
import com.qysd.lawtree.login.LoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterPassActivity extends BaseActivity {

    private EditText et_password,et_passwordtwo;
    private TextView tv_register;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_pass_register);
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
                if ("".equals(et_password.getText().toString())){
                    Toast.makeText(RegisterPassActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
//                int length = et_password.getText().toString().trim().length();
//                if (!(8<=length && length<=15)){
//                    Toast.makeText(RegisterPassActivity.this,"密码必须为8到15位的字母和数字",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                 if (!VerificationUtil.passwordFormat(et_password.getText().toString().trim())) {
                     Toast.makeText(RegisterPassActivity.this, "字母和数字", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 if (!et_password.getText().toString().equals(et_passwordtwo.getText().toString())) {
                     Toast.makeText(RegisterPassActivity.this, "两次密码输入不一样", Toast.LENGTH_SHORT).show();
                     return;
                 }

                    register();

                break;
        }
    }

    private void register(){
        OkHttpUtils.post()
                .url(Constants.baseUrl+"userapp/registerUser")
                .addParams("mobile",getIntent().getStringExtra("account"))
                .addParams("password",et_password.getText().toString().trim())
                .addParams("telType","0")//注册手机类型 0，安卓 1，ios
                .addParams("phoneType", SystemUtil.getSystemModel())//手机型号（ios:自带 安卓：极光设备号）
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        log(response.toString());
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            if (object.optString("code").equals("1")) {
                                Toast.makeText(RegisterPassActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterPassActivity.this, LoginActivity.class));
                                finish();
                            }else if(object.optString("code").equals("2")){
                                Toast.makeText(RegisterPassActivity.this,"手机号已注册",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterPassActivity.this, LoginActivity.class));
                                finish();
                            }else {
                                Toast.makeText(RegisterPassActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}

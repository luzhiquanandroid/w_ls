package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.qysd.lawtree.DemoCache;
import com.qysd.lawtree.R;
import com.qysd.lawtree.config.preference.Preferences;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constant;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.login.LoginActivity;
import com.qysd.lawtree.main.activity.MainActivity;
import com.qysd.lawtree.main.activity.SettingsActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePasswordActivity extends BaseActivity {

    private TextView tv_finish;
    private EditText et_old_pass,et_new_pass,et_reset_pass;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_update_password);
    }

    @Override
    protected void initView() {
        tv_finish = findViewById(R.id.tv_finish);
        et_old_pass = findViewById(R.id.et_old_pass);
        et_new_pass = findViewById(R.id.et_new_pass);
        et_reset_pass = findViewById(R.id.et_reset_pass);
    }

    @Override
    protected void bindListener() {
        tv_finish.setOnClickListener(this);
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
            case R.id.tv_finish:
                if (et_new_pass.getText().toString().equals(et_reset_pass.getText().toString())){
                    updatePassword();
                }else {
                    Toast.makeText(this,"两次输入的密码不一样",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 修改密码接口
     */
    public void updatePassword(){
        OkHttpUtils.post()
                .url(Constants.baseUrl+"userapp/upsetPassWord")
                .addParams("userId", GetUserInfo.getUserId(this))
                .addParams("password",et_old_pass.getText().toString())
                .addParams("newPassword",et_new_pass.getText().toString())
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if ("1".equals(object.optString("code"))){
                                Toast.makeText(UpdatePasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent();
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.setClass(UpdatePasswordActivity.this, LoginActivity.class);
//                                startActivity(intent);
                                logout();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 注销
     */
    private void logout() {
        //BottomTabLayoutActivity.logout(UpdatePasswordActivity.this, false);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(UpdatePasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        Preferences.saveUserAccount("");
        Preferences.saveUserToken("");
        DemoCache.clear();
        finish();
        NIMClient.getService(AuthService.class).logout();
    }
}

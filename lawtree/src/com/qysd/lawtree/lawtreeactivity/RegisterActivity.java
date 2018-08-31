package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.VerificationUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity {

    private TextView tv_vertify, tv_next;
    private EditText et_account,et_vertify;
    private TimeCount time;
    private String sendCode = "";//未加密的验证码随机数
    private String sendTime = "";//后台发送的验证码时间

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_register);
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
    }

    @Override
    protected void initView() {
        tv_vertify = findViewById(R.id.tv_vertify);
        tv_next = findViewById(R.id.tv_next);
        et_account = findViewById(R.id.et_account);
        et_vertify = findViewById(R.id.et_vertify);
    }

    @Override
    protected void bindListener() {
        tv_vertify.setOnClickListener(this);
        tv_next.setOnClickListener(this);
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
            case R.id.tv_vertify:
                if (!VerificationUtil.isValidTelNumber(et_account.getText().toString().trim())) {
                    Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }else {
                    getVertify();
                }
                break;
            case R.id.tv_next:
                Intent intent = new Intent(RegisterActivity.this,RegisterPassActivity.class);
                startActivity(intent);
                if ("".equals(et_account.getText().toString())){
                    Toast.makeText(this,"账号不能为空",Toast.LENGTH_SHORT).show();
                }else if ("".equals(et_vertify.getText().toString())){
                    Toast.makeText(this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    next();
                }
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVertify() {
        OkHttpUtils.post()
                .url(Constants.baseUrl + "send/sendReCaptcha")
                .addParams("mobile", et_account.getText().toString().trim())
                .addParams("type","1")//1，注册页面 2，忘记密码（必传）
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response+"=======");
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            if (object.optString("code").equals("1")) {
                                time.start();
                                sendCode = object.optString("sendCode");
                                sendTime = object.optString("sendTime");
                            }else if ("4".equals(object.optString("code"))){
                                Toast.makeText(RegisterActivity.this,"该用户已注册",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void next(){
        OkHttpUtils.post()
                .url(Constants.baseUrl+"userapp/checkIdCode")
                .addParams("mobile",et_account.getText().toString().trim())
                .addParams("idCode",sendCode)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        log(response.toString());
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            if (object.optString("code").equals("1")) {
                                Intent intent = new Intent(RegisterActivity.this,RegisterPassActivity.class);
                                intent.putExtra("account",et_account.getText().toString().trim());
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this,"手机号或验证码不正确",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 定义一个倒计时的内部类
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            tv_vertify.setText("重新验证");
            tv_vertify.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            tv_vertify.setClickable(false);
            tv_vertify.setText("剩余" + millisUntilFinished / 1000 + "秒");
        }
    }
}

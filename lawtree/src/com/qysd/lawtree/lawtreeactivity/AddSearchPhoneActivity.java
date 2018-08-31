package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.VerificationUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by QYSD_AD on 2018/3/26.
 * 通过手机号搜索添加
 */

public class AddSearchPhoneActivity extends BaseActivity {
    private EditText et_phone;//输入的电话号码
    private TextView tv_add_friend;//添加

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_add_search_phone);
        initTitle(R.drawable.ic_left_jt, "手机号添加");
    }

    @Override
    protected void initView() {
        et_phone = findViewById(R.id.et_phone);
        tv_add_friend = findViewById(R.id.tv_add_friend);
    }

    @Override
    protected void bindListener() {
        tv_add_friend.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_friend:
                if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!VerificationUtil.isValidTelNumber(et_phone.getText().toString())) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, LianXiRenDetailActivity.class);
                intent.putExtra("mobile", et_phone.getText().toString());
                intent.putExtra("type", "");
                startActivity(intent);
                finish();
                break;
        }
    }

    //添加好友的请求接口
    private void addFriend(final String phone) {
        OkHttpUtils.post().url(Constants.baseUrl + "friend/addFriend")
                .addParams("mobile", phone)
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("addType", "1")//添加来源(0：扫码 1：手机号码 2：组织架构 3：邀请添加4：群组添加)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String code = jsonObject.optString("code");
                            String status = jsonObject.optString("status");
                            if ("0".equals(code)) {
                                Toast.makeText(AddSearchPhoneActivity.this, "" +
                                        status, Toast.LENGTH_SHORT).show();
                            } else if ("2".equals(code)) {
                                Toast.makeText(AddSearchPhoneActivity.this, "" +
                                        status, Toast.LENGTH_SHORT).show();
                            } else if ("1".equals(code)) {
//                                Toast.makeText(AddSearchPhoneActivity.this, "" +
//                                        jsonObject.optString("status"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddSearchPhoneActivity.this, LianXiRenDetailActivity.class);
                                intent.putExtra("mobile", phone);
                                intent.putExtra("type", "");
                                startActivity(intent);
                                finish();
                            } else if ("3".equals(code)) {
                                Toast.makeText(AddSearchPhoneActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

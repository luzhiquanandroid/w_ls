package com.qysd.lawtree.lawtreeactivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebusbean.AddPersonOrDepartmentBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.LoadDialog;
import com.qysd.uikit.common.util.string.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 添加子部门
 */

public class AddDepartmentActivity extends BaseActivity {
    private EditText et_add_department;
    private TextView tv_shangji_department;
    private String parentid;//父级部门id
    private String level;//等级
    private ImageView iv_setting;
    private boolean status = false;//不是生产部门
    private String state = "0";

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_add_department);
        initTitle("取消", "添加子部门", "完成");
    }

    @Override
    protected void initView() {
        parentid = getIntent().getStringExtra("parentid");
        level = getIntent().getStringExtra("level");
        et_add_department = findViewById(R.id.et_add_department);
        tv_shangji_department = findViewById(R.id.tv_shangji_department);
        iv_setting = findViewById(R.id.iv_setting);
        tv_shangji_department.setText(getIntent().getStringExtra("department"));

        if (status) {
            iv_setting.setImageResource(R.drawable.ic_open);
        } else {
            iv_setting.setImageResource(R.drawable.ic_close);
        }
    }

    @Override
    protected void bindListener() {
        iv_setting.setOnClickListener(this);
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
            case R.id.iv_setting:
                status = !status;
                if (status) {
                    state = "1";
                    iv_setting.setImageResource(R.drawable.ic_open);
                } else {
                    state = "0";
                    iv_setting.setImageResource(R.drawable.ic_close);
                }
                break;
        }

    }

    @Override
    protected void onClickTitleRight(View v) {
        if (StringUtil.isEmpty(et_add_department.getText().toString().trim())) {
            Toast.makeText(this, "部门名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        commitData(et_add_department.getText().toString().trim());
    }

    private void commitData(String departmentName) {
        LoadDialog.show(this);
        OkHttpUtils.post().url(Constants.baseUrl + "company/addCompanyDepartment")
                .addParams("deptname", departmentName)//部门名称(必传)
                .addParams("parentid", parentid)//父集ID(必传，若创建一级部门则传0)
                .addParams("compid", (String) GetUserInfo.getData(this,"compId",""))//公司id(必传)
                .addParams("level", level)//部门等级(必传，列1,2,3,4级)
                .addParams("proddeptflag", state)//是否为生产部 0，不是 1，是
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))//用户id（必传）
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq add dept", response.toString());
                        LoadDialog.dismiss(AddDepartmentActivity.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if ("1".equals(jsonObject.optString("code"))) {
                                Toast.makeText(AddDepartmentActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new AddPersonOrDepartmentBusBean(
                                        "Depart"
                                ));
                                finish();
                            } else {
                                Toast.makeText(AddDepartmentActivity.this, "添加失败，请重试", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebusbean.AddPersonEventBusBean;
import com.qysd.lawtree.lawtreebusbean.AddPersonOrDepartmentBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.LoadDialog;
import com.qysd.lawtree.lawtreeutils.VerificationUtil;
import com.qysd.uikit.common.util.string.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加员工
 */

public class AddPersonActivity extends BaseActivity {
    private TextView tv_add_dept;//部门是回显
    private TextView tv_add_gongxu, tv_add_jiaose;//工序和角色是选择
    private EditText et_add_zhiwei, et_add_name, et_add_phone, et_add_number;//职位 姓名 手机号 工号

    private LinearLayout ll_role, ll_gongxu;//角色和工序
    private List<String> nameString = new ArrayList<>();
    private List<String> idString = new ArrayList<>();
    private Intent intent;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_add_person);
        initTitle("取消", "添加员工", "完成");
    }

    @Override
    protected void initView() {
        ll_role = findViewById(R.id.ll_role);
        ll_gongxu = findViewById(R.id.ll_gongxu);
        tv_add_dept = findViewById(R.id.tv_add_dept);
        tv_add_gongxu = findViewById(R.id.tv_add_gongxu);
        tv_add_jiaose = findViewById(R.id.tv_add_jiaose);
        et_add_name = findViewById(R.id.et_add_name);
        et_add_phone = findViewById(R.id.et_add_phone);
        et_add_zhiwei = findViewById(R.id.et_add_zhiwei);
        et_add_number = findViewById(R.id.et_add_number);
        tv_add_dept.setText(getIntent().getStringExtra("department"));
    }

    @Override
    protected void bindListener() {
        ll_gongxu.setOnClickListener(this);
        ll_role.setOnClickListener(this);
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
            case R.id.ll_role:
                intent = new Intent(AddPersonActivity.this, RoleSelectActivity.class);
                intent.putStringArrayListExtra("roleId", (ArrayList<String>) roleIdList);
                startActivity(intent);
                break;
            case R.id.ll_gongxu:
                intent = new Intent(AddPersonActivity.this, GongXuSelectActivity.class);
                intent.putStringArrayListExtra("gxId", (ArrayList<String>) gxIdList);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onClickTitleRight(View v) {
        //请求完成的接口 核对数据完整性和正确性
        if (StringUtil.isEmpty(et_add_name.getText().toString().trim())) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtil.isEmpty(et_add_phone.getText().toString().trim())) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (VerificationUtil.isValidTelNumber(et_add_name.getText().toString().trim())) {
            Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtil.isEmpty(et_add_zhiwei.getText().toString().trim())) {
            Toast.makeText(this, "职位不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtil.isEmpty(tv_add_jiaose.getText().toString().trim())) {
            Toast.makeText(this, "请选择角色", Toast.LENGTH_SHORT).show();
            return;
        }
        commitData(et_add_number.getText().toString().trim(), et_add_zhiwei.getText().toString().trim(),
                et_add_phone.getText().toString().trim(), et_add_name.getText().toString().trim()
                , roleId, gxId);
    }

    private void commitData(String jobnum, String position, String mobileNum, String userName, String roleId, String procedureid) {
        /**
         * {
         "compid": 0,
         "deptid": 0,
         "flag": 0,
         "id": 0,
         "jobnum": "string",工号
         "position": "string",职位
         "procedureid": 0,工序
         "userid": "string" 用户id
         }
         */
        LoadDialog.show(this);
        OkHttpUtils.post().url(Constants.baseUrl + "company/addCompanyUser")
                .addParams("compid", (String) GetUserInfo.getData(this,"compId",""))
                .addParams("deptid", "0")
                .addParams("jobnum", jobnum)
                .addParams("procedureid", procedureid)
                .addParams("userid", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("position", position)
                .addParams("mobileNum", mobileNum)
                .addParams("userName", userName)
                .addParams("roleId", roleId)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        LoadDialog.dismiss(AddPersonActivity.this);
                        Log.e("lzq add", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if ("1".equals(jsonObject.optString("code"))) {
                                Toast.makeText(AddPersonActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new AddPersonOrDepartmentBusBean(
                                        "Person"
                                ));
                                finish();
                            } else {
                                Toast.makeText(AddPersonActivity.this, "添加失败，请重试", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private List<String> roleIdList = new ArrayList<>();
    private List<String> gxIdList = new ArrayList<>();
    private String roleId = "";
    private String roleName = "";
    private String gxId = "";
    private String gxName = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getData(AddPersonEventBusBean eventBusBean) {
        if ("ROLE".equals(eventBusBean.getType())) {
            roleIdList = eventBusBean.getId();
            roleName = "";
            roleId = "";
            for (int i = 0; i < eventBusBean.getName().size(); i++) {
                if (i == eventBusBean.getName().size() - 1) {
                    roleName = roleName + eventBusBean.getName().get(i);
                    roleId = roleId + eventBusBean.getId().get(i);
                } else {
                    roleName = roleName + eventBusBean.getName().get(i) + ",";
                    roleId = roleId + eventBusBean.getId().get(i) + ",";
                }
            }
            tv_add_jiaose.setText(roleName);
        } else if ("GX".equals(eventBusBean.getType())) {
            gxIdList = eventBusBean.getId();
            gxName = "";
            gxId = "";
            for (int i = 0; i < eventBusBean.getName().size(); i++) {
                if (i == eventBusBean.getName().size() - 1) {
                    gxName = gxName + eventBusBean.getName().get(i);
                    gxId = gxId + eventBusBean.getId().get(i);
                } else {
                    gxName = gxName + eventBusBean.getName().get(i) + ",";
                    gxId = gxId + eventBusBean.getId().get(i) + ",";
                }
            }
            tv_add_gongxu.setText(gxName);
        }
    }
}

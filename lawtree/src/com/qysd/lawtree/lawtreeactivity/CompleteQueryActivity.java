package com.qysd.lawtree.lawtreeactivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebusbean.FragmentEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 完工确认
 */
public class CompleteQueryActivity extends BaseActivity {
    private TextView tv_complete_time, tv_task_num;
    private EditText et_task_num, et_task_remarks;
    private TextView tv_complete_query;
    private String taskid;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_complete_query);
    }

    @Override
    protected void initView() {
        initTitle(R.drawable.ic_left_jt, "完工确认");
        tv_complete_time = findViewById(R.id.tv_complete_time);
        tv_task_num = findViewById(R.id.tv_task_num);
        et_task_num = findViewById(R.id.et_task_num);
        et_task_remarks = findViewById(R.id.et_task_remarks);
        tv_complete_query = findViewById(R.id.tv_complete_query);

        tv_complete_time.setText("完成日期:" + getIntent().getStringExtra("completeTime"));
        taskid = getIntent().getStringExtra("taskid");
        tv_task_num.setText("任务预期完成数量为 " + getIntent().getStringExtra("tasknum")
                + getIntent().getStringExtra("dicName") + "，请输入实际完成数量");
    }

    @Override
    protected void bindListener() {
        tv_complete_query.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_complete_query:
                if ("".equals(et_task_num.getText().toString())) {
                    Toast.makeText(this, "请输入实际完成数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("0".equals(et_task_num.getText().toString().trim())) {
                    Toast.makeText(this, "实际完成数量不能为0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_task_remarks.getText().toString().trim().length() > 140) {
                    Toast.makeText(this, "完工备注不能多于140字符", Toast.LENGTH_SHORT).show();
                    return;
                }
                commitData();
                break;
        }
    }

    private void commitData() {
        OkHttpUtils.post().url(Constants.baseUrl + "productionPlan/updateProductionTaskApp")
                .addParams("taskid", taskid)
                .addParams("compid", (String) GetUserInfo.getData(this,"compId",""))
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("finishnum", et_task_num.getText().toString().trim())
                .addParams("remarks", et_task_remarks.getText().toString().trim())
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("info", response.toString());
                        //{"code":1,"status":"操作成功"}
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.optString("code");
                            if ("1".equals(code)) {
                                Toast.makeText(CompleteQueryActivity.this, jsonObject.optString("status"), Toast.LENGTH_SHORT).show();
                                finish();
                                EventBus.getDefault().post(new FragmentEventBusBean("RWGL", "NO"));
                            } else if ("0".equals(code)) {
                                Toast.makeText(CompleteQueryActivity.this, jsonObject.optString("status"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}

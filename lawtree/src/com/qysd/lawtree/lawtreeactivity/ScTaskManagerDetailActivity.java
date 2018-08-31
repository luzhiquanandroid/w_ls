package com.qysd.lawtree.lawtreeactivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.ShenChanTaskBean;
import com.qysd.lawtree.lawtreebean.SmScTaskBean;
import com.qysd.lawtree.lawtreebusbean.FragmentEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class ScTaskManagerDetailActivity extends BaseActivity {
    private TextView tv_complete_query;
    private TextView tv_task_order, tv_task_time, tv_task_materName, tv_task_num;
    private TextView tv_task_completTime, tv_task_remark, tv_task_current, tv_task_count;
    private ShenChanTaskBean.Status taskDetail;
    private String planId = "";
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private SmScTaskBean smScTaskBean;
    private Bundle task;
    private LinearLayout rl_detail;
    private EditText et_task_num, et_task_remarks;
    private TextView tv_comp_task_num;
    private LinearLayout ll_comp_task_manager;//未完成的显示 已完成的不显示

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_sctask_manager_detail);
    }

    @Override
    protected void initView() {
        initTitle(R.drawable.ic_left_jt, "计划详情");
        ll_comp_task_manager = findViewById(R.id.ll_comp_task_manager);
        tv_comp_task_num = findViewById(R.id.tv_comp_task_num);
        et_task_remarks = findViewById(R.id.et_task_remarks);
        et_task_num = findViewById(R.id.et_task_num);
        rl_detail = findViewById(R.id.rl_detail);
        tv_complete_query = findViewById(R.id.tv_complete_query);
        tv_task_order = findViewById(R.id.tv_task_order);
        tv_task_time = findViewById(R.id.tv_task_time);
        tv_task_materName = findViewById(R.id.tv_task_materName);
        tv_task_num = findViewById(R.id.tv_task_num);
        tv_task_completTime = findViewById(R.id.tv_task_completTime);
        tv_task_remark = findViewById(R.id.tv_task_remark);
        tv_task_current = findViewById(R.id.tv_task_current);
        tv_task_count = findViewById(R.id.tv_task_count);
        task = getIntent().getBundleExtra("task");
        if ("sm".equals(task.getString("type"))) {
            planId = task.getString("planId");
        } else {
            taskDetail = (ShenChanTaskBean.Status) task.getSerializable("taskDetail");
            if ("2".equals(taskDetail.getState())) {
                tv_complete_query.setVisibility(View.GONE);
                ll_comp_task_manager.setVisibility(View.GONE);
            } else {
                tv_complete_query.setVisibility(View.VISIBLE);
                ll_comp_task_manager.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void bindListener() {
        tv_complete_query.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if ("sm".equals(task.getString("type"))) {
            loadSmData(planId);
        } else {
            tv_task_order.setText("订单编号:" + taskDetail.getOrderCodeNick());
            tv_task_time.setText(taskDetail.getdOrderDateStr());
            tv_task_completTime.setText("履约日期:" + taskDetail.getPerformDateStr());
            tv_task_materName.setText(taskDetail.getMaterName());
            tv_task_num.setText(taskDetail.getPlanNum() + taskDetail.getDicName());
            tv_task_remark.setText("备注信息:" + taskDetail.getOrderMemo());
            tv_task_current.setText("当前任务:" + taskDetail.getProcedureName());
            tv_task_count.setText("任务数量:" + taskDetail.getTasknum() + taskDetail.getDicName());
            tv_comp_task_num.setText("任务预期完成数量为 " + taskDetail.getPlanNum() + taskDetail.getDicName() + "，请输入实际完成数量");
        }
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_complete_query:
//                Intent intent = new Intent(this, CompleteQueryActivity.class);
//                if ("sm".equals(task.getString("type"))) {
//                    intent.putExtra("taskid", smScTaskBean.getStatus().getTaskid());
//                    intent.putExtra("tasknum", smScTaskBean.getStatus().getTasknum());
//                    intent.putExtra("dicName", smScTaskBean.getStatus().getDicName());
//                    intent.putExtra("completeTime", smScTaskBean.getStatus().getPerformDateStr());
//                } else {
//                    intent.putExtra("taskid", taskDetail.getTaskid());
//                    intent.putExtra("tasknum", taskDetail.getTasknum());
//                    intent.putExtra("dicName", taskDetail.getDicName());
//                    intent.putExtra("completeTime", taskDetail.getPerformDateStr());
//                }
//
//                startActivity(intent);
                if ("".equals(et_task_num.getText().toString())) {
                    Toast.makeText(this, "请输入实际完成数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("0".equals(et_task_num.getText().toString().trim())) {
                    Toast.makeText(this, "实际完成数量不能为0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("sm".equals(task.getString("type"))) {
                    commitData(smScTaskBean.getStatus().getTaskid());
                } else {
                    commitData(taskDetail.getTaskid());
                }
                break;
        }
    }

    private void commitData(String taskid) {
        OkHttpUtils.post().url(Constants.baseUrl + "productionPlan/updateProductionTaskApp")
                .addParams("taskid", taskid)
                .addParams("compid", (String) GetUserInfo.getData(this, "compId", ""))
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
                                Toast.makeText(ScTaskManagerDetailActivity.this, jsonObject.optString("status"), Toast.LENGTH_SHORT).show();
                                finish();
                                EventBus.getDefault().post(new FragmentEventBusBean("RWGL", "NO"));
                            } else if ("0".equals(code)) {
                                Toast.makeText(ScTaskManagerDetailActivity.this, jsonObject.optString("status"), Toast.LENGTH_SHORT).show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRefreshEvent(FragmentEventBusBean eventBusBean) {
        Log.e("event", "收到数据");
        if ("RWGL".equals(eventBusBean.getType())) {
            finish();
        }
    }

    public void loadSmData(String planId) {
        OkHttpUtils.get().url(Constants.baseUrl + "productionPlan/queryScanProductionTask")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("planId", planId)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        if (response.toString().contains("未找到匹配的生产任务或生产任务已完成")) {
                            rl_detail.setVisibility(View.INVISIBLE);
                            tv_complete_query.setVisibility(View.INVISIBLE);
                            showDialog();
                            return;
                        }
                        rl_detail.setVisibility(View.VISIBLE);
                        tv_complete_query.setVisibility(View.VISIBLE);
                        smScTaskBean = gson.fromJson(response.toString(), SmScTaskBean.class);
                        if ("1".equals(smScTaskBean.getCode())) {
                            tv_task_order.setText("订单编号:" + smScTaskBean.getStatus().getOrderCodeNick());
                            tv_task_time.setText(smScTaskBean.getStatus().getdOrderDateStr());
                            tv_task_completTime.setText("履约日期:" + smScTaskBean.getStatus().getPerformDateStr());
                            tv_task_materName.setText(smScTaskBean.getStatus().getMaterName());
                            tv_task_num.setText(smScTaskBean.getStatus().getPlanNum() + smScTaskBean.getStatus().getDicName());
                            tv_task_remark.setText("备注信息:" + smScTaskBean.getStatus().getOrderMemo());
                            tv_task_current.setText("当前任务:" + smScTaskBean.getStatus().getProcedureName());
                            tv_task_count.setText("任务数量:" + smScTaskBean.getStatus().getTasknum() + smScTaskBean.getStatus().getDicName());
                            tv_comp_task_num.setText("任务预期完成数量为 " + smScTaskBean.getStatus().getPlanNum() + smScTaskBean.getStatus().getDicName() + "，请输入实际完成数量");
                        }
                    }
                });
    }

    /**
     * 显示没有数据的对话框
     */
    private Dialog dialog;

    private void showDialog() {
        dialog = new Dialog(this, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_sc_task_manager, null);
        //点击确定进行登录页面的跳转
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.show();
    }
}

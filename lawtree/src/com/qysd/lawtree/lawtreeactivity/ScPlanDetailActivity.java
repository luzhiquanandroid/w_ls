package com.qysd.lawtree.lawtreeactivity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.ScPlanDetailProAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.ShenChanPlanDetailBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.qysd.lawtree.lawtreeutils.wightView.PieProgressView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ScPlanDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager manager;
    private ScPlanDetailProAdapter scPlanDetailProAdapter;
    private PieProgressView pie_progress_view1;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private ShenChanPlanDetailBean shenChanPlanDetailBean;
    private SwipeRefreshLayout swipe_refresh;
    private TextView tv_order_num, tv_order_time, tv_order_materName;
    private TextView tv_order_allnum, tv_order_completTime, tv_order_remark;
    private String compid, planId, productionid;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_scplan_detail);
    }

    @Override
    protected void initView() {
        initTitle(R.drawable.ic_left_jt, "计划详情");
        compid = getIntent().getStringExtra("compid");
        planId = getIntent().getStringExtra("planId");
        productionid = getIntent().getStringExtra("productionid");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        swipe_refresh = findViewById(R.id.swipe_refresh);
        tv_order_num = findViewById(R.id.tv_order_num);
        tv_order_time = findViewById(R.id.tv_order_time);
        tv_order_materName = findViewById(R.id.tv_order_materName);
        tv_order_allnum = findViewById(R.id.tv_order_allnum);
        tv_order_completTime = findViewById(R.id.tv_order_complet_time);
        tv_order_remark = findViewById(R.id.tv_order_remark);
        //pie_progress_view1 = findViewById(R.id.pie_progress_view1);
        //pie_progress_view1.postInvalidate();
    }

    @Override
    protected void bindListener() {
        swipe_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        //pie_progress_view1.setInputData(20);
        loadData(compid, planId, productionid);
    }

    private void loadData(String compid, String planId, String productionid) {
        OkHttpUtils.get()
                .url(Constants.baseUrl + "productionPlan/productionPlanDetails")
                //.addParams("userId", (String) GetUserInfo.getData(getActivity(), "userId", ""))
                .addParams("compid", compid)
                .addParams("planId", planId)
                .addParams("productionid", productionid)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("scjh detail", response);
                        swipe_refresh.setRefreshing(false);
                        shenChanPlanDetailBean = gson.fromJson(response.toString(), ShenChanPlanDetailBean.class);
                        setDetailData();
                        setAdapter(shenChanPlanDetailBean.getStatus().getProductionPlanList());
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        swipe_refresh.setRefreshing(false);
                    }
                });
    }

    private void setDetailData() {
        tv_order_num.setText("订单编号:" + shenChanPlanDetailBean.getStatus().getOrderCodeNick());
        tv_order_time.setText(shenChanPlanDetailBean.getStatus().getdOrderDateStr());
        tv_order_completTime.setText("履约日期:" + shenChanPlanDetailBean.getStatus().getPerformDateStr());
        tv_order_materName.setText(shenChanPlanDetailBean.getStatus().getMaterName());
        tv_order_remark.setText("备注信息:" + shenChanPlanDetailBean.getStatus().getRemarks());
        tv_order_allnum.setText(shenChanPlanDetailBean.getStatus().getPlannum() + shenChanPlanDetailBean.getStatus().getDicName());
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {

    }

    private void setAdapter(final List<ShenChanPlanDetailBean.Status.ProductionPlanList> list) {
        manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        scPlanDetailProAdapter = new ScPlanDetailProAdapter(list);
        mRecyclerView.setAdapter(scPlanDetailProAdapter);
        //条目点击接口
        scPlanDetailProAdapter.setOnItemClickListener(new ScPlanDetailProAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData(compid, planId, productionid);
    }
}

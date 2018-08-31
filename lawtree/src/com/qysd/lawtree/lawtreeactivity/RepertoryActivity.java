package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.RepertoryListAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.RepertoryListBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RepertoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout refreshlayout;
    private RecyclerView recyclerview;
    private RepertoryListAdapter examinendApproveListAdapter;
    private TextView noDataTv;
    private LinearLayout ll_search;
    private int num = 1;//页码
    private int size = 6;//每一页显示个数

    private int state = 0;//0 表示刷新 1表示加载更多
    private LinearLayoutManager manager;
    private Gson gson;

    //数据
    private List<RepertoryListBean.LeaveList> repertoryList = new ArrayList<>();
    private RepertoryListBean listBean;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_repertory);
        initTitle(R.drawable.ic_left_jt,"库存管理");
    }

    @Override
    protected void initView() {
        ll_search = findViewById(R.id.ll_search);
        noDataTv = (TextView) findViewById(R.id.noDataTv);
        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        refreshlayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    protected void bindListener() {
        refreshlayout.setOnRefreshListener(this);
        ll_search.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //加载数据
        loadData(state);
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_search:
                startActivity(new Intent(this,FindRepertoryActivity.class));
                break;
        }
    }

    /**
     * 请求网络数据
     */
    private void loadData(int state) {
        if (state == 0) {
            num = 1;
        } else if (state == 1) {
            num++;
        }
        gson = new Gson();
        refreshlayout.setRefreshing(true);
        OkHttpUtils.get()
                .url(Constants.baseUrl + "/repertory/searchRepertoryDetailList")
                .addParams("page", String.valueOf(num))
                //.addParams("pageSize","10") 
                .addParams("userId", GetUserInfo.getUserId(this))
                .addParams("search", "")
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if ("没有更多数据".equals(object.optString("status"))){
                                refreshlayout.setRefreshing(false);
                                return;
                            }else if ("没有数据".equals(object.optString("status"))){
                                refreshlayout.setRefreshing(false);
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (num == 1){
                            repertoryList.clear();
                        }

                        listBean = gson.fromJson(response,RepertoryListBean.class);

                        if ("1".equals(listBean.getCode())){
                            refreshlayout.setRefreshing(false);
                            repertoryList.addAll(listBean.getStatus());
                            if (repertoryList.size()>0) {
                                if (examinendApproveListAdapter == null) {
                                    setAdapter(repertoryList);
                                } else {
                                    examinendApproveListAdapter.notifyDataSetChanged();
                                }
                            }
                        }else {
                            refreshlayout.setRefreshing(false);
                        }
                    }
                });
    }

    private void setAdapter(final List<RepertoryListBean.LeaveList> list) {
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        examinendApproveListAdapter = new RepertoryListAdapter(list);
        recyclerview.setAdapter(examinendApproveListAdapter);
        //条目点击接口
        examinendApproveListAdapter.setOnItemClickListener(new RepertoryListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        /**
         * 滚动事件接口
         */
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = manager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= manager.getItemCount() - 1) {
                        state = 1;
                        loadData(state);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        state = 0;
        loadData(state);
    }
}

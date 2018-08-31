package com.qysd.lawtree.lawtreefragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeactivity.OrderDetailActivity;
import com.qysd.lawtree.lawtreeadapter.MarketingManagementAdapter;
import com.qysd.lawtree.lawtreeadapter.RepertoryListAdapter;
import com.qysd.lawtree.lawtreebase.BaseFragment;
import com.qysd.lawtree.lawtreebean.MarketingManagementBean;
import com.qysd.lawtree.lawtreebean.RepertoryListBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class MarketingManagementFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout refreshlayout;
    private RecyclerView recyclerview;
    private MarketingManagementAdapter examinendApproveListAdapter;
    private TextView noDataTv;
    private int num = 1;//页码
    private int size = 6;//每一页显示个数

    private int state = 0;//0 表示刷新 1表示加载更多
    private LinearLayoutManager manager;
    private Gson gson;

    //数据
    private List<MarketingManagementBean.Status> repertoryList = new ArrayList<>();
    private MarketingManagementBean listBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_marketingmanagement;
    }

    @Override
    protected void initView() {
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
    protected void loadData() {

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
                .url(Constants.baseUrl + "/appsalesorder/selectList")
                .addParams("page", String.valueOf(num))
                //.addParams("pageSize","10")
                .addParams("userId", GetUserInfo.getUserId(getActivity()))
                .addParams("compId", (String) GetUserInfo.getData(getActivity(),"conmpId",""))
                .addParams("type","0")//0.未完成 1已完成（必传）
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        if (num == 1){
                            repertoryList.clear();
                        }

                        listBean = gson.fromJson(response,MarketingManagementBean.class);

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

    private void setAdapter(final List<MarketingManagementBean.Status> list) {
        manager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);
        examinendApproveListAdapter = new MarketingManagementAdapter(list);
        recyclerview.setAdapter(examinendApproveListAdapter);
        //条目点击接口
        examinendApproveListAdapter.setOnItemClickListener(new MarketingManagementAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);

                startActivity(intent);
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

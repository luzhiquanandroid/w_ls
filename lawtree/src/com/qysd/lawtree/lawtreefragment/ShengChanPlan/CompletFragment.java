package com.qysd.lawtree.lawtreefragment.ShengChanPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeactivity.ScPlanDetailActivity;
import com.qysd.lawtree.lawtreeadapter.ScPlanAdapter;
import com.qysd.lawtree.lawtreebase.BaseFragment;
import com.qysd.lawtree.lawtreebean.ShenChanPlanBean;
import com.qysd.lawtree.lawtreebusbean.ShenChanPlanEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 生产计划已经完成
 */
public class CompletFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout orderRefreshLayout;
    private RecyclerView orderRecyclerView;
    private ScPlanAdapter scPlanAdapter;
    private TextView noDataTv;
    private LinearLayoutManager manager;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private ShenChanPlanBean shenChanPlanBean;
    private int num = 1;//页码
    private int state = 0;//0 表示刷新 1表示加载更多
    private List<ShenChanPlanBean.Status> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scplan_nocomplent;
    }

    @Override
    protected void initView() {
        noDataTv = (TextView) getView().findViewById(R.id.noDataTv);
        orderRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.orderRefreshLayout);
        orderRecyclerView = (RecyclerView) getView().findViewById(R.id.orderRecyclerView);

        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        orderRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    protected void bindListener() {
        orderRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        loadData(state, startTime, endTime, materName, orderCodeNick);
    }

    @Override
    protected void initNav() {

    }

    private void loadData(int state, String startTime, String endTime, String materName, String orderCodeNick) {
        if (state == 0) {
            orderRefreshLayout.setRefreshing(true);
            num = 1;
        } else if (state == 1) {
            num++;
        }
        GetBuilder postFormBuilder = OkHttpUtils.get()
                .url(Constants.baseUrl + "productionPlan/selectProductionPlanList");
        postFormBuilder.addParams("compid", (String) GetUserInfo.getData(getActivity(),"compId",""));
        postFormBuilder.addParams("finishstate", "1");
        postFormBuilder.addParams("page", String.valueOf(num));
        if (isSelect) {
            postFormBuilder.addParams("startTime", startTime);
            postFormBuilder.addParams("endTime", endTime);
            postFormBuilder.addParams("materName", materName);
            postFormBuilder.addParams("orderCodeNick", orderCodeNick);
        }

        postFormBuilder.build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        //Log.e("scjh", response);
                        orderRefreshLayout.setRefreshing(false);
                        if (num == 1) {
                            list.clear();
                        }
                        if (response.contains("没有更多数据")) {
                            if (list.size() > 0) {
                                orderRecyclerView.setVisibility(View.VISIBLE);
                                noDataTv.setVisibility(View.INVISIBLE);
                            } else {
                                orderRecyclerView.setVisibility(View.INVISIBLE);
                                noDataTv.setVisibility(View.VISIBLE);
                            }
                            num--;
                            Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        shenChanPlanBean = gson.fromJson(response.toString(), ShenChanPlanBean.class);
                        if ("1".equals(shenChanPlanBean.getCode())) {
                            list.addAll(shenChanPlanBean.getStatus());
                            if (list.size() > 0) {
                                if (scPlanAdapter == null) {
                                    setAdapter(list);
                                } else {
                                    scPlanAdapter.notifyDataSetChanged();
                                }
                                orderRecyclerView.setVisibility(View.VISIBLE);
                                noDataTv.setVisibility(View.INVISIBLE);
                            } else {
                                orderRecyclerView.setVisibility(View.INVISIBLE);
                                noDataTv.setVisibility(View.VISIBLE);
                            }
                        } else if ("2".equals(shenChanPlanBean.getCode())) {
                            if (list.size() > 0) {
                                orderRecyclerView.setVisibility(View.VISIBLE);
                                noDataTv.setVisibility(View.INVISIBLE);
                            } else {
                                orderRecyclerView.setVisibility(View.INVISIBLE);
                                noDataTv.setVisibility(View.VISIBLE);
                            }
                            num--;
                        } else if ("0".equals(shenChanPlanBean.getCode())) {
                            showToast("请求失败，请重试");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        orderRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    protected void loadData() {

    }


    private void setAdapter(final List<ShenChanPlanBean.Status> list) {
        manager = new LinearLayoutManager(getActivity());
        orderRecyclerView.setLayoutManager(manager);
        scPlanAdapter = new ScPlanAdapter(list);
        orderRecyclerView.setAdapter(scPlanAdapter);
        //条目点击接口
        scPlanAdapter.setOnItemClickListener(new ScPlanAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ScPlanDetailActivity.class);
                intent.putExtra("productionid", list.get(position).getProductionid());
                intent.putExtra("planId", list.get(position).getPlanid());
                intent.putExtra("compid", list.get(position).getCompid());
                startActivity(intent);
            }
        });

        /**
         * 滚动事件接口
         */
        orderRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (orderRecyclerView.getChildCount() < 6) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {//当前的recycleView不滑动(滑动已经停止时)
                    int lastVisiblePosition = manager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= manager.getItemCount() - 1) {
                        state = 1;
                        loadData(state, startTime, endTime, materName, orderCodeNick);
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//当前的recycleView被拖动滑动

                }
            }
        });
    }

    @Override
    public void onRefresh() {
        isSelect = false;
        state = 0;
        loadData(state, startTime, endTime, materName, orderCodeNick);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean isSelect = false;
    private String startTime;
    private String endTime;
    private String materName;
    private String orderCodeNick;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(ShenChanPlanEventBusBean eventBusBean) {
        Log.e("event", "收到数据");
        if (eventBusBean.getCurrent() == 1) {
            isSelect = true;
            startTime = eventBusBean.getStartTime();
            endTime = eventBusBean.getEndTime();
            materName = eventBusBean.getProductName();
            orderCodeNick = eventBusBean.getOrderNum();
            loadData(state, startTime, endTime, materName, orderCodeNick);
        }
    }
}


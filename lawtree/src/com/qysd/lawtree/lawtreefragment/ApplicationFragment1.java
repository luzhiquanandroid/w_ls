package com.qysd.lawtree.lawtreefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.ApplicationAdapter;
import com.qysd.lawtree.lawtreeadapter.ApplicationViewAdapter;
import com.qysd.lawtree.lawtreebase.BaseFragment;
import com.qysd.lawtree.lawtreebean.ApplicationBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouwei on 17/4/23.
 */

public class ApplicationFragment1 extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

    private LinearLayout ll_contain;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView myapplication_recyclerView;
    private ApplicationViewAdapter mAdapter;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private Intent intent = null;

    private String mFrom;
    public static ApplicationFragment1 newInstance(String from){
        ApplicationFragment1 fragment = new ApplicationFragment1();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mFrom = getArguments().getString("from");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_application_view;
    }

    @Override
    protected void initView() {
        //initTitle("企业应用");
        ll_contain = (LinearLayout) findViewById(R.id.ll_contain);
        //refreshLayout = getActivity().findViewById(R.id.refresh);
        myapplication_recyclerView = getActivity().findViewById(R.id.myqiye_recyclerView);
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
//        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    protected void bindListener() {
        //refreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        okhttpData();
    }

    @Override
    protected void initNav() {

    }

    @Override
    protected void loadData() {

    }

    private ApplicationBean applicationBean;
    public void okhttpData(){
        OkHttpUtils.get()
                .url(Constants.baseUrl+"appIndex/indexUIList")
                .addParams("userId", "e42ee32f609b45b4a5bb03c89d9ad7c0")
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
//                        refreshLayout.setRefreshing(false);
                        applicationBean = null;
                        Log.e("songlonglong",response+"hhhhhhhhh");
                        applicationBean = gson.fromJson(response.toString(),ApplicationBean.class);
                        //applicationBean.getApplicationDataInfo().get(0).getSubMenuItems();
                        for (int i=0;i<applicationBean.getStatus().size();i++){
                            addView(i,applicationBean.getStatus().get(i).getAppName(),applicationBean);
                        }
                    }
                });
    }

    /**
     * 添加view
     * @param i
     * @param title
     * @param list
     */
    public void addView(final int i, String title, final ApplicationBean bean){
        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.layout_viewatgood, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        myapplication_recyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);

        tv_title.setText(title);

        // 为item设置id
        //view.setId(i);
        tv_title.setId(i);
        //mRecyclerView.setId(i);
        // 为item设置点击事件
        view.setOnClickListener(this);
        tv_title.setOnClickListener(this);

        mAdapter = new ApplicationViewAdapter(getActivity(), bean.getStatus().get(i).getSubMenuItems());
        //设置热门活动布局管理器
//        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(this, 4, false);
//        mRecyclerView.setLayoutManager(fullyGridLayoutManager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),4);
        myapplication_recyclerView.setLayoutManager(gridLayoutManager);
        myapplication_recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ApplicationViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),bean.getStatus().get(i).getSubMenuItems().get(position).getAppName()+position,Toast.LENGTH_SHORT).show();
            }
        });

        ll_contain.addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onRefresh() {
//        okhttpData();
    }

    /**
     * 设置适配
     */
    private LinearLayoutManager manager;
    private ApplicationAdapter applicationAdapter;
}

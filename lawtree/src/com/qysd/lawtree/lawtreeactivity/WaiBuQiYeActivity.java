package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.WaiBuQiyeAllAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/26.
 * 联系人 外部的企业
 */

public class WaiBuQiYeActivity extends BaseActivity {
    private RecyclerView recyclerview;
    private LinearLayout ll_search;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_waibu_qiye);
        initTitle(R.drawable.ic_left_jt, "外部企业");
    }

    @Override
    protected void initView() {
        recyclerview = findViewById(R.id.recyclerview);
        ll_search = findViewById(R.id.ll_search);
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WaiBuQiYeActivity.this, SearchQyActivity.class));
            }
        });
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        loadData();
    }

    private LinearLayoutManager manager;
    private Gson gson = new Gson();

    private List<WaiBuQiyeBean.Status> wbqyLists = new ArrayList<>();

    private WaiBuQiyeBean waiBuQiyeBean;

    private void loadData() {
        OkHttpUtils.get()
                .url(Constants.baseUrl + "company/selectCompanyOutList")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", "")
                )
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        waiBuQiyeBean = gson.fromJson(response.toString(), WaiBuQiyeBean.class);
                        if ("1".equals(waiBuQiyeBean.getCode())) {//请求成功
                            wbqyLists.addAll(waiBuQiyeBean.getStatus());
                            setAdapter(wbqyLists);
                        }
                    }
                });

    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View view) {

    }

    private WaiBuQiyeAllAdapter waiBuQiyeAllAdapter;

    private void setAdapter(final List<WaiBuQiyeBean.Status> list) {
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        waiBuQiyeAllAdapter = new WaiBuQiyeAllAdapter(list);
        recyclerview.setAdapter(waiBuQiyeAllAdapter);
        waiBuQiyeAllAdapter.setOnItemClickListener(new WaiBuQiyeAllAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(WaiBuQiYeActivity.this, WaiBuQiyeDetailActivity.class);
                bundle.putSerializable("status", list.get(position));
                intent.putExtra("detail", bundle);
                startActivity(intent);
            }
        });
    }
}

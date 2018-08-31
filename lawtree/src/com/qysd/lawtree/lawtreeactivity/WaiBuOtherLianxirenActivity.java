package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.WaiBuQiyeOtherAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeOtherBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/26.
 * 外部企业其他联系人
 */

public class WaiBuOtherLianxirenActivity extends BaseActivity {
    private String outId;
    private WaiBuQiyeOtherBean waiBuQiyeOtherBean;
    private Gson gson = new Gson();
    private List<WaiBuQiyeOtherBean.Status> wbqyoLists = new ArrayList<>();
    private LinearLayoutManager manager;
    private RecyclerView recyclerview;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_waibu_other_lianxiren);
        initTitle(R.drawable.ic_left_jt, "其他联系人");
    }

    @Override
    protected void initView() {
        outId = getIntent().getStringExtra("id");
        recyclerview = findViewById(R.id.recyclerview);
    }

    @Override
    protected void bindListener() {
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Constants.baseUrl + "company/selectOutUserDetail")
                .addParams("outId", outId)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        waiBuQiyeOtherBean = gson.fromJson(response.toString(), WaiBuQiyeOtherBean.class);
                        if ("1".equals(waiBuQiyeOtherBean.getCode())) {//请求成功
                            wbqyoLists.addAll(waiBuQiyeOtherBean.getStatus());
                            setAdapter(wbqyoLists);
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

    private WaiBuQiyeOtherAdapter waiBuQiyeOtherAdapter;

    private void setAdapter(final List<WaiBuQiyeOtherBean.Status> list) {
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        waiBuQiyeOtherAdapter = new WaiBuQiyeOtherAdapter(list);
        recyclerview.setAdapter(waiBuQiyeOtherAdapter);
        waiBuQiyeOtherAdapter.setOnItemClickListener(new WaiBuQiyeOtherAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
    }
}

package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.SearchQyAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.uikit.common.util.string.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页面
 */
public class SearchQyActivity extends BaseActivity {
    private EditText et_content;
    private TextView tv_search;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_serach);
    }

    @Override
    protected void initView() {
        et_content = findViewById(R.id.et_content);
        tv_search = findViewById(R.id.tv_search);
        recyclerview = findViewById(R.id.recyclerview);
    }

    @Override
    protected void bindListener() {
        tv_search.setOnClickListener(this);
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
            case R.id.tv_search:
                if (StringUtil.isEmpty(et_content.getText().toString().trim())) {
                    Toast.makeText(this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadData(et_content.getText().toString().trim());
                break;
        }
    }

    private LinearLayoutManager manager;
    private Gson gson = new Gson();

    private List<WaiBuQiyeBean.Status> wbqyLists = new ArrayList<>();

    private WaiBuQiyeBean waiBuQiyeBean;
    private MyQiyeQunLiaoBean myQiyeQunLiaoBean;
    private List<MyQiyeQunLiaoBean.Status> selectPersonBeanList = new ArrayList<>();

    private void loadData(String username) {
        wbqyLists.clear();
        selectPersonBeanList.clear();
        OkHttpUtils.get()
                .url(Constants.baseUrl + "company/selectCompanyOutList")
                .addParams("userId", "dfad7dc2bc0145dc90c2bde32f219c56"
                )
                .addParams("compName", username)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        waiBuQiyeBean = gson.fromJson(response.toString(), WaiBuQiyeBean.class);
                        if ("1".equals(waiBuQiyeBean.getCode())) {//请求成功
                            wbqyLists.addAll(waiBuQiyeBean.getStatus());
                            setAdapter(wbqyLists, selectPersonBeanList);
                        }
                    }
                });
        OkHttpUtils.get()
                .url(Constants.baseUrl + "company/selectOutCompanyUserList")
                .addParams("userId", (String) GetUserInfo.getData(this,"userId","")
                )
                .addParams("compName", username)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        myQiyeQunLiaoBean = gson.fromJson(response, MyQiyeQunLiaoBean.class);
                        selectPersonBeanList = myQiyeQunLiaoBean.getStatus();
                        setAdapter(wbqyLists, selectPersonBeanList);
                    }
                });
    }

    private RecyclerView recyclerview;
    private SearchQyAdapter searchQyAdapter;
    private Intent intent;

    private void setAdapter(final List<WaiBuQiyeBean.Status> qylist, final List<MyQiyeQunLiaoBean.Status> lxrlist) {
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        searchQyAdapter = new SearchQyAdapter(qylist, lxrlist);
        recyclerview.setAdapter(searchQyAdapter);
        searchQyAdapter.setOnItemClickListener(new SearchQyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String type) {
                if ("HEAD".equals(type)) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(SearchQyActivity.this, WaiBuQiyeDetailActivity.class);
                    bundle.putSerializable("status", qylist.get(position));
                    intent.putExtra("detail", bundle);
                    startActivity(intent);
                } else if ("BOTTOM".equals(type)) {
                    Intent intent = new Intent(SearchQyActivity.this, LianXiRenDetailActivity.class);
                    intent.putExtra("mobile", wbqyLists.get(position).getMobileNum());
                    intent.putExtra("type", "");
                    startActivity(intent);
                }
            }
        });
    }
}

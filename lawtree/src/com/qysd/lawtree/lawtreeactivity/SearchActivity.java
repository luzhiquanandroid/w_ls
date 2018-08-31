package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.SearchLxrAdapter;
import com.qysd.lawtree.lawtreeadapter.WaiBuQiyeOtherAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeOtherBean;
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
public class SearchActivity extends BaseActivity {
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

    private MyQiyeQunLiaoBean selectPersonBean;
    private Gson gson = new Gson();
    private List<MyQiyeQunLiaoBean.Status> selectPersonBeanList = new ArrayList<>();

    private void loadData(String username) {
        selectPersonBeanList.clear();
        OkHttpUtils.get()
                .url(Constants.baseUrl + "friend/selectFriendList")
                .addParams("userId", (String) GetUserInfo.getData(this,"userId",""))
                .addParams("userName", username)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        selectPersonBean = gson.fromJson(response.toString(), MyQiyeQunLiaoBean.class);
                        if ("1".equals(selectPersonBean.getCode())) {
                            selectPersonBeanList = selectPersonBean.getStatus();
                            setAdapter(selectPersonBeanList);
                        }
                    }
                });
    }

    private LinearLayoutManager manager;
    private RecyclerView recyclerview;
    private SearchLxrAdapter searchLxrAdapter;
    private Intent intent;

    private void setAdapter(final List<MyQiyeQunLiaoBean.Status> list) {
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        searchLxrAdapter = new SearchLxrAdapter(list);
        recyclerview.setAdapter(searchLxrAdapter);
        searchLxrAdapter.setOnItemClickListener(new SearchLxrAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(SearchActivity.this, LianXiRenDetailActivity.class);
                intent.putExtra("mobile",list.get(position).getMobileNum());
                intent.putExtra("type","");
                startActivity(intent);
            }
        });
    }
}

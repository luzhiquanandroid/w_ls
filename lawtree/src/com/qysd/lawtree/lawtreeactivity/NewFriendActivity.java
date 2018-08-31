package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.MyQiyeAdapter;
import com.qysd.lawtree.lawtreeadapter.NewFriendAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.MyQiyeBean;
import com.qysd.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.lawtree.lawtreebusbean.RefreshEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constant;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.LoadDialog;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新的好友
 */
public class NewFriendActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, NewFriendAdapter.OnAcceptClickListener {
    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private SelectPersonBean selectPersonBean;
    private List<SelectPersonBean.Status> selectPersonBeanList = new ArrayList<>();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_new_friend);
        initTitle(R.drawable.ic_left_jt, "新的好友");
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerview);
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    protected void bindListener() {
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        loadNewFriend();
    }

    /**
     * 请求新的好友数据
     */
    private void loadNewFriend() {
        selectPersonBeanList.clear();
        OkHttpUtils.get().url(Constants.baseUrl + "friend/selectNewFriend")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        refresh.setRefreshing(false);
                        String res = "{" + "\"" + "status" + "\"" + ":" + response + "}";
                        Log.e("lzq", res);
                        selectPersonBean = gson.fromJson(res, SelectPersonBean.class);
                        selectPersonBeanList = selectPersonBean.getStatus();
                        setAdapter(selectPersonBeanList);
                    }
                });
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onRefresh() {
        loadNewFriend();
    }

    /**
     * 设置适配
     */
    private LinearLayoutManager manager;
    private NewFriendAdapter newFriendAdapter;

    private void setAdapter(List<SelectPersonBean.Status> list) {
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        newFriendAdapter = new NewFriendAdapter(list);
        recyclerView.setAdapter(newFriendAdapter);
        newFriendAdapter.setOnAcceptClickListener(this);
        newFriendAdapter.setOnItemClickListener(new NewFriendAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    @Override
    public void onAccept(View view, int position) {
        //接受好友
        acceptFriend(selectPersonBeanList.get(position).getMobileNum(), position);
    }

    /**
     * 接受好友的网络请求
     */
    private void acceptFriend(String friendMobile, final int position) {
        LoadDialog.show(this);
        OkHttpUtils.post().url(Constants.baseUrl + "friend/agreeAddFriend")
                .addParams("userId", (String) GetUserInfo.getData(this,"userId",""))
                .addParams("friendMobile", friendMobile)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        LoadDialog.dismiss(NewFriendActivity.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if ("1".equals(jsonObject.optString("code"))) {
                                Toast.makeText(NewFriendActivity.this, "" + jsonObject.optString("status"),
                                        Toast.LENGTH_SHORT).show();
                                selectPersonBeanList.get(position).setReqStatus("1");
                                newFriendAdapter.notifyDataSetChanged();
                                EventBus.getDefault().post(new RefreshEventBusBean("RLXR"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

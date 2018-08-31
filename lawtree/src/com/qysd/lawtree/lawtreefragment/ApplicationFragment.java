package com.qysd.lawtree.lawtreefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qysd.lawtree.DemoCache;
import com.qysd.lawtree.R;
import com.qysd.lawtree.contact.activity.UserProfileSettingActivity;
import com.qysd.lawtree.lawtreeactivity.MyBusinessActivity;
import com.qysd.lawtree.lawtreeactivity.ShenChanDateActivity;
import com.qysd.lawtree.lawtreeactivity.ShenChanPlanActivity;
import com.qysd.lawtree.lawtreeactivity.TaskManagerActivity;
import com.qysd.lawtree.lawtreeactivity.OrderActivity;
import com.qysd.lawtree.lawtreeactivity.PinListActivity;
import com.qysd.lawtree.lawtreeactivity.RepertoryActivity;
import com.qysd.lawtree.lawtreeadapter.MyQiyeAdapter;
import com.qysd.lawtree.lawtreeadapter.YinyongAdapter;
import com.qysd.lawtree.lawtreebase.BaseFragment;
import com.qysd.lawtree.lawtreebean.MyQiyeBean;
import com.qysd.lawtree.lawtreebean.YingyongBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constant;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.GlideUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by zhouwei on 17/4/23.
 */

public class ApplicationFragment extends BaseFragment implements View.OnClickListener {
    private String mFrom;
    private RecyclerView recycler_view;
    private TextView tv_nodata;

    public static ApplicationFragment newInstance(String from) {
        ApplicationFragment fragment = new ApplicationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }

    @Override
    protected int getLayoutId() {
        //return R.layout.fragment_application_layout;
        return R.layout.fragment_yinyong_layout;
    }

    @Override
    protected void initView() {
        initTitle("企业应用");
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
    }

    @Override
    protected void bindListener() {
    }

    @Override
    protected void initData() {
        OkHttpUtils.get().url(Constants.baseUrl + "appIndex/indexUIList")
                .addParams("userId", (String) GetUserInfo.getData(getActivity(),"userId",""))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("eee", response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            if ("没有数据".equals(object.optString("status"))){
                                tv_nodata.setVisibility(View.VISIBLE);
                                recycler_view.setVisibility(View.GONE);
                                return;
                            }else {
                                tv_nodata.setVisibility(View.GONE);
                                recycler_view.setVisibility(View.VISIBLE);
                                yingyongBean = gson.fromJson(response.toString(), YingyongBean.class);
                                setAdapter(yingyongBean.getStatus());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void initNav() {

    }

    private YingyongBean yingyongBean;
    private Gson gson = new Gson();

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
    }


    private LinearLayoutManager manager;
    private YinyongAdapter myQiyeAdapter;

    private void setAdapter(List<YingyongBean.Status> list) {
        manager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(manager);
        myQiyeAdapter = new YinyongAdapter(list);
        recycler_view.setAdapter(myQiyeAdapter);
    }
}

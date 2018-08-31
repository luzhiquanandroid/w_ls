package com.qysd.lawtree.lawtreeactivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.GxAllAdapter;
import com.qysd.lawtree.lawtreeadapter.RoleAllAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.GxBean;
import com.qysd.lawtree.lawtreebean.RoleBean;
import com.qysd.lawtree.lawtreebusbean.AddPersonEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class GongXuSelectActivity extends BaseActivity {
    private RecyclerView role_recycle;
    private GxBean gxBean;
    private Gson gson = new Gson();
    private LinearLayoutManager manager;
    private GxAllAdapter gxAllAdapter;
    private List<String> gxId = new ArrayList<>();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_role_select);
        initTitle(R.drawable.ic_left_jt, "工序选择", "完成");

        gxId = getIntent().getStringArrayListExtra("gxId");
    }

    @Override
    protected void initView() {
        role_recycle = findViewById(R.id.role_recycle);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        loadRole();
    }

    private void loadRole() {
        OkHttpUtils.get().url(Constants.baseUrl + "company/selectProcedureListApp")
                .addParams("compId", "2")
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("gx", response.toString());
                        gxBean = gson.fromJson(response.toString(), GxBean.class);
                        if ("1".equals(gxBean.getCode())) {
                            setAdapter(gxBean.getStatus(),gxId);
                        }
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
    protected void onClickTitleRight(View v) {
        super.onClickTitleRight(v);
        List<String> nameStr = new ArrayList<>();
        List<String> idStr = new ArrayList<>();

        for (int i = 0; i < GxAllAdapter.selectedGxBeanList.size(); i++) {
            nameStr.add(GxAllAdapter.selectedGxBeanList.get(i).getProcedurename());
            idStr.add(GxAllAdapter.selectedGxBeanList.get(i).getProcedureid());
        }
        EventBus.getDefault().post(new AddPersonEventBusBean("GX", nameStr, idStr));
        finish();
    }

    private void setAdapter(final List<GxBean.Status> list, List<String> gxId) {
        manager = new LinearLayoutManager(this);
        role_recycle.setLayoutManager(manager);
        gxAllAdapter = new GxAllAdapter(list, gxId);
        role_recycle.setAdapter(gxAllAdapter);
//        roleAllAdapter.setOnItemClickListener(new RoleAllAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
    }
}

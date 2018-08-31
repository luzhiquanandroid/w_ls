package com.qysd.lawtree.lawtreeactivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.RoleAllAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.RoleBean;
import com.qysd.lawtree.lawtreebusbean.AddPersonEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class RoleSelectActivity extends BaseActivity {
    private RecyclerView role_recycle;
    private RoleBean roleBean;
    private Gson gson = new Gson();
    private LinearLayoutManager manager;
    private RoleAllAdapter roleAllAdapter;
    private List<String> roleId = new ArrayList<>();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_role_select);
        initTitle(R.drawable.ic_left_jt, "角色选择", "完成");

        roleId = getIntent().getStringArrayListExtra("roleId");
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
        OkHttpUtils.get().url(Constants.baseUrl + "company/selectRoleInfoApp")
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("data", response.toString());
                        roleBean = gson.fromJson(response.toString(), RoleBean.class);
                        if ("1".equals(roleBean.getCode())) {
                            setAdapter(roleBean.getStatus(),roleId);
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
        for (int i = 0; i < RoleAllAdapter.selectedRoleBeanList.size(); i++) {
            nameStr.add(RoleAllAdapter.selectedRoleBeanList.get(i).getRoleName());
            idStr.add(RoleAllAdapter.selectedRoleBeanList.get(i).getRoleId());
        }
        EventBus.getDefault().post(new AddPersonEventBusBean("ROLE", nameStr, idStr));
        finish();
    }

    private void setAdapter(final List<RoleBean.Status> list,List<String> roleId) {
        manager = new LinearLayoutManager(this);
        role_recycle.setLayoutManager(manager);
        roleAllAdapter = new RoleAllAdapter(list,roleId);
        role_recycle.setAdapter(roleAllAdapter);
//        roleAllAdapter.setOnItemClickListener(new RoleAllAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
    }
}

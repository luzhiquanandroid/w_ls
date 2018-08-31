package com.qysd.lawtree.lawtreeactivity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.team.model.Team;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.QunLiaoPersonPicAdapter;
import com.qysd.lawtree.lawtreeadapter.TeamGroupAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.lawtree.session.SessionHelper;
import com.qysd.lawtree.team.TeamCreateHelper;
import com.qysd.uikit.common.util.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class QunLiaoSettingActivity extends BaseActivity {
    private List<MyQiyeQunLiaoBean.Status> allList = new ArrayList<>();
    private TextView tv_qun_person_num, tv_commit;
    private EditText et_qun_name;
    private RecyclerView recyclerView;
    private List<String> qunliaoPersonList = new ArrayList<>();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_qunliao_setting);
        initTitle(R.drawable.ic_left_jt, "聊天设置");
    }

    @Override
    protected void initView() {
        Bundle mqy = getIntent().getBundleExtra("all");
        allList = (List<MyQiyeQunLiaoBean.Status>) mqy.getSerializable("alllist");
        Log.e("lzq", allList.size() + "");
        for (int i = 0; i < allList.size(); i++) {
            Log.e("lzqq", allList.get(i).getUserId());
        }
        tv_qun_person_num = findViewById(R.id.tv_qun_person_num);
        tv_commit = findViewById(R.id.tv_commit);
        et_qun_name = findViewById(R.id.et_qun_name);
        recyclerView = findViewById(R.id.recyclerview);

        tv_qun_person_num.setText(allList.size() + "人");
    }

    @Override
    protected void bindListener() {
        tv_commit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setAdapter(allList);
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //创建群聊
            case R.id.tv_commit:
                if (StringUtil.isEmpty(et_qun_name.getText().toString().trim())) {
                    Toast.makeText(this, "群名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_qun_name.getText().toString().trim().length() > 10) {
                    Toast.makeText(this, "群名称最多10个字符", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < allList.size(); i++) {
                    qunliaoPersonList.add(allList.get(i).getUserId());
                }
                TeamCreateHelper.createAdvancedTeam(this, qunliaoPersonList, et_qun_name.getText().toString().trim());
                finish();
                break;
        }
    }

    /**
     * 设置适配
     */
    private GridLayoutManager manager;
    private QunLiaoPersonPicAdapter qunLiaoPersonPicAdapter;

    private void setAdapter(final List<MyQiyeQunLiaoBean.Status> allList) {
        manager = new GridLayoutManager(this, 10);
        recyclerView.setLayoutManager(manager);
        qunLiaoPersonPicAdapter = new QunLiaoPersonPicAdapter(allList);
        recyclerView.setAdapter(qunLiaoPersonPicAdapter);

        qunLiaoPersonPicAdapter.setOnItemClickListener(new QunLiaoPersonPicAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SessionHelper.startTeamSession(QunLiaoSettingActivity.this, "15630219");
            }
        });
    }
}

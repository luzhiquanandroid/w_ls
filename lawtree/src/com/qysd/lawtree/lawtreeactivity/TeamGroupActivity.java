package com.qysd.lawtree.lawtreeactivity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.TeamGroupAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.session.SessionHelper;

import java.util.List;

/**
 * 联系人我的群组
 */

public class TeamGroupActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_team_group);
        initTitle(R.drawable.ic_left_jt, "我的群组");
    }

    @Override
    protected void initView() {
        swipeRefreshLayout = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerview);
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    protected void bindListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        NIMClient.getService(TeamService.class).queryTeamListByType(TeamTypeEnum.Advanced).setCallback(new RequestCallback<List<Team>>() {
            @Override
            public void onSuccess(List<Team> teams) {
                swipeRefreshLayout.setRefreshing(false);
                setAdapter(teams);
            }

            @Override
            public void onFailed(int i) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(TeamGroupActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(TeamGroupActivity.this, "服务器异常，请重试", Toast.LENGTH_SHORT).show();
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
        loadData();
    }

    /**
     * 设置适配
     */
    private LinearLayoutManager manager;
    private TeamGroupAdapter teamGroupAdapter;

    private void setAdapter(final List<Team> teamList) {
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        teamGroupAdapter = new TeamGroupAdapter(teamList);
        recyclerView.setAdapter(teamGroupAdapter);

        teamGroupAdapter.setOnItemClickListener(new TeamGroupAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SessionHelper.startTeamSession(TeamGroupActivity.this, teamList.get(position).getId());
            }
        });
    }
}

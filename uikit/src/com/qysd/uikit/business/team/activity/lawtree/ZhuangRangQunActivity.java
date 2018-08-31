package com.qysd.uikit.business.team.activity.lawtree;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.uikit.R;
import com.qysd.uikit.business.team.activity.lawtree.fancyindexer.adapter.PingyinAdapter;
import com.qysd.uikit.business.team.activity.lawtree.fancyindexer.ui.FancyIndexer;
import com.qysd.uikit.business.team.activity.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.uikit.business.team.helper.TeamHelper;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ZhuangRangQunActivity extends BaseActivity {
    private ExpandableListView lv_content;
    private PingyinAdapter<SelectPersonBean.Status> adapter;
    private List<SelectPersonBean.Status> selectPersonBeanList = new ArrayList<>();
    private ArrayList<String> accountList = new ArrayList<>();
    private String teamId;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_zhuanrang_qun);
        initTitle(R.drawable.ic_left_jt, "转让群");
    }

    @Override
    protected void initView() {
        Bundle all = getIntent().getBundleExtra("all");
        accountList = all.getStringArrayList("accountList");
        teamId = all.getString("teamId");
        lv_content = findViewById(R.id.lv_content);
        lv_content.setGroupIndicator(null);//设置默认的ExpandableListView的左边箭头
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        for (int i = 0; i < accountList.size(); i++) {
            Log.e("lzq accunt", accountList.get(i));
            Log.e("lzq", TeamHelper.getTeamMemberDisplayName(teamId, accountList.get(i)));
            selectPersonBeanList.add(new SelectPersonBean.Status(
                    TeamHelper.getTeamMemberDisplayName(teamId, accountList.get(i)),
                    accountList.get(i)));
        }
        loadData();
    }

    private void loadData() {
        adapter = new PingyinAdapter<SelectPersonBean.Status>(lv_content, selectPersonBeanList, R.layout.item_zr_lianxiren) {

            @Override
            public String getItemName(SelectPersonBean.Status goodMan) {
                return goodMan.getUserName();
            }

            /**
             * 返回viewholder持有
             */
            @Override
            public ViewHolder getViewHolder(SelectPersonBean.Status goodMan) {
                /**View holder*/
                class DataViewHolder extends ViewHolder implements View.OnClickListener, View.OnLongClickListener {
                    public TextView tv_name, tv_department;
                    public HeadImageView imageViewHeader;

                    public DataViewHolder(SelectPersonBean.Status goodMan) {
                        super(goodMan);
                    }

                    /**初始化*/
                    @Override
                    public ViewHolder getHolder(View view) {
                        tv_name = view.findViewById(R.id.tv_name);
                        imageViewHeader = view.findViewById(R.id.iv_person);
                        /**在这里设置点击事件*/
                        view.setOnClickListener(this);
                        view.setOnLongClickListener(this);
                        return this;
                    }

                    /**显示数据*/
                    @Override
                    public void show() {
                        tv_name.setText(getItem().getUserName());
                        imageViewHeader.loadBuddyAvatar(getItem().getUserId());
                        //GlideImgManager.loadCircleImage(LianXiRenActivity.this, getItem().getImageUrl(), iv_person);
                    }

                    /**点击事件*/
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new ZrQunLiaoEventBusBean("ZR", getItem().getUserId()));
                        finish();
//                        Intent intent = new Intent(getActivity(), LianXiRenDetailActivity.class);
//                        intent.putExtra("mobile", getItem().getMobileNum());
//                        startActivity(intent);
//                        Toast.makeText(v.getContext(), getItem().getUserName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public boolean onLongClick(View v) {
                        //长按删除好友
                        return false;
                    }
                }
                return new DataViewHolder(goodMan);
            }

            @Override
            public void onItemClick(SelectPersonBean.Status data, View view, int position) {

            }
        };
        /**展开并设置adapter*/
        adapter.expandGroup();

        com.qysd.uikit.business.team.activity.lawtree.fancyindexer.ui.FancyIndexer mFancyIndexer
                = (com.qysd.uikit.business.team.activity.lawtree.fancyindexer.ui.FancyIndexer) findViewById(R.id.bar);
        mFancyIndexer.setOnTouchLetterChangedListener(new FancyIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter) {
                for (int i = 0, j = adapter.getKeyMapList().getTypes().size(); i < j; i++) {
                    String str = adapter.getKeyMapList().getTypes().get(i);
                    if (letter.toUpperCase().equals(str.toUpperCase())) {
                        /**跳转到选中的字母表*/
                        lv_content.setSelectedGroup(i);
                    }
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
}

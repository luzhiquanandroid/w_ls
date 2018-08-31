package com.qysd.uikit.business.team.activity.lawtree;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.uikit.R;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.business.team.activity.lawtree.fancyindexer.adapter.SelectPingyinAdapter;
import com.qysd.uikit.business.team.activity.lawtree.fancyindexer.ui.FancyIndexer;
import com.qysd.uikit.business.team.activity.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.uikit.business.team.activity.lawtree.search.AddQunLiaoEventBusBeanAdd;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddQunPersonActivity extends BaseActivity {
    private RelativeLayout rl_my_qiye;//发起群聊我的企业
    private RelativeLayout rl_waibu_qiye;//发起群聊外部企业
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private List<SelectPersonBean.Status> selectPersonBeanList = new ArrayList<>();
    private SelectPersonBean selectPersonBean;
    private SelectPingyinAdapter<SelectPersonBean.Status> adapter;
    private ExpandableListView lv_content;
    private TextView tv_select_my_num, tv_select_waibu_num, tv_select_all_num, tv_option_num;
    private Intent intent = null;
    private List<String> accountList = new ArrayList<>();//群传递过来的群成员集合
    private int teamNum = 0;
    private int sTeamNum = 200;
    private int count = 0;
    private List<SelectPersonBean.Status> newselectedPersonBeanList = new ArrayList<>();
    private Bundle bundle = new Bundle();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_send_qunliao);
        initTitle(R.drawable.ic_left_jt, "邀请成员");
    }

    @Override
    protected void initView() {
        final Bundle all = getIntent().getBundleExtra("all");
        accountList = all.getStringArrayList("accountList");
        teamNum = all.getInt("teamNum");
        sTeamNum = 200 - teamNum;
        rl_my_qiye = findViewById(R.id.rl_my_qiye);
        rl_waibu_qiye = findViewById(R.id.rl_waibu_qiye);
        lv_content = findViewById(R.id.lv_content);
        lv_content.setGroupIndicator(null);//设置默认的ExpandableListView的左边箭头
        tv_select_my_num = findViewById(R.id.tv_select_my_num);
        tv_select_waibu_num = findViewById(R.id.tv_select_waibu_num);
        tv_select_all_num = findViewById(R.id.tv_select_all_num);
        tv_option_num = findViewById(R.id.tv_option_num);

        tv_select_my_num.setText("已选择：" + 0 + "人");
        tv_select_waibu_num.setText("已选择：" + 0 + "人");
        tv_option_num.setText("确定(" + 0 + "/" + sTeamNum + ")");
        if (mqyList.size() == 0) {
            tv_select_my_num.setVisibility(View.GONE);
        }
        if (wbqyList.size() == 0) {
            tv_select_waibu_num.setVisibility(View.GONE);
        }

        rl_my_qiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AddQunPersonActivity.this, AddQunPersonMqyActivity.class);
                bundle.putSerializable("accountList", (Serializable) accountList);
                if (newselectedPersonBeanList.size() > 0) {
                    for (int i = 0; i < mqyList.size(); i++) {
                        for (int j = 0; j < newselectedPersonBeanList.size(); j++) {
                            if (mqyList.get(i).getUserId().equals(newselectedPersonBeanList.get(j).getUserId())) {
                                newselectedPersonBeanList.remove(j);
                            }
                        }
                    }
                    mqyList.addAll(newselectedPersonBeanList);
                }
                bundle.putSerializable("mqylist", (Serializable) mqyList);
                intent.putExtra("mqy", bundle);
                startActivity(intent);
            }
        });
        rl_waibu_qiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AddQunPersonActivity.this, AddQunPersonWbqyActivity.class);
                bundle.putSerializable("accountList", (Serializable) accountList);
                if (newselectedPersonBeanList.size() > 0) {
                    for (int i = 0; i < wbqyList.size(); i++) {
                        for (int j = 0; j < newselectedPersonBeanList.size(); j++) {
                            if (wbqyList.get(i).getUserId().equals(newselectedPersonBeanList.get(j).getUserId())) {
                                newselectedPersonBeanList.remove(j);
                            }
                        }
                    }
                    wbqyList.addAll(newselectedPersonBeanList);
                }
                bundle.putSerializable("wbqylist", (Serializable) wbqyList);
                intent.putExtra("wbqy", bundle);
                startActivity(intent);
            }
        });
        tv_option_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < newselectedPersonBeanList.size(); i++) {
                    allList.add(newselectedPersonBeanList.get(i));
                }
                for (int i = 0; i < mqyList.size(); i++) {
                    allList.add(mqyList.get(i));
                }
                for (int i = 0; i < wbqyList.size(); i++) {
                    allList.add(wbqyList.get(i));
                }
                if (allList.size() > sTeamNum) {
                    Toast.makeText(AddQunPersonActivity.this, "群成员最多为200人", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    EventBus.getDefault().post(new AddQunLiaoEventBusBeanAdd("ADD", allList));
                    finish();
                }
            }
        });
    }

    @Override
    protected void bindListener() {
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Constants.baseUrl + "friend/selectFriendList")
                .addParams("userId", NimUIKit.getAccount())
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        selectPersonBean = gson.fromJson(response, SelectPersonBean.class);
                        selectPersonBeanList = selectPersonBean.getStatus();
//                        for (int i = 0; i < selectPersonBeanList.size(); i++) {
//                            if (accountList.contains(selectPersonBeanList.get(i).getUserId())) {
//                                count = count + 1;
//                                tv_select_all_num.setText("已选择：" + count + "人");
//                                tv_option_num.setText("确定(" + count + "/200)");
//                            }
//                        }
                        adapter = new SelectPingyinAdapter<SelectPersonBean.Status>(lv_content, selectPersonBeanList, R.layout.item_select_add_lianxiren) {

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
                                    public HeadImageView iv_person;
                                    public CheckBox checkbox;
                                    public ImageView iv_cb;

                                    public DataViewHolder(SelectPersonBean.Status goodMan) {
                                        super(goodMan);
                                    }

                                    /**初始化*/
                                    @Override
                                    public ViewHolder getHolder(View view) {
                                        iv_cb = view.findViewById(R.id.iv_cb);
                                        tv_name = (TextView) view.findViewById(R.id.tv_name);
                                        tv_department = view.findViewById(R.id.tv_department);
                                        iv_person = view.findViewById(R.id.iv_person);
                                        checkbox = view.findViewById(R.id.checkbox);
                                        /**在这里设置点击事件*/
                                        view.setOnClickListener(this);
                                        view.setOnLongClickListener(this);
                                        return this;
                                    }

                                    /**显示数据*/
                                    @Override
                                    public void show() {
                                        tv_name.setText(getItem().getUserName());
                                        tv_department.setText(getItem().getPosition());
                                        iv_person.doLoadImage(getItem().getHeadUrl(), R.drawable.ic_lianxiren_default, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);
                                        Log.e("lzq", getItem().getUserId() + "--" + accountList.contains(getItem().getUserId()));
                                        if (accountList.contains(getItem().getUserId())) {
                                            //如果包含
                                            iv_cb.setImageResource(R.drawable.shape_oval_gray_bg);
                                            //newselectedPersonBeanList.add(getItem());
                                            newIsSelected.put(getItem().getUserId(), 0);
                                        }

                                        if (newIsSelected.get(getItem().getUserId()) == 1) {
                                            iv_cb.setImageResource(R.drawable.ic_check_normal);
                                        } else if (newIsSelected.get(getItem().getUserId()) == 2) {
                                            iv_cb.setImageResource(R.drawable.ic_check_pre);
                                        }
                                    }

                                    /**点击事件*/
                                    @Override
                                    public void onClick(View v) {
                                        if (accountList.contains(getItem().getUserId())) {
                                            return;
                                        }
                                        if (newIsSelected.get(getItem().getUserId()) == 1) {
                                            iv_cb.setImageResource(R.drawable.ic_check_pre);
                                            newIsSelected.put(getItem().getUserId(), 2);
                                            newselectedPersonBeanList.add(getItem());
                                            for (int i = 0; i < mqyList.size(); i++) {
                                                if (mqyList.get(i).getUserId().equals(getItem().getUserId())) {
                                                    mqyList.add(getItem());
                                                }
                                            }

                                            for (int i = 0; i < wbqyList.size(); i++) {
                                                if (wbqyList.get(i).getUserId().equals(getItem().getUserId())) {
                                                    wbqyList.add(getItem());
                                                }
                                            }
                                        } else if (newIsSelected.get(getItem().getUserId()) == 2) {
                                            iv_cb.setImageResource(R.drawable.ic_check_normal);
                                            newIsSelected.put(getItem().getUserId(), 1);
                                            newselectedPersonBeanList.remove(getItem());
                                            for (int i = 0; i < mqyList.size(); i++) {
                                                if (mqyList.get(i).getUserId().equals(getItem().getUserId())) {
                                                    mqyList.remove(i);
                                                }
                                            }
                                            for (int i = 0; i < wbqyList.size(); i++) {
                                                if (wbqyList.get(i).getUserId().equals(getItem().getUserId())) {
                                                    wbqyList.remove(i);
                                                }
                                            }
                                        }
                                        if (mqyList.size() > 0) {
                                            tv_select_my_num.setText("已选中" + mqyList.size() + "人");
                                            tv_select_my_num.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_select_my_num.setText("已选中" + 0 + "人");
                                            tv_select_my_num.setVisibility(View.GONE);
                                        }

                                        if (wbqyList.size() > 0) {
                                            tv_select_waibu_num.setText("已选中" + wbqyList.size() + "人");
                                            tv_select_waibu_num.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_select_waibu_num.setText("已选中" + 0 + "人");
                                            tv_select_waibu_num.setVisibility(View.GONE);
                                        }
                                        tv_select_all_num.setText("已选择：" + (count + newselectedPersonBeanList.size() + mqyList.size() + wbqyList.size()) + "人");
                                        tv_option_num.setText("确定(" + (count + newselectedPersonBeanList.size() + mqyList.size() + wbqyList.size()) + "/" + sTeamNum + ")");
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
                });
    }

    @Override
    protected void initNav() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private List<SelectPersonBean.Status> mqyList = new ArrayList<>();//我的企业选择的人
    private List<SelectPersonBean.Status> wbqyList = new ArrayList<>();//外部企业选择的人
    private List<SelectPersonBean.Status> allList = new ArrayList<>();//点击确定 邀请成员的人

    @Override
    public void onClick(View v) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAddPerson(AddQunLiaoEventBusBean data) {
        switch (data.getType()) {
            case "mqyadd":
                adapter.newIsSelected.put(data.getAccount(), data.getStatus());
                if (data.getStatus() == 1) {
                    for (int i = 0; i < newselectedPersonBeanList.size(); i++) {
                        if (newselectedPersonBeanList.get(i).getUserId().equals(data.getAccount())) {
                            newselectedPersonBeanList.remove(i);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case "wbqyadd":
                adapter.newIsSelected.put(data.getAccount(), data.getStatus());
                if (data.getStatus() == 1) {
                    for (int i = 0; i < newselectedPersonBeanList.size(); i++) {
                        if (newselectedPersonBeanList.get(i).getUserId().equals(data.getAccount())) {
                            newselectedPersonBeanList.remove(i);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case "mqy":
                mqyList = data.getSelectList();
                if (mqyList.size() > 0) {
                    tv_select_my_num.setText("已选中" + mqyList.size() + "人");
                    tv_select_my_num.setVisibility(View.VISIBLE);
                } else {
                    tv_select_my_num.setText("已选中" + 0 + "人");
                    tv_select_my_num.setVisibility(View.GONE);
                }
                break;
            case "wbqy":
                wbqyList = data.getSelectList();
                if (wbqyList.size() > 0) {
                    tv_select_waibu_num.setText("已选中" + wbqyList.size() + "人");
                    tv_select_waibu_num.setVisibility(View.VISIBLE);
                } else {
                    tv_select_waibu_num.setText("已选中" + 0 + "人");
                    tv_select_waibu_num.setVisibility(View.GONE);
                }
                break;
        }
        tv_select_all_num.setText("已选择：" + (newselectedPersonBeanList.size() + mqyList.size() + wbqyList.size()) + "人");
        tv_option_num.setText("确定(" + (newselectedPersonBeanList.size() + mqyList.size() + wbqyList.size()) + "/" + sTeamNum + ")");
    }
}

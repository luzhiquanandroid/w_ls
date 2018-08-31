package com.qysd.lawtree.lawtreeactivity;

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
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.lawtree.lawtreebusbean.QunLiaoEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.qysd.lawtree.lawtreeview.fancyindexer.adapter.SelectPingyinAdapter;
//import com.qysd.lawtree.lawtreeview.fancyindexer.ui.FancyIndexer;
import com.qysd.uikit.business.team.activity.lawtree.fancyindexer.ui.FancyIndexer;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/27.
 * 发起群聊
 */

public class SendQunLiaoActivity extends BaseActivity {
    private RelativeLayout rl_my_qiye;//发起群聊我的企业
    private RelativeLayout rl_waibu_qiye;//发起群聊外部企业
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private List<MyQiyeQunLiaoBean.Status> selectPersonBeanList = new ArrayList<>();
    private MyQiyeQunLiaoBean selectPersonBean;
    private SelectPingyinAdapter<MyQiyeQunLiaoBean.Status> adapter;
    private ExpandableListView lv_content;
    private List<MyQiyeQunLiaoBean.Status> selectedPersonBeanList = new ArrayList<>();
    private TextView tv_select_my_num, tv_select_waibu_num, tv_select_all_num, tv_option_num;
    private Intent intent = null;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_send_qunliao);
        initTitle(R.drawable.ic_left_jt, "发起群聊");
    }

    @Override
    protected void initView() {
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
        tv_option_num.setText("确定(" + 0 + "/200)");
        if (mqyList.size() == 0) {
            tv_select_my_num.setVisibility(View.GONE);
        }
        if (wbqyList.size() == 0) {
            tv_select_waibu_num.setVisibility(View.GONE);
        }
    }

    @Override
    protected void bindListener() {
        rl_my_qiye.setOnClickListener(this);
        rl_waibu_qiye.setOnClickListener(this);
        tv_option_num.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Constants.baseUrl + "friend/selectFriendList")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        selectPersonBean = gson.fromJson(response.toString(), MyQiyeQunLiaoBean.class);
//                        if ("1".equals(selectPersonBean.getCode())) {
                        selectPersonBeanList = selectPersonBean.getStatus();
                        adapter = new SelectPingyinAdapter<MyQiyeQunLiaoBean.Status>(lv_content, selectPersonBeanList, R.layout.item_select_lianxiren) {

                            @Override
                            public String getItemName(MyQiyeQunLiaoBean.Status goodMan) {
                                return goodMan.getUserName();
                            }

                            /**
                             * 返回viewholder持有
                             */
                            @Override
                            public ViewHolder getViewHolder(MyQiyeQunLiaoBean.Status goodMan) {
                                /**View holder*/
                                class DataViewHolder extends ViewHolder implements View.OnClickListener, View.OnLongClickListener {
                                    public TextView tv_name, tv_department;
                                    public HeadImageView iv_person;
                                    public CheckBox checkbox;

                                    public DataViewHolder(MyQiyeQunLiaoBean.Status goodMan) {
                                        super(goodMan);
                                    }

                                    /**初始化*/
                                    @Override
                                    public ViewHolder getHolder(View view) {
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
                                        iv_person.doLoadImage(getItem().getHeadUrl(),R.drawable.ic_lianxiren_default,HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);
                                        checkbox.setChecked(isSelected.get(getItem().getUserId()));
                                    }

                                    /**点击事件*/
                                    @Override
                                    public void onClick(View v) {
                                        checkbox.setChecked(!checkbox.isChecked());
                                        isSelected.put(getItem().getUserId(), checkbox.isChecked());
                                        if (isSelected.get(getItem().getUserId())) {
                                            selectedPersonBeanList.add(getItem());
                                        } else {
                                            selectedPersonBeanList.remove(getItem());
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
                                        tv_select_all_num.setText("已选择：" + (selectedPersonBeanList.size() + mqyList.size() + wbqyList.size()) + "人");
                                        tv_option_num.setText("确定(" + (selectedPersonBeanList.size() + mqyList.size() + wbqyList.size()) + "/200)");
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
                            public void onItemClick(MyQiyeQunLiaoBean.Status data, View view, int position) {

                            }
                        };
                        /**展开并设置adapter*/
                        adapter.expandGroup();

                        FancyIndexer mFancyIndexer = (FancyIndexer) findViewById(R.id.bar);
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
//                        }
                    }
                });
    }

    @Override
    protected void initNav() {

    }

    private Bundle bundle = new Bundle();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_qiye:
                if (mqyList.size() == 0) {
                    mqyList.addAll(selectedPersonBeanList);
                } else {
                    for (int i = 0; i < selectedPersonBeanList.size(); i++) {
                        for (int j = 0; j < mqyList.size(); j++) {
                            if (selectedPersonBeanList.get(i).getUserId().equals(
                                    mqyList.get(j).getUserId())) {
                                mqyList.remove(j);
                            }
                        }
                    }
                    mqyList.addAll(selectedPersonBeanList);
                }
                Log.e("alllll", mqyList.size() + "");
                intent = new Intent(this, QunLiaoMyQiyeActivity.class);
                bundle.putSerializable("mqylist", (Serializable) mqyList);
                intent.putExtra("mqy", bundle);
                startActivity(intent);
                break;
            case R.id.rl_waibu_qiye:
                if (wbqyList.size() == 0) {
                    wbqyList.addAll(selectedPersonBeanList);
                } else {
                    for (int i = 0; i < selectedPersonBeanList.size(); i++) {
                        for (int j = 0; j < wbqyList.size(); j++) {
                            if (selectedPersonBeanList.get(i).getUserId().equals(
                                    wbqyList.get(j).getUserId())) {
                                wbqyList.remove(j);
                            }
                        }
                    }
                    wbqyList.addAll(selectedPersonBeanList);
                }
                Log.e("alllll", mqyList.size() + "");
                intent = new Intent(this, QunLiaoWaibuQiyeActivity.class);
                bundle.putSerializable("wbqylist", (Serializable) wbqyList);
                intent.putExtra("wbqy", bundle);
                startActivity(intent);
                break;
            case R.id.tv_option_num:
                allList.clear();
                for (int i = 0; i < selectedPersonBeanList.size(); i++) {
                    Log.e("select", selectedPersonBeanList.get(i).getUserId());
                    allList.add(selectedPersonBeanList.get(i));
                }
                for (int i = 0; i < mqyList.size(); i++) {
                    Log.e("mqy", mqyList.get(i).getUserId());
                    allList.add(mqyList.get(i));
                }
                for (int i = 0; i < wbqyList.size(); i++) {
                    Log.e("wbqy", wbqyList.get(i).getUserId());
                    allList.add(wbqyList.get(i));
                }
                for (int i = 0; i < allList.size(); i++) {
                    Log.e("all", allList.get(i).getUserId());
                }
                if ((allList.size()) < 2) {
                    Toast.makeText(this, "群聊不能少于3人", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((allList.size() > 200)) {
                    Toast.makeText(this, "群聊不能多于200人", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(this, QunLiaoSettingActivity.class);
                bundle.putSerializable("alllist", (Serializable) allList);
                intent.putExtra("all", bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private List<MyQiyeQunLiaoBean.Status> mqyList = new ArrayList<>();
    private List<MyQiyeQunLiaoBean.Status> wbqyList = new ArrayList<>();
    private List<MyQiyeQunLiaoBean.Status> allList = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSelectPerson(QunLiaoEventBusBean data) {
        if ("mqyc".equals(data.getType())) {
            SelectPingyinAdapter.isSelected.put(data.getUserId(),
                    !SelectPingyinAdapter.isSelected.get(data.getUserId())
            );
            for (int i = 0; i < selectedPersonBeanList.size(); i++) {
                if (selectedPersonBeanList.get(i).getUserId().equals(data.getUserId())) {
                    selectedPersonBeanList.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
        } else if ("wbqyc".equals(data.getType())) {
            SelectPingyinAdapter.isSelected.put(data.getUserId(),
                    !SelectPingyinAdapter.isSelected.get(data.getUserId())
            );
            for (int i = 0; i < selectedPersonBeanList.size(); i++) {
                if (selectedPersonBeanList.get(i).getUserId().equals(data.getUserId())) {
                    selectedPersonBeanList.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
        }
        if ("mqy".equals(data.getType())) {
            mqyList = data.getSelectList();
            if (mqyList.size() > 0) {
                tv_select_my_num.setText("已选中" + mqyList.size() + "人");
                tv_select_my_num.setVisibility(View.VISIBLE);
            } else {
                tv_select_my_num.setText("已选中" + 0 + "人");
                tv_select_my_num.setVisibility(View.GONE);
            }
        } else if ("wbqy".equals(data.getType())) {
            wbqyList = data.getSelectList();
            if (wbqyList.size() > 0) {
                tv_select_waibu_num.setText("已选中" + wbqyList.size() + "人");
                tv_select_waibu_num.setVisibility(View.VISIBLE);
            } else {
                tv_select_waibu_num.setText("已选中" + 0 + "人");
                tv_select_waibu_num.setVisibility(View.GONE);
            }
        }
        if (mqyList.size() > 0 && selectedPersonBeanList.size() > 0) {
            for (int i = 0; i < mqyList.size(); i++) {
                for (int j = 0; j < selectedPersonBeanList.size(); j++) {
                    if (mqyList.get(i).getUserId().equals(selectedPersonBeanList.get(j).getUserId())) {
                        selectedPersonBeanList.remove(j);
                    }
                }
            }
        }
        if (wbqyList.size() > 0 && selectedPersonBeanList.size() > 0) {
            for (int i = 0; i < wbqyList.size(); i++) {
                for (int j = 0; j < selectedPersonBeanList.size(); j++) {
                    if (wbqyList.get(i).getUserId().equals(selectedPersonBeanList.get(j).getUserId())) {
                        selectedPersonBeanList.remove(j);
                    }
                }
            }
        }
        tv_select_all_num.setText("已选择：" + (selectedPersonBeanList.size() + mqyList.size() + wbqyList.size()) + "人");
        tv_option_num.setText("确定(" + (selectedPersonBeanList.size() + mqyList.size() + wbqyList.size()) + "/200)");
    }
}

package com.qysd.uikit.business.team.activity.lawtree;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.uikit.R;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.business.team.activity.lawtree.fancyindexer.adapter.SelectPingyinAdapter;
import com.qysd.uikit.business.team.activity.lawtree.fancyindexer.adapter.WbqySelectPingyinAdapter;
import com.qysd.uikit.business.team.activity.lawtree.fancyindexer.ui.FancyIndexer;
import com.qysd.uikit.business.team.activity.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AddQunPersonWbqyActivity extends BaseActivity {
    private ExpandableListView lv_content;
    private SelectPersonBean selectPersonBean;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private List<SelectPersonBean.Status> selectPersonBeanList = new ArrayList<>();
    private WbqySelectPingyinAdapter<SelectPersonBean.Status> adapter;
    private List<SelectPersonBean.Status> selectedPersonBeanList = new ArrayList<>();

    private List<String> accountList = new ArrayList<>();//传递过来的群成员

    private int count = 0;
    private List<SelectPersonBean.Status> newselectedPersonBeanList = new ArrayList<>();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_add_qunliao_myqiye);
        initTitle(R.drawable.ic_left_jt, "外部企业");
    }

    @Override
    protected void initView() {
        Bundle mqy = getIntent().getBundleExtra("wbqy");
        selectedPersonBeanList = (List<SelectPersonBean.Status>) mqy.getSerializable("wbqylist");
        accountList = (List<String>) mqy.getSerializable("accountList");
        lv_content = findViewById(R.id.lv_content);
        lv_content.setGroupIndicator(null);//设置默认的ExpandableListView的左边箭头
//        View inflate = LayoutInflater.from(this).inflate(R.layout.sendqunliao_head_layout, null);
//        lv_content.addHeaderView(inflate);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        loadData();
    }


    /**
     * 请求网络数据
     */
    private void loadData() {
        OkHttpUtils.get().url(Constants.baseUrl + "company/selectOutCompanyUserList")
                .addParams("userId", NimUIKit.getAccount())
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        Log.e("lzq", response.toString());
                        selectPersonBean = gson.fromJson(response, SelectPersonBean.class);
                        selectPersonBeanList = selectPersonBean.getStatus();
                        for (int i = 0; i < selectPersonBeanList.size(); i++) {
                            if (accountList.contains(selectPersonBeanList.get(i).getUserId())) {
                                count = count + 1;
                                //tv_select_all_num.setText("已选择：" + count + "人");
                                //tv_option_num.setText("确定(" + count + "/200)");
                            }
                        }
                        adapter = new WbqySelectPingyinAdapter<SelectPersonBean.Status>(lv_content, selectPersonBeanList, R.layout.item_select_add_lianxiren) {

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
                                        } else {
                                            newIsSelected.put(getItem().getUserId(), 1);
                                        }

                                        for (int i = 0; i < selectedPersonBeanList.size(); i++) {
                                            newIsSelected.put(selectedPersonBeanList.get(i).getUserId(), 2);
                                            newselectedPersonBeanList.add(selectedPersonBeanList.get(i));
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
                                        } else if (newIsSelected.get(getItem().getUserId()) == 2) {
                                            iv_cb.setImageResource(R.drawable.ic_check_normal);
                                            newIsSelected.put(getItem().getUserId(), 1);
                                            for (int i = 0; i < newselectedPersonBeanList.size(); i++) {
                                                if (getItem().getUserId().equals(newselectedPersonBeanList.get(i).getUserId())) {
                                                    newselectedPersonBeanList.remove(i);
                                                }
                                            }
                                            Log.e("userid--", getItem().getUserId());
                                            EventBus.getDefault().post(new AddQunLiaoEventBusBean("wbqyadd", getItem().getUserId(), 1));
                                        }
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
    public void onClick(View view) {

    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(new AddQunLiaoEventBusBean("wbqy", newselectedPersonBeanList));
        Log.e("wbqy add", newselectedPersonBeanList.size() + "");
        super.onDestroy();
    }
}

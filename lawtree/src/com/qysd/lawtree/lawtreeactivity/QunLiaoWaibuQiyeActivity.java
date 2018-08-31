package com.qysd.lawtree.lawtreeactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.qysd.lawtree.lawtreeview.fancyindexer.adapter.WaiBuQiyePingyinAdapter;
import com.qysd.lawtree.lawtreeview.fancyindexer.ui.FancyIndexer;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 发起群聊外部的企业
 */

public class QunLiaoWaibuQiyeActivity extends BaseActivity {
    private ExpandableListView lv_content;
    private MyQiyeQunLiaoBean selectPersonBean;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private List<MyQiyeQunLiaoBean.Status> selectPersonBeanList = new ArrayList<>();
    private WaiBuQiyePingyinAdapter<MyQiyeQunLiaoBean.Status> adapter;
    private List<MyQiyeQunLiaoBean.Status> selectedPersonBeanList = new ArrayList<>();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_qunliao_waibuqiye);
        initTitle(R.drawable.ic_left_jt, "外部企业");
    }

    @Override
    protected void initView() {
        Bundle wbqy = getIntent().getBundleExtra("wbqy");
        selectedPersonBeanList = (List<MyQiyeQunLiaoBean.Status>) wbqy.getSerializable("wbqylist");
        Log.e("lzq", selectedPersonBeanList.size() + "");
        lv_content = findViewById(R.id.lv_content);
        lv_content.setGroupIndicator(null);//设置默认的ExpandableListView的左边箭头
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        loadData();
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 请求网络数据
     */
    private void loadData() {
        OkHttpUtils.get().url(Constants.baseUrl + "company/selectOutCompanyUserList")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", "")
                )
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        selectPersonBean = gson.fromJson(response, MyQiyeQunLiaoBean.class);
                        selectPersonBeanList = selectPersonBean.getStatus();
                        adapter = new WaiBuQiyePingyinAdapter<MyQiyeQunLiaoBean.Status>(lv_content, selectPersonBeanList, R.layout.item_select_lianxiren) {

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
                                        iv_person.doLoadImage(getItem().getHeadUrl(), R.drawable.ic_lianxiren_default, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);
                                        for (int i = 0; i < selectedPersonBeanList.size(); i++) {
                                            isSelected.put(selectedPersonBeanList.get(i).getUserId(), true);
                                        }
                                        checkbox.setChecked(isSelected.get(getItem().getUserId()));
                                    }

                                    /**点击事件*/
                                    @Override
                                    public void onClick(View v) {
                                        EventBus.getDefault().post(new QunLiaoEventBusBean("wbqyc", getItem().getUserId()));
                                        checkbox.setChecked(!checkbox.isChecked());
                                        isSelected.put(getItem().getUserId(), checkbox.isChecked());
                                        if (isSelected.get(getItem().getUserId())) {
                                            selectedPersonBeanList.add(getItem());
                                        } else {
                                            //selectedPersonBeanList.remove(getItem());
                                            for (int i = 0; i < selectedPersonBeanList.size(); i++) {
                                                if (getItem().getUserId().equals(selectedPersonBeanList.get(i).getUserId())) {
                                                    selectedPersonBeanList.remove(i);
                                                }
                                            }
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
                            public void onItemClick(MyQiyeQunLiaoBean.Status data, View view, int position) {

                            }
                        };
                        /**展开并设置adapter*/
                        adapter.expandGroup();

                        FancyIndexer mFancyIndexer = findViewById(R.id.bar);
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
    protected void onClickTitleRight(View v) {
        //点击确定 发送选择的人回群聊首页
        EventBus.getDefault().post(new QunLiaoEventBusBean("wbqy", selectedPersonBeanList));
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(new QunLiaoEventBusBean("wbqy", selectedPersonBeanList));
        super.onDestroy();
    }
}

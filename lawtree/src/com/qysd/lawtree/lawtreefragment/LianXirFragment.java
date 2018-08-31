package com.qysd.lawtree.lawtreefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeactivity.AddFriendActivity;
import com.qysd.lawtree.lawtreeactivity.LianXiRenDetailActivity;
import com.qysd.lawtree.lawtreeactivity.MyQiYeActivity;
import com.qysd.lawtree.lawtreeactivity.NewFriendActivity;
import com.qysd.lawtree.lawtreeactivity.ScanZxingCode;
import com.qysd.lawtree.lawtreeactivity.SearchActivity;
import com.qysd.lawtree.lawtreeactivity.SendQunLiaoActivity;
import com.qysd.lawtree.lawtreeactivity.TeamGroupActivity;
import com.qysd.lawtree.lawtreeactivity.WaiBuQiYeActivity;
import com.qysd.lawtree.lawtreebase.BaseFragment;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.lawtree.lawtreebusbean.NewFriendCountEventBusBean;
import com.qysd.lawtree.lawtreebusbean.RefreshEventBusBean;
import com.qysd.lawtree.lawtreeutils.CommonPopupWindow;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.LoadDialog;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.qysd.lawtree.lawtreeview.fancyindexer.adapter.PingyinAdapter;
import com.qysd.lawtree.lawtreeview.fancyindexer.ui.FancyIndexer;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.qysd.uikit.common.util.string.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.qysd.uikit.common.ui.imageview.HeadImageView.DEFAULT_AVATAR_THUMB_SIZE;

public class LianXirFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private CommonPopupWindow window;
    private CommonPopupWindow.LayoutGravity layoutGravity;
    private String mFrom;
    private ExpandableListView lv_content;//联系人列表
    private PingyinAdapter<MyQiyeQunLiaoBean.Status> adapter;
    private MyQiyeQunLiaoBean selectPersonBean;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private List<MyQiyeQunLiaoBean.Status> selectPersonBeanList = new ArrayList<>();
    //我的企业 外部企业 我的群组
    private RelativeLayout rl_my_qiye, rl_waibu_qiye, rl_wode_qunzu;
    //页面上部添加按钮
    private ImageView iv_lianxiren_add;
    private LinearLayout ll_send_qunliao, ll_saoyisao, ll_add_friend;
    private RelativeLayout rl_layout;//新的好友
    private LinearLayout ll_search;//搜索
    private SwipeRefreshLayout swipe_refresh;//刷新
    private TextView tv_newFriend;//新好友个数

    public static LianXirFragment newInstance(String from) {
        LianXirFragment fragment = new LianXirFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lianxiren_fragment_layout;
    }

    @Override
    protected void initView() {
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        lv_content = getActivity().findViewById(R.id.lv_content);
        lv_content.setGroupIndicator(null);//设置默认的ExpandableListView的左边箭头
        View head_view = LayoutInflater.from(getActivity()).inflate(R.layout.lianxiren_head_layout, null);
        lv_content.addHeaderView(head_view);
        rl_my_qiye = head_view.findViewById(R.id.rl_my_qiye);
        rl_waibu_qiye = head_view.findViewById(R.id.rl_waibu_qiye);
        rl_wode_qunzu = head_view.findViewById(R.id.rl_wode_qunzu);
        rl_layout = head_view.findViewById(R.id.rl_layout);
        ll_search = head_view.findViewById(R.id.ll_search);
        tv_newFriend = head_view.findViewById(R.id.tv_newFriend);
        if ((int) (GetUserInfo.getData(getActivity(), "newFriend", 0)) == 0) {
            tv_newFriend.setVisibility(View.INVISIBLE);
        } else {
            tv_newFriend.setVisibility(View.VISIBLE);
            tv_newFriend.setText(GetUserInfo.getData(getActivity(), "newFriend", 0) + "");
        }
        iv_lianxiren_add = getActivity().findViewById(R.id.iv_lianxiren_add);
        swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        window = new CommonPopupWindow(getActivity(), R.layout.add_popupwindow_layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                ll_send_qunliao = view.findViewById(R.id.ll_send_qunliao);
                ll_saoyisao = view.findViewById(R.id.ll_saoyisao);
                ll_add_friend = view.findViewById(R.id.ll_add_friend);
            }

            @Override
            protected void initEvent() {
                ll_send_qunliao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        window.getPopupWindow().dismiss();
                        startActivity(new Intent(getActivity(), SendQunLiaoActivity.class));
                    }
                });
                ll_saoyisao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        window.getPopupWindow().dismiss();
                        startActivity(new Intent(getActivity(), ScanZxingCode.class));
                        //Toast.makeText(context, "扫一扫", Toast.LENGTH_SHORT).show();
                    }
                });
                ll_add_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        window.getPopupWindow().dismiss();
                        startActivity(new Intent(getActivity(), AddFriendActivity.class));
                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getActivity().getWindow().setAttributes(lp);
                    }
                });
            }
        };
        layoutGravity = new CommonPopupWindow.LayoutGravity(CommonPopupWindow.LayoutGravity.ALIGN_LEFT | CommonPopupWindow.LayoutGravity.TO_BOTTOM);
    }

    @Override
    protected void bindListener() {
        iv_lianxiren_add.setOnClickListener(this);
        rl_layout.setOnClickListener(this);
        rl_wode_qunzu.setOnClickListener(this);
        rl_waibu_qiye.setOnClickListener(this);
        rl_my_qiye.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        swipe_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        LoadDialog.show(getActivity());
        OkHttpUtils.get()
                .url(Constants.baseUrl + "friend/selectFriendList")
                .addParams("userId", (String) GetUserInfo.getData(getActivity(), "userId", ""))
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("lianxiren",response.toString());
                        LoadDialog.dismiss(getActivity());
                        swipe_refresh.setRefreshing(false);
                        if (response.toString().length() > 0) {
                            selectPersonBean = gson.fromJson(response.toString(), MyQiyeQunLiaoBean.class);
                            selectPersonBeanList = selectPersonBean.getStatus();
                            adapter = new PingyinAdapter<MyQiyeQunLiaoBean.Status>(lv_content, selectPersonBeanList, R.layout.item_lianxiren) {

                                @Override
                                public String getItemName(MyQiyeQunLiaoBean.Status goodMan) {
                                    Log.e("lzq", goodMan.getUserName());
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

                                        public DataViewHolder(MyQiyeQunLiaoBean.Status goodMan) {
                                            super(goodMan);
                                        }

                                        /**初始化*/
                                        @Override
                                        public ViewHolder getHolder(View view) {
                                            tv_name = (TextView) view.findViewById(R.id.tv_name);
                                            tv_department = view.findViewById(R.id.tv_department);
                                            iv_person = view.findViewById(R.id.iv_person);
                                            /**在这里设置点击事件*/
                                            view.setOnClickListener(this);
                                            view.setOnLongClickListener(this);
                                            return this;
                                        }

                                        /**显示数据*/
                                        @Override
                                        public void show() {
                                            tv_name.setText(getItem().getUserName());
                                            if (StringUtil.isEmpty(getItem().getCompName()) && StringUtil.isEmpty(getItem().getPosition())) {
                                                tv_department.setText("");
                                            } else {
                                                tv_department.setText(
                                                        StringUtil.isEmpty(getItem().getCompName()) ? "" : getItem().getCompName() +
                                                                (StringUtil.isEmpty(getItem().getPosition()) ? "" : ("—" + getItem().getPosition()))
                                                );
                                            }
                                            iv_person.doLoadImage(getItem().getHeadUrl(), R.drawable.ic_lianxiren_default, DEFAULT_AVATAR_THUMB_SIZE);
                                        }

                                        /**点击事件*/
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), LianXiRenDetailActivity.class);
                                            intent.putExtra("mobile", getItem().getMobileNum());
                                            intent.putExtra("type", "");
                                            startActivity(intent);
                                            //Toast.makeText(v.getContext(), getItem().getUserName(), Toast.LENGTH_SHORT).show();
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

                            FancyIndexer mFancyIndexer = (FancyIndexer) getView().findViewById(R.id.bar);
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
                    }
                });
    }

    @Override
    protected void initNav() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_qiye:
                if (!"".equals(GetUserInfo.getData(getActivity(),"entecode",""))){
                    startActivity(new Intent(getActivity(), MyQiYeActivity.class));
                }else {
                    Toast.makeText(getActivity(),"您暂无企业，请前往律树网页端进行创建",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_waibu_qiye:
                if (!"".equals(GetUserInfo.getData(getActivity(),"entecode",""))){
                    startActivity(new Intent(getActivity(), WaiBuQiYeActivity.class));
                }else {
                    Toast.makeText(getActivity(),"您暂无企业，请前往律树网页端进行创建",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rl_wode_qunzu:
                startActivity(new Intent(getActivity(), TeamGroupActivity.class));
                break;
            case R.id.iv_lianxiren_add:
                window.showBashOfAnchor(iv_lianxiren_add, layoutGravity, 0, 0);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.3f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
                break;
            case R.id.rl_layout:
                //云信个人页面
                //UserProfileActivity.start(getActivity(), "17778139781");
                //startActivity(new Intent(getActivity(), LianXiRenDetailActivity.class));
                startActivity(new Intent(getActivity(), NewFriendActivity.class));
                GetUserInfo.putData(getActivity(), "newFriend", 0);
                tv_newFriend.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_search:
                //搜索页面
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRefreshLxr(RefreshEventBusBean bean) {
        if ("RLXR".equals(bean.getType())) {
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNewFriendCount(NewFriendCountEventBusBean bean) {
        if ("newFriend".equals(bean.getType())) {
            tv_newFriend.setVisibility(View.VISIBLE);
            tv_newFriend.setText(bean.getCount() + "");
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }


}

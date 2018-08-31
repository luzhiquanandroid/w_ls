package com.qysd.lawtree.lawtreeactivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.LianXiRenDetailBean;
import com.qysd.lawtree.lawtreebusbean.RefreshEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.CommonPopupWindow;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.LoadDialog;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.qysd.lawtree.lawtreeutils.PhoneUtils;
import com.qysd.lawtree.session.SessionHelper;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.qysd.uikit.common.util.string.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by QYSD_AD on 2018/3/27.
 * 联系人详情
 */

public class LianXiRenDetailActivity extends BaseActivity {
    private TextView tv_send_message;
    private TextView tv_bottom_add_friend;
    private LinearLayout ll_beizhu, ll_delete_friend, ll_lianxiren_detail;
    private View view_line;
    private CommonPopupWindow window;
    private CommonPopupWindow.LayoutGravity layoutGravity;
    private String mobile = "";
    private LianXiRenDetailBean lianXiRenDetailBean;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private TextView tv_detail_position, tv_detail_name,
            tv_detail_comp_name, tv_detail_phone, tv_detail_sign;
    private RelativeLayout rl_tel;//打电话
    private HeadImageView iv_detail_person;//图像
    private TextView tv_detail_send_message;//发送消息
    private LinearLayout ll_detail;
    private String typeId = "";
    private String account = "";

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_lianxiren_detail);
        initTitle(R.drawable.ic_left_jt, "联系人详情", R.drawable.ic_more);
        mobile = getIntent().getStringExtra("mobile");
        typeId = getIntent().getStringExtra("type");
        account = getIntent().getStringExtra("account");
    }

    @Override
    protected void initView() {
        tv_bottom_add_friend = findViewById(R.id.tv_bottom_add_friend);
        iv_detail_person = findViewById(R.id.iv_detail_person);
        rl_tel = findViewById(R.id.rl_tel);
        tv_send_message = findViewById(R.id.tv_send_message);
        tv_detail_name = findViewById(R.id.tv_detail_name);
        tv_detail_position = findViewById(R.id.tv_detail_position);
        tv_detail_comp_name = findViewById(R.id.tv_detail_comp_name);
        tv_detail_phone = findViewById(R.id.tv_detail_phone);
        tv_detail_sign = findViewById(R.id.tv_detail_sign);
        ll_detail = findViewById(R.id.ll_detail);
        tv_detail_send_message = findViewById(R.id.tv_detail_send_message);
    }

    @Override
    protected void bindListener() {
        tv_send_message.setOnClickListener(this);
        tv_detail_send_message.setOnClickListener(this);
        rl_tel.setOnClickListener(this);
        tv_bottom_add_friend.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        window = new CommonPopupWindow(this, R.layout.more_popupwindow_layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                ll_beizhu = view.findViewById(R.id.ll_beizhu);
                ll_delete_friend = view.findViewById(R.id.ll_delete_friend);
                ll_lianxiren_detail = view.findViewById(R.id.ll_lianxiren_detail);
                view_line = view.findViewById(R.id.view_line);
            }

            @Override
            protected void initEvent() {
                ll_beizhu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        window.getPopupWindow().dismiss();
                        mUpdateBeiZhu();
                    }
                });
                ll_delete_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //删除好友
                        window.getPopupWindow().dismiss();
                        deleteFriend(mobile);
                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
        layoutGravity = new CommonPopupWindow.LayoutGravity(CommonPopupWindow.LayoutGravity.ALIGN_LEFT | CommonPopupWindow.LayoutGravity.TO_BOTTOM);

        if ("typeId".equals(typeId)) {
            Log.e("lzq detail", typeId);
            loadIdDetailData();
        } else {
            loadDetailData();
        }
    }

    /**
     * 修改备注
     */
    private void updateBeiZhu(String beizhu) {
        LoadDialog.show(this);
        OkHttpUtils.post().url(Constants.baseUrl + "friend/updateFriendMemo")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("friendMobile", mobile)
                .addParams("memoName", beizhu)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        LoadDialog.dismiss(LianXiRenDetailActivity.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if ("1".equals(jsonObject.optString("code"))) {
                                Toast.makeText(LianXiRenDetailActivity.this, "" +
                                        jsonObject.optString("status"), Toast.LENGTH_SHORT).show();
                                //只要修改成功
                                if ("typeId".equals(typeId)) {
                                    Log.e("lzq detail", typeId);
                                    loadIdDetailData();
                                } else {
                                    loadDetailData();
                                }
                                EventBus.getDefault().post(new RefreshEventBusBean("RLXR"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 加载详情数据
     */
    private void loadDetailData() {
        //7dbc673447ff4fbeb2b8232ce8d7e3d8
        LoadDialog.show(this);
        OkHttpUtils.get().url(Constants.baseUrl + "friend/selectFriendDetail")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("friendMobile", mobile)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        LoadDialog.dismiss(LianXiRenDetailActivity.this);
                        Log.e("lzq phone", response.toString());
                        lianXiRenDetailBean = gson.fromJson(response.toString(), LianXiRenDetailBean.class);
                        mobile = lianXiRenDetailBean.getStatus().getMobileNum();
                        tv_detail_name.setText(lianXiRenDetailBean.getStatus().getUserName());
                        if (StringUtil.isEmpty(lianXiRenDetailBean.getStatus().getPosition())) {
                            tv_detail_position.setVisibility(View.GONE);
                        } else {
                            tv_detail_position.setVisibility(View.VISIBLE);
                            tv_detail_position.setText(lianXiRenDetailBean.getStatus().getPosition());
                        }

                        tv_detail_phone.setText(lianXiRenDetailBean.getStatus().getMobileNum());
                        tv_detail_sign.setText(lianXiRenDetailBean.getStatus().getSign());
                        tv_detail_comp_name.setText(lianXiRenDetailBean.getStatus().getCompName());
//                        if ("1".equals(lianXiRenDetailBean.getStatus().getReqStatus())) {
//                            tv_detail_send_message.setVisibility(View.VISIBLE);
//                            ll_detail.setVisibility(View.GONE);
//                        }
                        showView(lianXiRenDetailBean.getStatus().getReqStatus(), lianXiRenDetailBean.getStatus().getCompId());
                        iv_detail_person.doLoadImage(lianXiRenDetailBean.getStatus().getHeadUrl(), R.drawable.ic_lianxiren_default, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);

                    }
                });
    }

    /**
     * 通过id查
     */
    private void loadIdDetailData() {
        LoadDialog.show(this);
        OkHttpUtils.get().url(Constants.baseUrl + "friend/selectFriendDetailById")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("friendId", account)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        LoadDialog.dismiss(LianXiRenDetailActivity.this);
                        Log.e("lzq id", response.toString());
                        lianXiRenDetailBean = gson.fromJson(response.toString(), LianXiRenDetailBean.class);
                        mobile = lianXiRenDetailBean.getStatus().getMobileNum();
                        tv_detail_name.setText(lianXiRenDetailBean.getStatus().getUserName());
                        tv_detail_phone.setText(lianXiRenDetailBean.getStatus().getMobileNum());
                        tv_detail_sign.setText(lianXiRenDetailBean.getStatus().getSign());
                        tv_detail_comp_name.setText(lianXiRenDetailBean.getStatus().getCompName());
//                        if ("1".equals(lianXiRenDetailBean.getStatus().getReqStatus())) {
//                            tv_detail_send_message.setVisibility(View.VISIBLE);
//                            ll_detail.setVisibility(View.GONE);
//                        }
                        showView(lianXiRenDetailBean.getStatus().getReqStatus(), lianXiRenDetailBean.getStatus().getCompId());
                        iv_detail_person.doLoadImage(lianXiRenDetailBean.getStatus().getHeadUrl(), R.drawable.ic_lianxiren_default, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);
                        if (StringUtil.isEmpty(lianXiRenDetailBean.getStatus().getPosition())) {
                            tv_detail_position.setVisibility(View.GONE);
                        } else {
                            tv_detail_position.setVisibility(View.VISIBLE);
                            tv_detail_position.setText(lianXiRenDetailBean.getStatus().getPosition());
                        }
                    }
                });
    }


    /**
     * 删除好友
     */
    private void deleteFriend(String friendMobile) {
        LoadDialog.show(this);
        OkHttpUtils.post().url(Constants.baseUrl + "friend/delFriend")
                .addParams("userId", (String) GetUserInfo.getData(LianXiRenDetailActivity.this,
                        "userId", ""))
                .addParams("friendMobile", friendMobile)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        LoadDialog.dismiss(LianXiRenDetailActivity.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if ("1".equals(jsonObject.optString("code"))) {
                                Toast.makeText(LianXiRenDetailActivity.this, "删除好友成功", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new RefreshEventBusBean("RLXR"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_message:
                SessionHelper.startP2PSession(this, lianXiRenDetailBean.getStatus().getUserId());
                break;
            case R.id.tv_detail_send_message:
                SessionHelper.startP2PSession(this, lianXiRenDetailBean.getStatus().getUserId());
                break;
            case R.id.rl_tel:
                PhoneUtils.openDial(this, tv_detail_phone.getText().toString());
                break;
            case R.id.tv_bottom_add_friend:
                //添加好友
                addFriend(mobile);
                break;
        }
    }

    @Override
    protected void onClickTitleRight(View v) {
        super.onClickTitleRight(v);
        //如果当前是自己就隐藏修改备注 发消息和加好友等按钮操作
        if (lianXiRenDetailBean.getStatus().getUserId().endsWith(
                (String) GetUserInfo.getData(this, "userId", ""))) {
            ll_detail.setVisibility(View.GONE);
            tv_detail_send_message.setVisibility(View.GONE);
            return;
        }
        window.showBashOfAnchor(v, layoutGravity, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    /**
     * 弹出修改备注对的对话框
     */
    private Dialog mUpdateBeiZhu = null;

    public void mUpdateBeiZhu() {
        mUpdateBeiZhu = new Dialog(this, R.style.AlertDialogStyle);
        View view = View.inflate(this, R.layout.dialog_update_beizhu, null);
        final EditText et_beizhu = view.findViewById(R.id.et_beizhu);
        view.findViewById(R.id.tv_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(et_beizhu.getText().toString().trim())) {
                    mUpdateBeiZhu.dismiss();
                    return;
                }
                //判断是不是好友
                boolean isMyFriend = NIMClient.getService(FriendService.class).isMyFriend(lianXiRenDetailBean.getStatus().getUserId());
                if (isMyFriend) {//是好友直接修改本地备注 和云信备注
                    Map<FriendFieldEnum, Object> map = new HashMap<>();
                    map.put(FriendFieldEnum.ALIAS, et_beizhu.getText().toString().trim());
                    NIMClient.getService(FriendService.class).updateFriendFields(lianXiRenDetailBean.getStatus().getUserId(), map);
                    updateBeiZhu(et_beizhu.getText().toString().trim());
                } else {//不是好友 添加好友 修改本地备注 和云信备注
                    final VerifyType verifyType = VerifyType.DIRECT_ADD;
                    NIMClient.getService(FriendService.class).addFriend(new AddFriendData(lianXiRenDetailBean.getStatus().getUserId(), verifyType))
                            .setCallback(new RequestCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Map<FriendFieldEnum, Object> map = new HashMap<>();
                                    map.put(FriendFieldEnum.ALIAS, et_beizhu.getText().toString().trim());
                                    Log.e("hhhh", lianXiRenDetailBean.getStatus().getUserId());
                                    NIMClient.getService(FriendService.class).updateFriendFields(lianXiRenDetailBean.getStatus().getUserId(), map);
                                    updateBeiZhu(et_beizhu.getText().toString().trim());
                                }

                                @Override
                                public void onFailed(int i) {

                                }

                                @Override
                                public void onException(Throwable throwable) {

                                }
                            });
                }
                mUpdateBeiZhu.dismiss();
                mUpdateBeiZhu = null;
            }
        });
        mUpdateBeiZhu.setContentView(view);
        mUpdateBeiZhu.setCanceledOnTouchOutside(true);
        mUpdateBeiZhu.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mUpdateBeiZhu.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mUpdateBeiZhu.show();
    }

    @SuppressLint("ResourceAsColor")
    public void showView(String reqStatus, String compId) {
        //如果当前是自己就隐藏修改备注 发消息和加好友等按钮操作
        if (lianXiRenDetailBean.getStatus().getUserId().endsWith(
                (String) GetUserInfo.getData(this, "userId", ""))) {
            ll_detail.setVisibility(View.GONE);
            tv_detail_send_message.setVisibility(View.GONE);
            return;
        }
        if ("1".equals(reqStatus)) {
            //强关系（好友）
            tv_detail_send_message.setVisibility(View.VISIBLE);
            ll_detail.setVisibility(View.GONE);
            ll_delete_friend.setVisibility(View.VISIBLE);
            ll_beizhu.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            //ll_lianxiren_detail.setBackgroundResource(R.drawable.ic_pop_two);
        } else if ("3".equals(reqStatus)) {
            //3 不是好友
            ll_detail.setVisibility(View.VISIBLE);
            tv_detail_send_message.setVisibility(View.GONE);
            ll_delete_friend.setVisibility(View.GONE);
            ll_beizhu.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.GONE);
        } else {
            //相等 弱关系（同事但不是好友）
            if (GetUserInfo.getData(LianXiRenDetailActivity.this, "compId", "").equals(compId)) {
                ll_detail.setVisibility(View.VISIBLE);
                tv_detail_send_message.setVisibility(View.GONE);
                ll_delete_friend.setVisibility(View.GONE);
                ll_beizhu.setVisibility(View.VISIBLE);
                view_line.setVisibility(View.GONE);
                //ll_lianxiren_detail.setBackgroundResource(R.drawable.ic_pop);
            } else {//无关系
                initTitle(R.drawable.ic_left_jt, "联系人详情");
                ll_lianxiren_detail.setVisibility(View.GONE);
                ll_detail.setVisibility(View.GONE);
                tv_detail_send_message.setVisibility(View.GONE);
            }
        }
    }

    //添加好友的请求接口
    private void addFriend(final String phone) {
        OkHttpUtils.post().url(Constants.baseUrl + "friend/addFriend")
                .addParams("mobile", phone)
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("addType", "1")//添加来源(0：扫码 1：手机号码 2：组织架构 3：邀请添加4：群组添加)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String code = jsonObject.optString("code");
                            String status = jsonObject.optString("status");
                            if ("0".equals(code)) {
                                Toast.makeText(LianXiRenDetailActivity.this, "" +
                                        status, Toast.LENGTH_SHORT).show();
                            } else if ("2".equals(code)) {
                                Toast.makeText(LianXiRenDetailActivity.this, "" +
                                        status, Toast.LENGTH_SHORT).show();
                            } else if ("1".equals(code)) {
                                loadDetailData();
                            } else if ("3".equals(code)) {
                                Toast.makeText(LianXiRenDetailActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                            }
                            EventBus.getDefault().post(new RefreshEventBusBean("RLXR"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

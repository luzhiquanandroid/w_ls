package com.qysd.lawtree.lawtreeactivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.qysd.avchatkit.AVChatProfile;
import com.qysd.avchatkit.activity.AVChatActivity;
import com.qysd.avchatkit.constant.AVChatExtras;
import com.qysd.lawtree.R;
import com.qysd.lawtree.config.preference.UserPreferences;
import com.qysd.lawtree.lawtreebusbean.RedCountEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.DataGenerator;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.login.LogoutHelper;
import com.qysd.lawtree.main.activity.MainActivity;
import com.qysd.lawtree.main.helper.SystemMessageUnreadManager;
import com.qysd.lawtree.main.reminder.ReminderItem;
import com.qysd.lawtree.main.reminder.ReminderManager;
import com.qysd.lawtree.session.SessionHelper;
import com.qysd.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.qysd.uikit.business.chatroom.helper.ChatRoomHelper;
import com.qysd.uikit.common.activity.UI;
import com.qysd.uikit.common.ui.dialog.DialogMaker;
import com.qysd.uikit.common.ui.drop.DropCover;
import com.qysd.uikit.common.ui.drop.DropManager;
import com.qysd.uikit.common.util.log.LogUtil;
import com.qysd.uikit.impl.cache.ChatRoomMemberCache;
import com.qysd.uikit.support.permission.MPermission;
import com.qysd.uikit.support.permission.annotation.OnMPermissionDenied;
import com.qysd.uikit.support.permission.annotation.OnMPermissionGranted;
import com.qysd.uikit.support.permission.annotation.OnMPermissionNeverAskAgain;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by zhouwei on 17/4/23.
 */

public class BottomTabLayoutActivity extends UI implements View.OnClickListener, ReminderManager.UnreadNumChangedCallback {
    private TabLayout mTabLayout;
    private Fragment[] mFragmensts;

    //网易云
    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private static final String TAG = BottomTabLayoutActivity.class.getSimpleName();
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private final String MY_ACTION = "com.qysd.lawtree.receiver.DistanceLoginReceiver.action.MYACTION";

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, BottomTabLayoutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    // 注销
    public static void logout(Context context, boolean quit) {
        Intent extra = new Intent();
        extra.putExtra(EXTRA_APP_QUIT, quit);
        start(context, extra);
    }

    @Override
    protected boolean displayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_tab_layout_ac);
        EventBus.getDefault().register(this);
        mFragmensts = DataGenerator.getFragments("通讯录");
        initView();
        requestBasicPermission();

        onParseIntent();

        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {

                syncPushNoDisturb(UserPreferences.getStatusConfig());

                DialogMaker.dismissProgressDialog();
            }
        });

        LogUtil.i(TAG, "sync completed = " + syncCompleted);
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(BottomTabLayoutActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        } else {
            syncPushNoDisturb(UserPreferences.getStatusConfig());
        }

        onInit();
        //极光推送设置别名
        //jpushAlias();

        //默认进入设置 状态为离线
        //updateStatus("3");
        //头像
        //GlideImgManager.loadRoundCornerImage2(this, GetUserInfo.getHeaderUrl(this), userImg);
        //版本更新
        //updateApp();

        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode>() {
                    public void onEvent(StatusCode status) {
                        Log.i("tag", "User status changed to: " + status);
                        if (status.wontAutoLogin()) {
                            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                            LogoutHelper.logout();
                            Intent myIntent = new Intent();
                            myIntent.setAction(MY_ACTION);
                            myIntent.putExtra("msg", "");
                            BottomTabLayoutActivity.this.sendBroadcast(myIntent);
                        }
                    }
                }, true);
        //同步个人信息
        userData();

    }

    private TextView tvRedCount;

    private void initView() {
        mTabLayout = findViewById(R.id.bottom_tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if (i == tab.getPosition()) {
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    } else {
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < 4; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }
        for (int i = 0; i < 4; i++) {
            View view = mTabLayout.getTabAt(i).getCustomView();
            if (i == 0) {
                tvRedCount = view.findViewById(R.id.tvRedCount);
                if (NIMClient.getService(MsgService.class).getTotalUnreadCount() > 0) {
                    tvRedCount.setVisibility(View.VISIBLE);
                    tvRedCount.setText(NIMClient.getService(MsgService.class).getTotalUnreadCount() + "");
                } else {
                    tvRedCount.setVisibility(View.GONE);
                }
            }
        }

    }

    /**
     * 同步个人资料
     */
    public void userData(){
        OkHttpUtils.post()
                .url(Constants.baseUrl+"userapp/selectMyInfo")
                .addParams("userId", GetUserInfo.getUserId(this))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"deptId",object.optString("deptId"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"userId",object.optString("userId"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"userName",object.optString("userName"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"position",object.optString("position"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"mobileNum",object.optString("mobileNum"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"sign",object.optString("sign"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"idCard",object.optString("idCard"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"birthDay",object.optString("birthDay"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"sex",object.optString("sex"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"compName",object.optString("compName"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"reqStatus",object.optString("reqStatus"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"birthDayStr",object.optString("birthDayStr"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"headUrl",object.optString("headUrl"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"compId",object.optString("compId"));
                            myCompanyData(object.optString("userId"));//同步我的企业信息
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void myCompanyData(String userId) {
        OkHttpUtils.get()
                .url(Constants.baseUrl+"company/selectMyCompany")
                .addParams("userId",userId)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"compname",object.optString("compname"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"entecode",object.optString("entecode"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"trade",object.optString("trade"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"address",object.optString("address"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"logourl",object.optString("logourl"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"legalperson",object.optString("legalperson"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"contectnum",object.optString("contectnum"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"createby",object.optString("createby"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"createtime",object.optString("createtime"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"registercode",object.optString("registercode"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"scopmuch",object.optString("scopmuch"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"createTimeStr",object.optString("createTimeStr"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"provinceName",object.optString("provinceName"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"cityName",object.optString("cityName"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"countyName",object.optString("countyName"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"province",object.optString("province"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"city",object.optString("city"));
                            GetUserInfo.putData(BottomTabLayoutActivity.this,"county",object.optString("county"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void onTabItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = mFragmensts[0];
                break;
            case 1:
                fragment = mFragmensts[1];
                break;

            case 2:
                fragment = mFragmensts[2];
                break;
            case 3:
                fragment = mFragmensts[3];
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        Log.e("unread main", item.getUnread() + "");
        tvRedCount.setText(NIMClient.getService(MsgService.class).getTotalUnreadCount() + "");
    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    /**
     * 初始化未读红点动画
     */
    private void initUnreadCover() {
        DropManager.getInstance().init(this, (DropCover) findView(R.id.unread_cover),
                new DropCover.IDropCompletedListener() {
                    @Override
                    public void onCompleted(Object id, boolean explosive) {
                        if (id == null || !explosive) {
                            return;
                        }

                        if (id instanceof RecentContact) {
                            RecentContact r = (RecentContact) id;
                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                            LogUtil.i("HomeFragment", "clearUnreadCount, sessionId=" + r.getContactId());
                        } else if (id instanceof String) {
                            if (((String) id).contentEquals("0")) {
                                List<RecentContact> recentContacts = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
                                for (RecentContact r : recentContacts) {
                                    if (r.getUnreadCount() > 0) {
                                        NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                                    }
                                }
                                LogUtil.i("HomeFragment", "clearAllUnreadCount");
                            } else if (((String) id).contentEquals("1")) {
                                NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
                                LogUtil.i("HomeFragment", "clearAllSystemUnreadCount");
                            }
                        }
                    }
                });
    }


    /**
     * 若增加第三方推送免打扰（V3.2.0新增功能），则：
     * 1.添加下面逻辑使得 push 免打扰与先前的设置同步。
     * 2.设置界面{@link } 以及
     * 免打扰设置界面{@link } 也应添加 push 免打扰的逻辑
     * <p>
     * 注意：isPushDndValid 返回 false， 表示未设置过push 免打扰。
     */
    private void syncPushNoDisturb(StatusBarNotificationConfig staConfig) {

        boolean isNoDisbConfigExist = NIMClient.getService(MixPushService.class).isPushNoDisturbConfigExist();

        if (!isNoDisbConfigExist && staConfig.downTimeToggle) {
            NIMClient.getService(MixPushService.class).setPushNoDisturbConfig(staConfig.downTimeToggle,
                    staConfig.downTimeBegin, staConfig.downTimeEnd);
        }
    }

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(BottomTabLayoutActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        //Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    private void onInit() {
        // 加载主页面
        //showMainFragment();
        // 聊天室初始化
        //ChatRoomHelper.init();
        ChatRoomMemberCache.getInstance().clear();
        ChatRoomMemberCache.getInstance().registerObservers(true);
        LogUtil.ui("NIM SDK cache path=" + NIMClient.getSdkStorageDirPath());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //initData();
        onParseIntent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    private void onParseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            switch (message.getSessionType()) {
                case P2P:
                    SessionHelper.startP2PSession(this, message.getSessionId());
                    break;
                case Team:
                    SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
                default:
                    break;
            }
        } else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
            return;
        } else if (intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
            if (AVChatProfile.getInstance().isAVChatting()) {
                Intent localIntent = new Intent();
                localIntent.setClass(this, AVChatActivity.class);
                startActivity(localIntent);
            }
        } else if (intent.hasExtra(AVChatExtras.EXTRA_FROM_NOTIFICATION)) {
            String account = intent.getStringExtra(AVChatExtras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        }
    }

    // 注销

    private void onLogout() {

        // 清理缓存&注销监听
        LogoutHelper.logout();
        // 启动登录
        //NewLoginActivity.start(this);
        //RouteManager.getInstance().toLoginStartActivity(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRedCount(RedCountEventBusBean data) {
        if (data.getCount() > 0) {
            tvRedCount.setVisibility(View.VISIBLE);
            tvRedCount.setText(data.getCount() + "");
        } else {
            tvRedCount.setVisibility(View.GONE);
            tvRedCount.setText(data.getCount() + "");
        }

    }
}

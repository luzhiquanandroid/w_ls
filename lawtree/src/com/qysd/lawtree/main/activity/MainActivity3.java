package com.qysd.lawtree.main.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.qysd.avchatkit.AVChatProfile;
import com.qysd.avchatkit.activity.AVChatActivity;
import com.qysd.lawtree.DemoCache;
import com.qysd.lawtree.R;
import com.qysd.lawtree.config.preference.UserPreferences;
import com.qysd.lawtree.lawtreeactivity.ScanZxingCode;
import com.qysd.lawtree.lawtreefragment.ApplicationFragment;
import com.qysd.lawtree.lawtreefragment.ApplicationFragment1;
import com.qysd.lawtree.lawtreefragment.LianXirFragment;
import com.qysd.lawtree.lawtreefragment.MeFragment;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.login.LoginActivity;
import com.qysd.lawtree.login.LogoutHelper;
import com.qysd.lawtree.main.fragment.HomeFragment;
import com.qysd.lawtree.main.fragment.SessionListFragment;
import com.qysd.lawtree.main.helper.SystemMessageUnreadManager;
import com.qysd.lawtree.main.reminder.ReminderItem;
import com.qysd.lawtree.main.reminder.ReminderManager;
import com.qysd.lawtree.session.SessionHelper;
import com.qysd.lawtree.team.TeamCreateHelper;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.qysd.uikit.business.contact.selector.activity.ContactSelectActivity;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.qysd.lawtree.main.model.Extras.EXTRA_ACCOUNT;
import static com.qysd.lawtree.main.model.Extras.EXTRA_DATA;
import static com.qysd.lawtree.main.model.Extras.EXTRA_JUMP_P2P;
import static com.qysd.lawtree.main.model.MainTab.RECENT_CONTACTS;


/**
 * Created by QYSD_GT on 2017/3/10.
 */

public class MainActivity3 extends UI implements View.OnClickListener, ReminderManager.UnreadNumChangedCallback {
    public static final String KEY_TYPE = "KEY_TYPE";
    private LinearLayout ll_tab_saoyisao;
    private TextView tvMessage;
    private TextView tvLxr;
    private TextView tvYingyong;
    private TextView tvMy;
    @IdRes
    private int contentId;
    @Type
    private int mType;
    private SparseArray<Fragment> sparseArray;//存放fragment
    private SessionListFragment sessionListFragment;//网易云消息
    private long lastChickTime = 0;//上次点击返回键的时间
    //网易云
    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private static final String TAG = MainActivity3.class.getSimpleName();
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private TextView tvRedCount;
    private final String MY_ACTION = "com.qysd.elvfu.receiver.DistanceLoginReceiver.action.MYACTION";
    private HomeFragment mainFragment;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity3.class);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //初始化好友的数量
        GetUserInfo.putData(this, "newFriend", GetUserInfo.getData(this, "newFriend", 0));
        initView();

        initBadgeTotal(NIMClient.getService(MsgService.class).getTotalUnreadCount());

        initListener();
        initData();

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
            DialogMaker.showProgressDialog(MainActivity3.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        } else {
            syncPushNoDisturb(UserPreferences.getStatusConfig());
        }


        //默认进入设置 状态为离线  请求以前状态进行设置
        Log.e("hhh", GetUserInfo.getData(MainActivity3.this, "stuts", "") + "");

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
                            MainActivity3.this.sendBroadcast(myIntent);
                        }
                    }
                }, true);

        //同步个人信息
        userData();
        //极光推送设置别名
        jpushAlias();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableMsgNotification(false);

        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();

        //initUnreadCover();
    }

    @Override
    public void onPause() {
        super.onPause();
        enableMsgNotification(true);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        //当杀死进程的时候不在设置为默认离线状态
        //updateStatus();
        LogUtil.ui("activity: " + getClass().getSimpleName() + " onDestroy()");
        super.onDestroy();
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
    }

    private void enableMsgNotification(boolean enable) {
        boolean msg = (mType != RECENT_CONTACTS.tabIndex);
        if (enable | msg) {
            /**
             * 设置最近联系人的消息为已读
             *
             * @param account,    聊天对象帐号，或者以下两个值：
             *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
             *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
             */
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 未读消息数量观察者实现
     */
    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        Log.e("unread main", item.getUnread() + "");
        initBadgeTotal(NIMClient.getService(MsgService.class).getTotalUnreadCount());
        //MainTab tab = MainTab.fromReminderId(item.getId());
//        if (tab != null) {
//            tabs.updateTab(tab.tabIndex, item);
//        }
    }

    public void initBadgeTotal(int total) {
        tvRedCount.setText(total + "");
        if (total <= 0) {
            tvRedCount.setVisibility(View.INVISIBLE);
        } else {
            tvRedCount.setVisibility(View.VISIBLE);
        }
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
     * 2.设置界面{@link MainActivity3} 以及
     * 免打扰设置界面{@link NoDisturbActivity} 也应添加 push 免打扰的逻辑
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
        MPermission.with(MainActivity3.this)
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
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
        } else if (intent.hasExtra(EXTRA_JUMP_P2P)) {
            Intent data = intent.getParcelableExtra(EXTRA_DATA);
            String account = data.getStringExtra(EXTRA_ACCOUNT);
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
        LoginActivity.start(this);
        //RouteManager.getInstance().toLoginStartActivity(this);
        finish();
    }

    protected void initView() {
        //this.ll_saoyisao = findViewById(R.id.ll_saoyisao);
        this.contentId = R.id.layContent;
        this.tvMessage = (TextView) findViewById(R.id.tvMessage);
        this.tvLxr = (TextView) findViewById(R.id.tvLxr);
        this.tvYingyong = (TextView) findViewById(R.id.tvYingYong);
        this.tvMy = (TextView) findViewById(R.id.tvMy);
        tvRedCount = (TextView) findViewById(R.id.tvRedCount);
        ll_tab_saoyisao=findViewById(R.id.ll_tab_saoyisao);

    }

    protected void initListener() {
        ll_tab_saoyisao.setOnClickListener(this);
        this.tvYingyong.setOnClickListener(this);
        this.tvMessage.setOnClickListener(this);
        this.tvLxr.setOnClickListener(this);
        this.tvMy.setOnClickListener(this);
    }

    protected void initData() {
        if (sparseArray == null) {
            sparseArray = new SparseArray<>();
            //云信的homeFragment 不是自己写的
            sparseArray.put(MESSAGE, new HomeFragment());
            sparseArray.put(LXR, new LianXirFragment());
            sparseArray.put(YINGYONG, new ApplicationFragment());
            sparseArray.put(MY, new MeFragment());
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(KEY_TYPE)) {
                switch (getIntent().getExtras().getInt(KEY_TYPE)) {
                    case MESSAGE:
                        showFragment(MESSAGE);
                        break;
                    case LXR:
                        showFragment(LXR);
                        break;
                    case YINGYONG:
                        showFragment(YINGYONG);
                        break;
                    case MY:
                        showFragment(MY);
                        break;

                    default:
                        showFragment(MESSAGE);
                }
            }
        } else {
            showFragment(MESSAGE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMessage:
                showFragment(MESSAGE);
                enableMsgNotification(false);
                break;
            case R.id.tvLxr:
                showFragment(LXR);
                enableMsgNotification(false);
                break;
            case R.id.tvYingYong://点击客户时候
                showFragment(YINGYONG);
                enableMsgNotification(false);
                break;
            case R.id.tvMy:
                showFragment(MY);
                enableMsgNotification(false);
                break;

           case R.id.ll_tab_saoyisao:
               Intent intent = new Intent(this, ScanZxingCode.class);
               startActivity(intent);
               break;
        }
    }

    private void showFragment(@Type int type) {
//        if ((type == MAIL || type == HOME || type == MY) /*&& !UserManager.getInstance().isLogin()*/) {
//            //RouteManager.getInstance().toLogin(this);
//            return;
//        }
        if (sparseArray == null || mType == type) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (sparseArray.get(mType) != null) {
            transaction.hide(sparseArray.get(mType));
        }
        Fragment f = sparseArray.get(mType = type);
        if (f != null) {
            if (f.isAdded()) {
                transaction.show(f);
            } else {
                transaction.add(contentId, f);
            }
        }
        transaction.commitAllowingStateLoss();
        tvMessage.setSelected(false);
        tvLxr.setSelected(false);
        tvYingyong.setSelected(false);
        tvMy.setSelected(false);
        switch (mType) {
            case MESSAGE:
                tvMessage.setSelected(true);
                break;
            case LXR:
                tvLxr.setSelected(true);
                break;
            case YINGYONG:
                tvYingyong.setSelected(true);
                break;
            case MY:
                tvMy.setSelected(true);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        if (mType != MESSAGE) {
            showFragment(MESSAGE);
        } else {
            if (System.currentTimeMillis() - lastChickTime > 1500) {
                //Toast.makeText(MainActivity3.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                lastChickTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (sparseArray != null && sparseArray.get(mType) != null) {
            sparseArray.get(mType).onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    TeamCreateHelper.createNormalTeam(MainActivity3.this, selected, false, null);
                } else {
                    Toast.makeText(MainActivity3.this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                TeamCreateHelper.createAdvancedTeam(MainActivity3.this, selected, "");
            }
        }
    }

    //region Type
    /**
     * 消息
     */
    public static final int MESSAGE = 1;
    /**
     * 联系人
     */
    public static final int LXR = 2;
    /**
     * 应用
     */
    public static final int YINGYONG = 3;
    /**
     * 我
     */
    public static final int MY = 4;

    @IntDef({MESSAGE, LXR, YINGYONG, MY})
    public @interface Type {
    }

    /**
     * 同步个人资料
     */
    public void userData() {
        OkHttpUtils.post()
                .url(Constants.baseUrl + "userapp/selectMyInfo")
                .addParams("userId", GetUserInfo.getUserId(this))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            GetUserInfo.putData(MainActivity3.this, "deptId", object.optString("deptId"));
                            GetUserInfo.putData(MainActivity3.this, "userId", object.optString("userId"));
                            DemoCache.setAccount(object.optString("userId"));
                            GetUserInfo.putData(MainActivity3.this, "userName", object.optString("userName"));
                            GetUserInfo.putData(MainActivity3.this, "position", object.optString("position"));
                            GetUserInfo.putData(MainActivity3.this, "mobileNum", object.optString("mobileNum"));
                            GetUserInfo.putData(MainActivity3.this, "sign", object.optString("sign"));
                            GetUserInfo.putData(MainActivity3.this, "idCard", object.optString("idCard"));
                            GetUserInfo.putData(MainActivity3.this, "birthDay", object.optString("birthDay"));
                            GetUserInfo.putData(MainActivity3.this, "sex", object.optString("sex"));
                            GetUserInfo.putData(MainActivity3.this, "compName", object.optString("compName"));
                            GetUserInfo.putData(MainActivity3.this, "reqStatus", object.optString("reqStatus"));
                            GetUserInfo.putData(MainActivity3.this, "birthDayStr", object.optString("birthDayStr"));
                            GetUserInfo.putData(MainActivity3.this, "headUrl", object.optString("headUrl"));
                            GetUserInfo.putData(MainActivity3.this, "compId", object.optString("compId"));
                            //GetUserInfo.putData(MainActivity3.this, "isAdmin", object.getString("isAdmin"));
                            myCompanyData(object.optString("userId"));//同步我的企业信息
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void myCompanyData(String userId) {
        OkHttpUtils.get()
                .url(Constants.baseUrl + "company/selectMyCompany")
                .addParams("userId", userId)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong", response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            GetUserInfo.putData(MainActivity3.this, "compname", object.optString("compname"));
                            GetUserInfo.putData(MainActivity3.this, "entecode", object.optString("entecode"));
                            Toast.makeText(MainActivity3.this,object.optString("entecode"),Toast.LENGTH_SHORT).show();
                            GetUserInfo.putData(MainActivity3.this, "trade", object.optString("trade"));
                            GetUserInfo.putData(MainActivity3.this, "address", object.optString("address"));
                            GetUserInfo.putData(MainActivity3.this, "logourl", object.optString("logourl"));
                            GetUserInfo.putData(MainActivity3.this, "legalperson", object.optString("legalperson"));
                            GetUserInfo.putData(MainActivity3.this, "contectnum", object.optString("contectnum"));
                            GetUserInfo.putData(MainActivity3.this, "createby", object.optString("createby"));
                            GetUserInfo.putData(MainActivity3.this, "createtime", object.optString("createtime"));
                            GetUserInfo.putData(MainActivity3.this, "registercode", object.optString("registercode"));
                            GetUserInfo.putData(MainActivity3.this, "scopmuch", object.optString("scopmuch"));
                            GetUserInfo.putData(MainActivity3.this, "createTimeStr", object.optString("createTimeStr"));
                            GetUserInfo.putData(MainActivity3.this, "provinceName", object.optString("provinceName"));
                            GetUserInfo.putData(MainActivity3.this, "cityName", object.optString("cityName"));
                            GetUserInfo.putData(MainActivity3.this, "countyName", object.optString("countyName"));
                            GetUserInfo.putData(MainActivity3.this, "province", object.optString("province"));
                            GetUserInfo.putData(MainActivity3.this, "city", object.optString("city"));
                            GetUserInfo.putData(MainActivity3.this, "county", object.optString("county"));
                            GetUserInfo.putData(MainActivity3.this, "isAdmin", object.getString("isAdmin"));
                            //GetUserInfo.putData(MainActivity3.this, "isAdmin", object.getString("isAdmin"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //极光推送设置别名
    public void jpushAlias() {
        Log.e("hhh", JPushInterface.getRegistrationID(this));
        JPushInterface.setAliasAndTags(this, JPushInterface.getRegistrationID(this) + GetUserInfo.getData(this, "mobileNum", ""), null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                switch (i) {
                    case 0:
                        String logs = "Set tag and alias success";
                        Log.e("lzq", logs + "=regid=" + JPushInterface.getRegistrationID(MainActivity3.this) + "=userid=" + GetUserInfo.getData(MainActivity3.this, "userId", ""));
                        okHttpAlias(JPushInterface.getRegistrationID(MainActivity3.this) + (String) GetUserInfo.getData(MainActivity3.this, "mobileNum", ""));
                        break;
                    case 6002:
                        logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                        Log.i("songlonglong", logs);
                        break;
                    default:
                        logs = "Failed with errorCode = " + i;
                        Log.e("songlonglong", logs);
                }

            }
        });
    }

    //极光推送设置别名接口
    public void okHttpAlias(String alias) {
        OkHttpUtils.post()
                .url(Constants.baseUrl + "userapp/updatePhoneType")
                .addParams("mobile", (String) GetUserInfo.getData(this, "mobileNum", ""))
                .addParams("telType", "0")
                .addParams("phoneType", alias)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong", "极光设置成功");
                    }
                });
    }
}

package com.qysd.lawtree;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;
import com.qysd.avchatkit.AVChatKit;
import com.qysd.avchatkit.config.AVChatOptions;
import com.qysd.avchatkit.model.ITeamDataProvider;
import com.qysd.avchatkit.model.IUserInfoProvider;
import com.qysd.lawtree.chatroom.ChatRoomSessionHelper;
import com.qysd.lawtree.common.util.LogHelper;
import com.qysd.lawtree.common.util.crash.AppCrashHandler;
import com.qysd.lawtree.config.preference.Preferences;
import com.qysd.lawtree.config.preference.UserPreferences;
import com.qysd.lawtree.contact.ContactHelper;
import com.qysd.lawtree.event.DemoOnlineStateContentProvider;
import com.qysd.lawtree.lawtreebean.LocationBean;
import com.qysd.lawtree.lawtreebean.PickerCityBean;
import com.qysd.lawtree.main.activity.MainActivity;
import com.qysd.lawtree.main.activity.WelcomeActivity;
import com.qysd.lawtree.mixpush.DemoMixPushMessageHandler;
import com.qysd.lawtree.mixpush.DemoPushContentProvider;
import com.qysd.lawtree.redpacket.NIMRedPacketClient;
import com.qysd.lawtree.session.NimDemoLocationProvider;
import com.qysd.lawtree.session.SessionHelper;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.api.UIKitOptions;
import com.qysd.uikit.business.contact.core.query.PinYin;
import com.qysd.uikit.business.team.helper.TeamHelper;
import com.qysd.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.qysd.uikit.common.util.log.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import io.fabric.sdk.android.Fabric;

public class NimApplication extends Application {

    //省市区集合
    public static ArrayList<PickerCityBean> options1Items = new ArrayList<>();
    public static ArrayList<ArrayList<PickerCityBean>> options2Items = new ArrayList<>();
    public static ArrayList<ArrayList<ArrayList<PickerCityBean>>> options3Items = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DemoCache.setContext(this);
        //极光推送
        JPushInterface.setDebugMode(com.qysd.lawtree.lawtreeutils.LogUtil.debug);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        // 4.6.0 开始，第三方推送配置入口改为 SDKOption#mixPushConfig，旧版配置方式依旧支持。
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));

        // crash handler
        AppCrashHandler.getInstance(this);

        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {

            // 注册自定义推送消息处理，这个是可选项
            NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());

            // 初始化红包模块，在初始化UIKit模块之前执行
            NIMRedPacketClient.init(this);
            // init pinyin
            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);
            // 初始化音视频模块
            initAVChatKit();
        }

        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);

        //地区对话框
        initJsonData("provice_city.json");//读取本地省市区json数据
        initDatas2();
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // IM 会话窗口的定制初始化。
        SessionHelper.init();

        // 聊天室聊天窗口的定制初始化。
        ChatRoomSessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }

    private void initAVChatKit() {
        AVChatOptions avChatOptions = new AVChatOptions() {
            @Override
            public void logout(Context context) {
                MainActivity.logout(context, true);
            }
        };
        avChatOptions.entranceActivity = WelcomeActivity.class;
        avChatOptions.notificationIconRes = R.drawable.ic_stat_notify_msg;
        AVChatKit.init(avChatOptions);

        // 初始化日志系统
        LogHelper.init();
        // 设置用户相关资料提供者
        AVChatKit.setUserInfoProvider(new IUserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return NimUIKit.getUserInfoProvider().getUserInfo(account);
            }

            @Override
            public String getUserDisplayName(String account) {
                return UserInfoHelper.getUserDisplayName(account);
            }
        });
        // 设置群组数据提供者
        AVChatKit.setTeamDataProvider(new ITeamDataProvider() {
            @Override
            public String getDisplayNameWithoutMe(String teamId, String account) {
                return TeamHelper.getDisplayNameWithoutMe(teamId, account);
            }

            @Override
            public String getTeamMemberDisplayName(String teamId, String account) {
                return TeamHelper.getTeamMemberDisplayName(teamId, account);
            }
        });
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private Gson gson = new Gson();
    private LocationBean locationBean;

    private void initJsonData(String name) {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open(name);
            int len = -1;
            byte[] buf = new byte[1024 * 100];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "UTF-8"));
            }
            is.close();
            locationBean = gson.fromJson(sb.toString(), LocationBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas2() {
        //DialogMaker.showProgressDialog(this, "");
        for (int i = 0; i < locationBean.getRegionInfo().size(); i++) {
            LocationBean.RegionInfo regionInfo = locationBean.getRegionInfo().get(i);
            if ("0".equals(regionInfo.getPid())) {
                PickerCityBean bean = new PickerCityBean();
                bean.setId(regionInfo.getId());
                bean.setName(regionInfo.getName());
                bean.setPid(regionInfo.getPid());
                options1Items.add(bean);
            }
        }
        //市的数据
        for (int i = 0; i < options1Items.size(); i++) {
            ArrayList<PickerCityBean> options2Items_city = new ArrayList<>();
            for (int j = 0; j < locationBean.getRegionInfo().size(); j++) {
                LocationBean.RegionInfo regionInfo = locationBean.getRegionInfo().get(j);
                if (options1Items.get(i).getId().equals(regionInfo.getPid())) {
                    PickerCityBean bean = new PickerCityBean();
                    bean.setId(regionInfo.getId());
                    bean.setName(regionInfo.getName());
                    bean.setPid(regionInfo.getPid());
                    options2Items_city.add(bean);
                }
            }
            options2Items.add(options2Items_city);
            //区的数据
            ArrayList<ArrayList<PickerCityBean>> options3Items_dis = new ArrayList<>();
            for (int j = 0; j < options2Items_city.size(); j++) {
                ArrayList<PickerCityBean> options3Items_dist = new ArrayList<>();
                for (int k = 0; k < locationBean.getRegionInfo().size(); k++) {
                    LocationBean.RegionInfo regionInfo = locationBean.getRegionInfo().get(k);
                    if (options2Items_city.get(j).getId().equals(regionInfo.getPid())) {
                        PickerCityBean bean = new PickerCityBean();
                        bean.setId(regionInfo.getId());
                        bean.setName(regionInfo.getName());
                        bean.setPid(regionInfo.getPid());
                        options3Items_dist.add(bean);
                    }
                }
                options3Items_dis.add(options3Items_dist);
            }
            options3Items.add(options3Items_dis);
        }
        //DialogMaker.dismissProgressDialog();
    }
}

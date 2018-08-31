package com.qysd.uikit.business.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.uikit.R;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.api.model.contact.ContactChangedObserver;
import com.qysd.uikit.api.model.main.OnlineStateChangeObserver;
import com.qysd.uikit.api.model.session.SessionCustomization;
import com.qysd.uikit.api.model.user.UserInfoObserver;
import com.qysd.uikit.api.wrapper.NimToolBarOptions;
import com.qysd.uikit.business.session.constant.Extras;
import com.qysd.uikit.business.session.fragment.MessageFragment;
import com.qysd.uikit.business.team.activity.lawtree.Constants;
import com.qysd.uikit.business.team.activity.lawtree.NullStringToEmptyAdapterFactory;
import com.qysd.uikit.business.team.activity.lawtree.UserCallback;
import com.qysd.uikit.business.uinfo.UserInfoHelper;
import com.qysd.uikit.common.activity.ToolBarOptions;
import com.qysd.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Set;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity {

    private boolean isResume = false;
    private TextView tv_p2p_info, tv_p2p_comp;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_p2p_info = findViewById(R.id.tv_p2p_info);
        tv_p2p_comp = findViewById(R.id.tv_p2p_comp);
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
        registerOnlineStateChangeListener(true);

        loadIdDetailData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerOnlineStateChangeListener(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    private void requestBuddyInfo() {
        setTitle("");
        tv_p2p_info.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObserver uinfoObserver;

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        setSubTitle(detailContent);
    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);
    }
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private LianXiRenDetailBean lianXiRenDetailBean;

    private void loadIdDetailData() {
        Log.e("NimUIKit.getAccount()",NimUIKit.getAccount());
        Log.e("sessionId",sessionId);
        OkHttpUtils.get().url(Constants.baseUrl + "friend/selectFriendDetailById")
                .addParams("userId", NimUIKit.getAccount())
                .addParams("friendId", sessionId)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("info", response.toString());
                        if (response.contains("非平台用户")) {
                            return;
                        }
                        lianXiRenDetailBean = gson.fromJson(response.toString(), LianXiRenDetailBean.class);
                        tv_p2p_comp.setText(lianXiRenDetailBean.getStatus().getCompName());
                    }
                });
    }

    public class LianXiRenDetailBean {
        //    {
//        "code": "1",
//            "status": {
//        "birthDay": {
//            "date": 9,
//                    "hours": 0,
//                    "seconds": 0,
//                    "month": 3,
//                    "timezoneOffset": -480,
//                    "year": 118,
//                    "minutes": 0,
//                    "time": 1523203200000,
//                    "day": 1
//        },
//        "idCard": "34042119920806121X",
//                "sex": 1,
//                "deptId": 0,
//                "headUrl": "http://192.168.1.152/images/4cf50bff8b2a4e928a6b1fa3d547af30.png",
//                "sign": "哈哈哈哈还哈",
//                "userName": "刘明明",
//                "compName": "律树",
//                "userId": "dfad7dc2bc0145dc90c2bde32f219c56",
//                "mobileNum": "13141103344",
//                "reqStatus": 2, 0未同意 1好友 2删除 3未发送邀请
//                "compId": "2",
//                "birthDayStr": "2018年04月09日",
//                "position": "高级iOS开发工程师"
//    }
//    }
        private String code;
        private Status status;

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public class Status {
            private String birthDayStr;

            private String compId;
            private String compName;
            private String deptId;

            private String headUrl;
            private String idCard;
            private String mobileNum;
            private String position;
            private String reqStatus;
            private String sex;
            private String sign;
            private String userId;
            private String userName;
            //private BirthDay birthDay;

            public String getCompId() {
                return compId;
            }

            public void setCompId(String compId) {
                this.compId = compId;
            }

            public String getCompName() {
                return compName;
            }

            public void setCompName(String compName) {
                this.compName = compName;
            }

            public String getDeptId() {
                return deptId;
            }

            public void setDeptId(String deptId) {
                this.deptId = deptId;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public String getIdCard() {
                return idCard;
            }

            public void setIdCard(String idCard) {
                this.idCard = idCard;
            }

            public String getMobileNum() {
                return mobileNum;
            }

            public void setMobileNum(String mobileNum) {
                this.mobileNum = mobileNum;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getReqStatus() {
                return reqStatus;
            }

            public void setReqStatus(String reqStatus) {
                this.reqStatus = reqStatus;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

//            public BirthDay getBirthDay() {
//                return birthDay;
//            }
//
//            public void setBirthDay(BirthDay birthDay) {
//                this.birthDay = birthDay;
//            }

            public String getBirthDayStr() {
                return birthDayStr;
            }

            public void setBirthDayStr(String birthDayStr) {
                this.birthDayStr = birthDayStr;
            }

            public class BirthDay {
                private String date;
                private String day;
                private String hours;
                private String minutes;
                private String month;
                private String seconds;
                private String time;
                private String timezoneOffset;
                private String year;

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public String getHours() {
                    return hours;
                }

                public void setHours(String hours) {
                    this.hours = hours;
                }

                public String getMinutes() {
                    return minutes;
                }

                public void setMinutes(String minutes) {
                    this.minutes = minutes;
                }

                public String getMonth() {
                    return month;
                }

                public void setMonth(String month) {
                    this.month = month;
                }

                public String getSeconds() {
                    return seconds;
                }

                public void setSeconds(String seconds) {
                    this.seconds = seconds;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getTimezoneOffset() {
                    return timezoneOffset;
                }

                public void setTimezoneOffset(String timezoneOffset) {
                    this.timezoneOffset = timezoneOffset;
                }

                public String getYear() {
                    return year;
                }

                public void setYear(String year) {
                    this.year = year;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }
            }
        }
    }
}

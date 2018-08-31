package com.qysd.lawtree.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.qysd.lawtree.DemoCache;
import com.qysd.lawtree.R;
import com.qysd.lawtree.contact.activity.UserProfileActivity;
import com.qysd.lawtree.lawtreeactivity.LianXiRenDetailActivity;
import com.qysd.lawtree.lawtreeactivity.SendQunLiaoActivity;
import com.qysd.lawtree.lawtreebean.LianXiRenDetailBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.qysd.lawtree.session.search.SearchMessageActivity;
import com.qysd.lawtree.team.TeamCreateHelper;
import com.qysd.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.qysd.uikit.business.team.helper.TeamHelper;
import com.qysd.uikit.business.uinfo.UserInfoHelper;
import com.qysd.uikit.common.activity.ToolBarOptions;
import com.qysd.uikit.common.activity.UI;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.qysd.uikit.common.ui.widget.SwitchButton;
import com.qysd.uikit.common.util.sys.NetworkUtil;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;

/**
 * Created by hzxuwen on 2015/10/13.
 */
public class MessageInfoActivity extends UI {
    private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
    private static final int REQUEST_CODE_NORMAL = 1;
    // data
    private String account;
    // view
    private SwitchButton switchButton;

    private RelativeLayout rl_create_qunliao;//创建群聊
    private RelativeLayout rl_look_message;//查看
    private RelativeLayout rl_person_xiaoxi_state;//消息免打扰状态
    private TextView tv_name, tv_department;//姓名 公司
    private LianXiRenDetailBean lianXiRenDetailBean;
    private boolean state = true;
    private ImageView iv_person_xiaoxi_state;//信息开始的状态
    private HeadImageView iv_person;
    private RelativeLayout rl_message_info;//个人信息

    public static void startActivity(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, MessageInfoActivity.class);
        intent.putExtra(EXTRA_ACCOUNT, account);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_info_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.message_info;
        options.navigateId = R.drawable.ic_left_jt;
        setToolBar(R.id.toolbar, options);

        account = getIntent().getStringExtra(EXTRA_ACCOUNT);
        initTitle(R.drawable.ic_left_jt, null, "聊天设置", null, 0);
        findViews();
        loadIdDetailData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSwitchBtn();
    }

    private void findViews() {
        rl_message_info = findViewById(R.id.rl_message_info);
        iv_person = findViewById(R.id.iv_person);
        iv_person_xiaoxi_state = findViewById(R.id.iv_person_xiaoxi_state);
        tv_name = findViewById(R.id.tv_name);
        tv_department = findViewById(R.id.tv_department);
        rl_create_qunliao = findViewById(R.id.rl_create_qunliao);
        rl_look_message = findViewById(R.id.rl_look_message);
        rl_person_xiaoxi_state = findViewById(R.id.rl_person_xiaoxi_state);
        rl_message_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageInfoActivity.this, LianXiRenDetailActivity.class);
                intent.putExtra("type", "typeId");
                intent.putExtra("account", account);
                startActivity(intent);
            }
        });
        rl_create_qunliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageInfoActivity.this, SendQunLiaoActivity.class));
            }
        });
        rl_look_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MessageHistoryActivity.start(MessageInfoActivity.this, account,
//                        SessionTypeEnum.P2P);
                SearchMessageActivity.start(MessageInfoActivity.this,
                        account, SessionTypeEnum.P2P);
            }
        });
        iv_person.loadBuddyAvatar(account);
        if (NIMClient.getService(FriendService.class).isNeedMessageNotify(account)) {
            iv_person_xiaoxi_state.setImageResource(R.drawable.ic_open);
            state = true;
        } else {
            iv_person_xiaoxi_state.setImageResource(R.drawable.ic_close);
            state = false;
        }
        rl_person_xiaoxi_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = !state;
                NIMClient.getService(FriendService.class).setMessageNotify(account, state).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (state) {
                            iv_person_xiaoxi_state.setImageResource(R.drawable.ic_open);
                            Toast.makeText(MessageInfoActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                        } else {
                            iv_person_xiaoxi_state.setImageResource(R.drawable.ic_close);
                            Toast.makeText(MessageInfoActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MessageInfoActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        });

        HeadImageView userHead = (HeadImageView) findViewById(R.id.user_layout).findViewById(R.id.imageViewHeader);
        TextView userName = (TextView) findViewById(R.id.user_layout).findViewById(R.id.textViewName);
        userHead.loadBuddyAvatar(account);
        userName.setText(UserInfoHelper.getUserDisplayName(account));
        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });

        ((TextView) findViewById(R.id.create_team_layout).findViewById(R.id.textViewName)).setText(R.string.create_normal_team);
        HeadImageView addImage = (HeadImageView) findViewById(R.id.create_team_layout).findViewById(R.id.imageViewHeader);
        addImage.setBackgroundResource(com.qysd.uikit.R.drawable.nim_team_member_add_selector);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeamMsg();
            }
        });

        ((TextView) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_title)).setText(R.string.msg_notice);
        switchButton = (SwitchButton) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_toggle);
        switchButton.setOnChangedListener(onChangedListener);
    }

    private void updateSwitchBtn() {
        boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
        switchButton.setCheck(notice);
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            if (!NetworkUtil.isNetAvailable(MessageInfoActivity.this)) {
                Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                switchButton.setCheck(!checkState);
                return;
            }

            NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    if (checkState) {
                        Toast.makeText(MessageInfoActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageInfoActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(int code) {
                    if (code == 408) {
                        Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageInfoActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                    }
                    switchButton.setCheck(!checkState);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    };

    private void openUserProfile() {
        //UserProfileActivity.start(this, account);
        Intent intent = new Intent(this, LianXiRenDetailActivity.class);
        intent.putExtra("type", "typeId");
        intent.putExtra("account", account);
        startActivity(intent);
    }

    /**
     * 创建群聊
     */
    private void createTeamMsg() {
        ArrayList<String> memberAccounts = new ArrayList<>();
        memberAccounts.add(account);
        ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(memberAccounts, 50);
        NimUIKit.startContactSelector(this, option, REQUEST_CODE_NORMAL);// 创建群
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    TeamCreateHelper.createNormalTeam(MessageInfoActivity.this, selected, true, new RequestCallback<CreateTeamResult>() {
                        @Override
                        public void onSuccess(CreateTeamResult param) {
                            finish();
                        }

                        @Override
                        public void onFailed(int code) {

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    Toast.makeText(DemoCache.getContext(), "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 通过id查
     */
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();

    private void loadIdDetailData() {
        OkHttpUtils.get().url(Constants.baseUrl + "friend/selectFriendDetailById")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("friendId", account)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lzq", response.toString());
                        lianXiRenDetailBean = gson.fromJson(response.toString(), LianXiRenDetailBean.class);
                        tv_name.setText(lianXiRenDetailBean.getStatus().getUserName());
                        tv_department.setText(lianXiRenDetailBean.getStatus().getCompName());
                    }
                });
    }

    protected final void initTitle(int resLeft, String strLeft, String strContent, String strRight, int resRight) {
        ImageView ivTitleLeft = (ImageView) findViewById(R.id.iv_title_left);
        ImageView ivTitleRight = (ImageView) findViewById(R.id.iv_title_right);
        TextView tvTitleLeft = (TextView) findViewById(R.id.tv_title_left);
        TextView tvTitleContent = (TextView) findViewById(R.id.tv_title_content);
        TextView tvTitleRight = (TextView) findViewById(R.id.tv_title_right);

        if (ivTitleLeft != null) {
            if (resLeft == 0) {
                ivTitleLeft.setVisibility(View.GONE);
            } else {
                ivTitleLeft.setVisibility(View.VISIBLE);
                ivTitleLeft.setImageResource(resLeft);
                ivTitleLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

        if (ivTitleRight != null) {
            if (resRight == 0) {
                ivTitleRight.setVisibility(View.GONE);
            } else {
                ivTitleRight.setVisibility(View.VISIBLE);
                ivTitleRight.setImageResource(resRight);
                ivTitleRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }

        if (tvTitleLeft != null) {
            if (strLeft == null) {
                tvTitleLeft.setVisibility(View.GONE);
            } else {
                tvTitleLeft.setVisibility(View.VISIBLE);
                tvTitleLeft.setText(strLeft);
                tvTitleLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }
        if (tvTitleContent != null) {
            if (strContent == null) {
                tvTitleContent.setVisibility(View.GONE);
            } else {
                tvTitleContent.setVisibility(View.VISIBLE);
                tvTitleContent.setText(strContent);
            }
        }
        if (tvTitleRight != null) {
            if (strRight == null) {
                tvTitleRight.setVisibility(View.GONE);
            } else {
                tvTitleRight.setVisibility(View.VISIBLE);
                tvTitleRight.setText(strRight);
                tvTitleRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }
    }
}

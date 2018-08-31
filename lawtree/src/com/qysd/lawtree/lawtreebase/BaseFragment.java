package com.qysd.lawtree.lawtreebase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.qysd.lawtree.R;
import com.qysd.lawtree.main.reminder.ReminderItem;
import com.qysd.lawtree.main.reminder.ReminderManager;

import org.greenrobot.eventbus.EventBus;


/**
 * 基础的碎片类
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseFragment extends Fragment implements ReminderManager.UnreadNumChangedCallback {
    /*子类必须要实现的方法*/
    protected abstract int getLayoutId();//绑定视图

    protected abstract void initView();//初始化视图

    protected abstract void bindListener();//绑定点击事件

    protected abstract void initData();//初始化数据

    protected abstract void initNav();//初始化导航栏

    private View fragmentview;
    protected String TAG;
    private Toast cdToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentview = inflater.inflate(getLayoutId(), container, false);
        return fragmentview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TAG = getActivity().getClass().getSimpleName();
        initView();
        bindListener();
        initData();
        initNav();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMsgUnreadInfoObserver(true);
        //EventBus.getDefault().post(new ReadCountEvent("", NIMClient.getService(MsgService.class).getTotalUnreadCount()));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 隐藏输入法
     */
    protected void hideSoftInput() {
        InputMethodManager imeManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imeManager.isActive();
        imeManager.hideSoftInputFromWindow(getActivity().getWindow()
                        .getDecorView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示输入法
     */
    protected void showSoftInput() {
        InputMethodManager imeManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imeManager.isActive();
        imeManager.hideSoftInputFromWindow(getActivity().getWindow()
                        .getDecorView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        imeManager.showSoftInputFromInputMethod(getActivity().getWindow()
                        .getDecorView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 发送提示消息
     *
     * @param message
     */
    protected void showToast(String message) {
        if (cdToast == null) {
            cdToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        } else {
            cdToast.setText(message);
            cdToast.setDuration(Toast.LENGTH_SHORT);
        }
        cdToast.show();
    }

    /**
     * 加载视图组件
     *
     * @param id
     * @return
     */
    protected View findViewById(int id) {
        return fragmentview.findViewById(id);
    }

    /**
     * 打印日志
     *
     * @param log
     */
    protected void log(String log) {
        Log.d(TAG, log);
    }

    //在fragment中添加自定义标题
    protected final void initTitle(String strContent) {
        this.initTitle(0, null, strContent, null, 0);
    }

    protected final void initTitle(int resLeft, String strContent) {
        this.initTitle(resLeft, null, strContent, null, 0);
    }

    protected final void initTitle(int resLeft, String strContent, String strRight) {
        this.initTitle(resLeft, null, strContent, strRight, 0);
    }

    protected final void initTitle(int resLeft, String strContent, int resRight) {
        this.initTitle(resLeft, null, strContent, null, resRight);
    }

    protected final void initTitle(String strLeft, String strContent, String strRight) {
        this.initTitle(0, strLeft, strContent, strRight, 0);
    }

    protected final void initTitle(int resLeft, String strLeft, String strContent, String strRight, int resRight) {
        View view = getView();
        if (view == null) {
            return;
        }
        ImageView ivTitleLeft = (ImageView) view.findViewById(R.id.iv_title_left);
        ImageView ivTitleRight = (ImageView) view.findViewById(R.id.iv_title_right);
        TextView tvTitleLeft = (TextView) view.findViewById(R.id.tv_title_left);
        TextView tvTitleContent = (TextView) view.findViewById(R.id.tv_title_content);
        TextView tvTitleRight = (TextView) view.findViewById(R.id.tv_title_right);

        if (resLeft == 0) {
            ivTitleLeft.setVisibility(View.GONE);
        } else {
            ivTitleLeft.setVisibility(View.VISIBLE);
            ivTitleLeft.setImageResource(resLeft);
            ivTitleLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTitleLeft(v);
                }
            });
        }

        if (resRight == 0) {
            ivTitleRight.setVisibility(View.GONE);
        } else {
            ivTitleRight.setVisibility(View.VISIBLE);
            ivTitleRight.setImageResource(resRight);
            ivTitleRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTitleRight(v);
                }
            });
        }
        if (strLeft == null) {
            tvTitleLeft.setVisibility(View.GONE);
        } else {
            tvTitleLeft.setVisibility(View.VISIBLE);
            tvTitleLeft.setText(strLeft);
            tvTitleLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTitleLeft(v);
                }
            });
        }
        if (strContent == null) {
            tvTitleContent.setVisibility(View.GONE);
        } else {
            tvTitleContent.setVisibility(View.VISIBLE);
            tvTitleContent.setText(strContent);
        }
        if (strRight == null) {
            tvTitleRight.setVisibility(View.GONE);
        } else {
            tvTitleRight.setVisibility(View.VISIBLE);
            tvTitleRight.setText(strRight);
            tvTitleRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTitleRight(v);
                }
            });
        }
    }

    protected void onClickTitleLeft(View v) {
    }

    protected void onClickTitleRight(View v) {

    }

    //fragment 是否可见
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见的时候进行数据的刷新
     */
    protected void onVisible() {
        loadData();
    }

    protected void onInvisible() {

    }

    protected abstract void loadData();

    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        Log.e("base fragment", item.getUnread() + "");
        Log.e("base fragment all", NIMClient.getService(MsgService.class).getTotalUnreadCount() + "");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerMsgUnreadInfoObserver(true);
        //EventBus.getDefault().post(new ReadCountEvent("", NIMClient.getService(MsgService.class).getTotalUnreadCount()));
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
}

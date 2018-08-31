package com.qysd.uikit.business.team.activity.lawtree;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.qysd.uikit.R;


/**
 * 基础的activity视图
 * Created by longshao on 2016/5/13.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener{

    /*子类必须要实现的方法*/
    protected abstract void bindView();//绑定视图

    protected abstract void initView();//初始化视图

    protected abstract void bindListener();//绑定点击事件

    protected abstract void initData();//初始化数据

    protected abstract void initNav();//初始化导航栏

    protected String TAG;

    private Toast cdToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.gc();
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置隐藏默认标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置输入框模式
        getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        bindView();
        initView();
        bindListener();
        initData();
        initNav();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Activity被系统杀死后再重建时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancleToast();
    }

    /**
     * 发送提示消息
     *
     * @param message
     */
    protected void showToast(String message) {
        if (cdToast == null) {
            cdToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            cdToast.setText(message);
            cdToast.setDuration(Toast.LENGTH_SHORT);
        }
        cdToast.show();
    }

    /**
     * 取消Toast提示
     */
    protected void cancleToast() {
        if (cdToast != null) {
            cdToast.cancel();
        }
    }

    /**
     * 当activity触屏事件时，隐藏输入法
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSoftInput();
        return super.onTouchEvent(event);
    }

    /**
     * 隐藏输入法
     */
    protected void hideSoftInput() {
        InputMethodManager imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imeManager.isActive();
        imeManager.hideSoftInputFromWindow(getWindow().getDecorView()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示输入法
     */
    protected void showSoftInput() {
        InputMethodManager imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imeManager.isActive();
        imeManager.hideSoftInputFromWindow(getWindow().getDecorView()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        imeManager.showSoftInputFromInputMethod(getWindow().getDecorView()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 打印日志
     *
     * @param log
     */
    protected void log(String log) {
        Log.e(TAG, log);
    }

    //activity中添加标题控件组
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
                        onClickTitleLeft(v);
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
                        onClickTitleRight(v);
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
                        onClickTitleLeft(v);
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
                        onClickTitleRight(v);
                    }
                });
            }
        }
    }

    protected void onClickTitleLeft(View v) {
        finish();
    }

    protected void onClickTitleRight(View v) {

    }
}

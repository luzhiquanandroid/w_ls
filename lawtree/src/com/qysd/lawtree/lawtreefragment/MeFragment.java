package com.qysd.lawtree.lawtreefragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.qiangxi.checkupdatelibrary.bean.CheckUpdateInfo;
import com.qiangxi.checkupdatelibrary.dialog.ForceUpdateDialog;
import com.qiangxi.checkupdatelibrary.dialog.UpdateDialog;
import com.qysd.lawtree.DemoCache;
import com.qysd.lawtree.R;
import com.qysd.lawtree.appupdate.DeviceUtils;
import com.qysd.lawtree.config.preference.Preferences;
import com.qysd.lawtree.contact.activity.UserProfileSettingActivity;
import com.qysd.lawtree.lawtreeactivity.AccountAndSecurityActivity;
import com.qysd.lawtree.lawtreeactivity.MyBusinessActivity;
import com.qysd.lawtree.lawtreeactivity.MyZxingPic;
import com.qysd.lawtree.lawtreeactivity.UpdatePasswordActivity;
import com.qysd.lawtree.lawtreebase.BaseFragment;
import com.qysd.lawtree.lawtreebusbean.HeadUrlEventBusBean;
import com.qysd.lawtree.lawtreebusbean.RefreshEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constant;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.GlideUtils;
import com.qysd.lawtree.login.LoginActivity;
import com.qysd.lawtree.main.activity.SettingsActivity;
import com.qysd.lawtree.main.activity.WelcomeActivity;
import com.qysd.lawtree.main.model.Extras;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import static com.qiangxi.checkupdatelibrary.dialog.ForceUpdateDialog.FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE;
import static com.qiangxi.checkupdatelibrary.dialog.UpdateDialog.UPDATE_DIALOG_PERMISSION_REQUEST_CODE;


/**
 * Created by zhouwei on 17/4/23.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener{

    private RelativeLayout rl_personinfo,rl_myzxing,rl_mybusiness,rl_message,rl_account_and_security,rl_update;
    private String mFrom;
    private TextView tv_name,tv_position,tv_companyname,tv_exit;
    private ImageView iv_header;
    //版本更新
    private CheckUpdateInfo mCheckUpdateInfo;
    private UpdateDialog mUpdateDialog;
    private ForceUpdateDialog mForceUpdateDialog;

    public static MeFragment newInstance(String from){
        MeFragment fragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mFrom = getArguments().getString("from");
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.me_fragment;
    }

    @Override
    protected void initView() {
        initTitle("我的");
        rl_personinfo = (RelativeLayout) findViewById(R.id.rl_personinfo);
        rl_myzxing = (RelativeLayout) findViewById(R.id.rl_myzxing);
        rl_mybusiness = (RelativeLayout) findViewById(R.id.rl_mybusiness);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        rl_account_and_security = (RelativeLayout) findViewById(R.id.rl_account_and_security);
        rl_update = (RelativeLayout) findViewById(R.id.rl_update);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_companyname = (TextView) findViewById(R.id.tv_companyname);
        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        iv_header = (ImageView) findViewById(R.id.iv_header);
//        TextView textView = (TextView) findViewById(R.id.title_from);
//        TextView content = (TextView) findViewById(R.id.fragment_content);
//        textView.setText(mFrom);
//        content.setText("MeFragment");
    }

    @Override
    protected void bindListener() {
        rl_personinfo.setOnClickListener(this);
        rl_myzxing.setOnClickListener(this);
        rl_mybusiness.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_account_and_security.setOnClickListener(this);
        rl_update.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_name.setText((String)GetUserInfo.getData(getActivity(),"userName",""));
        if ("".equals(GetUserInfo.getData(getActivity(),"entecode",""))){
            tv_companyname.setVisibility(View.GONE);
        }else {
            tv_companyname.setVisibility(View.VISIBLE);
            tv_companyname.setText((String)GetUserInfo.getData(getActivity(),"compName",""));
        }
        if ("".equals(GetUserInfo.getData(getActivity(),"position",""))||"null".equals(GetUserInfo.getData(getActivity(),"position",""))){
            tv_position.setText("待定");
        }else {
            tv_position.setText((String)GetUserInfo.getData(getActivity(),"position",""));
        }
        Log.e("sssssssssss",GetUserInfo.getHeaderUrl(getActivity()));
        GlideUtils.loadCircleImage(getActivity(),(String) GetUserInfo.getHeaderUrl(getActivity()),iv_header);
    }

    @Override
    protected void initNav() {

    }

    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.activity_account_and_security.me_fragment,null);
//        TextView textView = (TextView) view.findViewById(R.id.title_from);
//        TextView content = (TextView) view.findViewById(R.id.fragment_content);
//        textView.setText(mFrom);
//        content.setText("MeFragment");
//        return view;
//    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_personinfo:
                UserProfileSettingActivity.start(getActivity(), DemoCache.getAccount());
                break;
            case R.id.rl_myzxing:
                startActivity(new Intent(getActivity(), MyZxingPic.class));
                break;
            case R.id.rl_mybusiness:
                if (!"".equals(GetUserInfo.getData(getActivity(),"entecode",""))){
                    startActivity(new Intent(getActivity(), MyBusinessActivity.class));
                }else {
                    Toast.makeText(getActivity(),"您暂无企业，请前往律树网页端进行创建",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_message:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.rl_account_and_security:
                startActivity(new Intent(getActivity(), AccountAndSecurityActivity.class));
                break;
            case R.id.rl_update:
                versionUpdate();
                break;
            case R.id.tv_exit:
                logout();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRefreshLxr(HeadUrlEventBusBean bean) {
        Log.e("eventbus",bean.getName());
        if (!"".equals(bean.getName())) {
            tv_name.setText((String)GetUserInfo.getData(getActivity(),"userName",""));
        }
        if (!"".equals(bean.getHeadUrl())) {
            GlideUtils.loadCircleImage(getActivity(),(String) GetUserInfo.getHeaderUrl(getActivity()),iv_header);
        }
    }

    /**
     * 查询是否需要更新
     */
    public void versionUpdate(){
        OkHttpUtils.get()
                .url(Constants.baseUrl+"version/selectVersion")
                .addParams("userId",GetUserInfo.getUserId(getActivity()))
                .addParams("oldVersionCode",DeviceUtils.getVersionCode(getActivity())+"")
                .addParams("telType","0")//手机类型（0，安卓1，ios）
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if ("1".equals(object.optString("code"))){
                                if (Integer.parseInt(object.optString("code")) > DeviceUtils.getVersionCode(getActivity())){

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 版本更新日志
     */
    public void updateApp() {
        //app版本更新
        OkHttpUtils.post()
                .url(Constants.baseUrl + "/version/updateVersion")
                .addParams("appType", "2")//设备类型
                .addParams("versionType", DeviceUtils.getVersionCode(getActivity()) + "")//版本号
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("response", response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            if ("1".equals(object.optString("code"))) {
                                if ("-1".equals(object.optString("isForceUpdate"))) {//版本号一样
                                    Toast.makeText(getActivity(), "当前已是最新版本",Toast.LENGTH_SHORT).show();
                                    return;
                                } else {//更新
                                    mCheckUpdateInfo = new CheckUpdateInfo();
                                    mCheckUpdateInfo.setAppName("律服通-用户版更新")
                                            .setIsForceUpdate(Integer.valueOf(object.optString("isForceUpdate")))//设置是否强制更新,0正常更新，1强制更新
                                            //.setNewAppReleaseTime("2016-10-14 12:37")//软件发布时间
                                            .setNewAppSize(30.1f)//单位为M
                                            .setNewAppUrl(object.optString("url"))
                                            .setNewAppVersionCode(Integer.valueOf(object.optString("version")))//新app的VersionCode
                                            .setNewAppVersionName(object.optString("versionName"))
                                            //.setNewAppUpdateDesc("1,优化下载逻辑\n2,修复一些bug\n3,完全实现强制更新与非强制更新逻辑\n4,非强制更新状态下进行下载,默认在后台进行下载\n5,当下载成功时,会在通知栏显示一个通知,点击该通知,进入安装应用界面\n6,当下载失败时,会在通知栏显示一个通知,点击该通知,会重新下载该应用\n7,当下载中,会在通知栏显示实时下载进度,但前提要dialog.setShowProgress(true).");
                                            .setNewAppUpdateDesc(object.optString("description"));
                                    if (mCheckUpdateInfo.getIsForceUpdate() == 0) {
                                        UpdateDialogClick();
                                    } else {
                                        //UpdateDialogClick();
                                        forceUpdateDialog();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 强制更新,checkupdatelibrary中提供的默认强制更新Dialog,您完全可以自定义自己的Dialog,
     */
    public void forceUpdateDialog() {
        mForceUpdateDialog = new ForceUpdateDialog(getActivity());
        mForceUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                .setFileName("律服通-用户版.apk")
                .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib").show();
    }

    /**
     * 非强制更新,checkupdatelibrary中提供的默认非强制更新Dialog,您完全可以自定义自己的Dialog
     */
    public void UpdateDialogClick() {
        mUpdateDialog = new UpdateDialog(getActivity());
        mUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                .setFileName("律服通-用户版.apk")
                .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib")
                //该方法需设为true,才会在通知栏显示下载进度,默认为false,即不显示
                //该方法只会控制下载进度的展示,当下载完成或下载失败时展示的通知不受该方法影响
                //即不管该方法是置为false还是true,当下载完成或下载失败时都会在通知栏展示一个通知
                .setShowProgress(true)
                .setIconResId(R.drawable.ic_logo)
                .setAppName(mCheckUpdateInfo.getAppName()).show();
    }

    /**
     * 版本更新6.0系统需要重写此方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果用户同意所请求的权限
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //UPDATE_DIALOG_PERMISSION_REQUEST_CODE和FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE这两个常量是library中定义好的
            //所以在进行判断时,必须要结合这两个常量进行判断.
            //非强制更新对话框
            if (requestCode == UPDATE_DIALOG_PERMISSION_REQUEST_CODE) {
                //进行下载操作
                mUpdateDialog.download();
            }
            //强制更新对话框
            else if (requestCode == FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE) {
                //进行下载操作
                mForceUpdateDialog.download();
            }
        } else {
            //用户不同意,提示用户,如下载失败,因为您拒绝了相关权限
            //Toast.makeText(this, "some description...", Toast.LENGTH_SHORT).show();
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Log.e("tag", "false.请开启读写sd卡权限,不然无法正常工作");
//            } else {
//                Log.e("tag", "true.请开启读写sd卡权限,不然无法正常工作");
//            }

        }
    }


    /**
     * 注销
     */
    private void logout() {
        //BottomTabLayoutActivity.logout(UpdatePasswordActivity.this, false);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
        //LoginActivity.start(getActivity());
        Preferences.saveUserAccount("");
        Preferences.saveUserToken("");
        DemoCache.clear();
        getActivity().finish();
        NIMClient.getService(AuthService.class).logout();
    }
}

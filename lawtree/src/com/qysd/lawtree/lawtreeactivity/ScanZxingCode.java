package com.qysd.lawtree.lawtreeactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.ChoosePicturePop;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.uikit.common.util.string.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanZxingCode extends BaseActivity implements QRCodeView.Delegate {

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private QRCodeView mQRCodeView;
    private ImageView iv_flashlight_off, iv_flashlight_on;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_scanzxingcode);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        iv_flashlight_off = findViewById(R.id.iv_flashlight_off);
        iv_flashlight_on = findViewById(R.id.iv_flashlight_on);
        iv_flashlight_off.setOnClickListener(this);
        iv_flashlight_on.setOnClickListener(this);
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
        //开始识别
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_flashlight_off:
                mQRCodeView.openFlashlight();
                iv_flashlight_off.setVisibility(View.GONE);
                iv_flashlight_on.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_flashlight_on:
                mQRCodeView.closeFlashlight();
                iv_flashlight_on.setVisibility(View.GONE);
                iv_flashlight_off.setVisibility(View.VISIBLE);
                break;
        }
    }
    private Bundle bundle = new Bundle();
    @Override
    public void onScanQRCodeSuccess(String result) {
        mQRCodeView.stopSpot();//关闭扫描
        Log.e(TAG, "result1:" + result+"---"+result.substring(0,20));
        Log.e(TAG, "result:" + result.substring(result.indexOf("/") + 1));
        if(result.toString().contains("com.lvshu.planDetail")){
            Intent intent = new Intent(ScanZxingCode.this, ScTaskManagerDetailActivity.class);
            bundle.putString("type","sm");
            bundle.putString("planId",result.substring(result.indexOf("/") + 1));
            intent.putExtra("task", bundle);
            startActivity(intent);
        } else {
            Intent intent = new Intent(ScanZxingCode.this, LianXiRenDetailActivity.class);
            intent.putExtra("type", "typeId");
            intent.putExtra("account", result.substring(result.indexOf("/") + 1));
            startActivity(intent);
        }
//        if (GetUserInfo.getUserId(this).equals(result.substring(result.indexOf("/") + 1))){
//            Toast.makeText(this,"不能扫描自己的二维码",Toast.LENGTH_SHORT).show();
//        }else {
//            addFriend(result.substring(result.indexOf("/") + 1));
//        }
        vibrate();
        mQRCodeView.startSpot();
        finish();
    }

//    public void addFriend(final String friendId){
//        OkHttpUtils.get()
//                .url(Constants.baseUrl+"friend/selectFriendDetailById")
//                .addParams("userId", GetUserInfo.getUserId(this))
//                .addParams("friendId",friendId)
//                .build()
//                .execute(new UserCallback() {
//                    @Override
//                    public void onResponse(String response, int id) {
////                        reqStatus == 1 强关系（好友） 不相等进行下一个判断
////                        compId 相等 弱关系（同事但不是好友） 不相等 无关系
//                        Log.e("songlonglong",response);
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            if ("1".equals(object.optString("code"))){
//                                Intent intent = new Intent(ScanZxingCode.this, LianXiRenDetailActivity.class);
//                                if ("1".equals(object.optString("reqStatus"))){
//                                    //强关系（好友）
//                                    intent.putExtra("type", "typeId");
//                                    intent.putExtra("account", friendId);
//                                    startActivity(intent);
//                                }else {
//                                    //相等 弱关系（同事但不是好友）
//                                    if (GetUserInfo.getData(ScanZxingCode.this,"compId","").equals(object.optString("compId"))){
//                                        intent.putExtra("type", "typeId");
//                                        intent.putExtra("account", friendId);
//                                        startActivity(intent);
//                                    }else {//无关系
//
//                                    }
//                                }
//
//                            }else {
//                                Toast.makeText(ScanZxingCode.this,"非平台用户",Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mQRCodeView.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
            /*
            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(ScanZxingCode.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScanZxingCode.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

}

package com.qysd.lawtree.contact.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.qysd.lawtree.R;
import com.qysd.lawtree.contact.constant.UserConstant;
import com.qysd.lawtree.contact.helper.UserUpdateHelper;
import com.qysd.lawtree.lawtreeactivity.BottomTabLayoutActivity;
import com.qysd.lawtree.lawtreebean.PersonalInfoBean;
import com.qysd.lawtree.lawtreebusbean.HeadUrlEventBusBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.BitmapUtils;
import com.qysd.lawtree.lawtreeutils.ChoosePicturePop;
import com.qysd.lawtree.lawtreeutils.Constant;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.HeadImageUtils;
import com.qysd.lawtree.lawtreeutils.PictureUtils;
import com.qysd.lawtree.main.model.Extras;
import com.qysd.uikit.business.session.actions.PickImageAction;
import com.qysd.uikit.common.activity.ToolBarOptions;
import com.qysd.uikit.common.activity.UI;
import com.qysd.uikit.common.media.picker.PickImageHelper;
import com.qysd.uikit.common.ui.dialog.DialogMaker;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.qysd.uikit.common.util.log.LogUtil;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.api.model.SimpleCallback;
import com.qysd.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.qysd.uikit.common.util.media.BitmapUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzxuwen on 2015/9/14.
 */
public class UserProfileSettingActivity extends UI implements View.OnClickListener {
    private final String TAG = UserProfileSettingActivity.class.getSimpleName();

    // constant
    private static final int PICK_AVATAR_REQUEST = 0x0E;
    private static final int AVATAR_TIME_OUT = 30000;

    private String account;

    // view
    private HeadImageView userHead;
    private RelativeLayout nickLayout;
    private RelativeLayout genderLayout;
    private RelativeLayout birthLayout;
    private RelativeLayout idcardLayout;
    private RelativeLayout firmLayout;
    private RelativeLayout phoneLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout signatureLayout;

    private ImageView iv_title_left;
    private TextView tv_title_right;
    private TextView nickText;
    private TextView genderText;
    private TextView birthText;
    private TextView idcardText;
    private TextView firmText;
    private TextView phoneText;
    private TextView emailText;
    private TextView signatureText;

    private Map<Integer, UserInfoFieldEnum> fieldMap;
    private ChoosePicturePop choosePicturePop;//选择图片工具头像
    private boolean isCamera = false;
    Bitmap bitmap, cameraBitmap;
    private Uri uri;
    private String path = "";
    private File file;
    // data
    AbortableFuture<String> uploadAvatarFuture;
    private NimUserInfo userInfo;

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileSettingActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_set_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.user_information;
        setToolBar(R.id.toolbar, options);
        EventBus.getDefault().register(this);
        choosePicturePop = new ChoosePicturePop(this);

        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        findViews();
        getUserInfo();
        setData();
    }

    public void setData(){
        nickText.setText((String)GetUserInfo.getData(this,"userName",""));
        idcardText.setText((String)GetUserInfo.getData(this,"idCard",""));
        signatureText.setText((String)GetUserInfo.getData(this,"sign",""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void findViews() {
        userHead = findView(R.id.user_head);
        nickLayout = findView(R.id.nick_layout);
        genderLayout = findView(R.id.gender_layout);
        birthLayout = findView(R.id.birth_layout);
        idcardLayout = findView(R.id.idcard_layout);
        firmLayout = findView(R.id.firm_layout);
        phoneLayout = findView(R.id.phone_layout);
        emailLayout = findView(R.id.email_layout);
        signatureLayout = findView(R.id.signature_layout);

        ((TextView) nickLayout.findViewById(R.id.attribute)).setText(R.string.nickname);
        ((TextView) genderLayout.findViewById(R.id.attribute)).setText(R.string.gender);
        ((TextView) birthLayout.findViewById(R.id.attribute)).setText(R.string.birthday);
        ((TextView) idcardLayout.findViewById(R.id.attribute)).setText(R.string.idcard);
        ((TextView) firmLayout.findViewById(R.id.attribute)).setText(R.string.firm);
        ((TextView) phoneLayout.findViewById(R.id.attribute)).setText(R.string.phone);
        ((TextView) emailLayout.findViewById(R.id.attribute)).setText(R.string.email);
        ((TextView) signatureLayout.findViewById(R.id.attribute)).setText(R.string.signature);

        iv_title_left = findViewById(R.id.iv_title_left);
        tv_title_right = findViewById(R.id.tv_title_right);
        nickText = (TextView) nickLayout.findViewById(R.id.value);
        genderText = (TextView) genderLayout.findViewById(R.id.value);
        birthText = (TextView) birthLayout.findViewById(R.id.value);
        idcardText = (TextView) idcardLayout.findViewById(R.id.value);
        firmText = (TextView) firmLayout.findViewById(R.id.value);
        phoneText = (TextView) phoneLayout.findViewById(R.id.value);
        emailText = (TextView) emailLayout.findViewById(R.id.value);
        signatureText = (TextView) signatureLayout.findViewById(R.id.value);

        findViewById(R.id.head_layout).setOnClickListener(this);
        iv_title_left.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        nickLayout.setOnClickListener(this);
        genderLayout.setOnClickListener(this);
        birthLayout.setOnClickListener(this);
        idcardLayout.setOnClickListener(this);
        firmLayout.setOnClickListener(this);
        phoneLayout.setOnClickListener(this);
        emailLayout.setOnClickListener(this);
        signatureLayout.setOnClickListener(this);
    }

    private void getUserInfo() {
        userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo == null) {
            NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {

                @Override
                public void onResult(boolean success, NimUserInfo result, int code) {
                    if (success) {
                        userInfo = result;
                        updateUI();
                    } else {
                        Toast.makeText(UserProfileSettingActivity.this, "getUserInfoFromRemote failed:" + code, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            updateUI();
        }

    }

    private void updateUI() {
        userHead.loadBuddyAvatar(account);
        //nickText.setText(userInfo.getName());
        if (userInfo.getGenderEnum() != null) {
            if (userInfo.getGenderEnum() == GenderEnum.MALE) {
                genderText.setText("男");
            } else if (userInfo.getGenderEnum() == GenderEnum.FEMALE) {
                genderText.setText("女");
            } else {
                genderText.setText("其他");
            }
        }
        if (userInfo.getBirthday() != null) {
            birthText.setText(userInfo.getBirthday());
        }
        if (userInfo.getMobile() != null) {
            phoneText.setText(userInfo.getMobile());
        }
        if (userInfo.getEmail() != null) {
            emailText.setText(userInfo.getEmail());
        }
        if (userInfo.getSignature() != null) {
            //signatureText.setText(userInfo.getSignature());
        }
//        idcardText.setText((String)GetUserInfo.getData(this,"idCard",""));
        firmText.setText((String)GetUserInfo.getData(this,"compName",""));
        phoneText.setText((String)GetUserInfo.getData(this,"mobileNum",""));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_title_right:
                checkData();
                break;
            case R.id.head_layout:
//                PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
//                option.titleResId = R.string.set_head_image;
//                option.crop = true;
//                option.multiSelect = false;
//                option.cropOutputImageWidth = 720;
//                option.cropOutputImageHeight = 720;
//                PickImageHelper.pickImage(UserProfileSettingActivity.this, PICK_AVATAR_REQUEST, option);
                choosePicturePop.showPopupWindow(userHead);
                choosePicturePop.setOnPopClickListener(new ChoosePicturePop.OnPopClickListener() {
                    @Override
                    public void onPhotograph() {
                        // 选择拍照
                        isCamera = true;
                        // TODO
                        HeadImageUtils
                                .getFromCamara(UserProfileSettingActivity.this);
                    }

                    @Override
                    public void onAlbums() {
                        // 选择相册
                        isCamera = false;
                        HeadImageUtils.getFromLocation(UserProfileSettingActivity.this);
                    }
                });
                break;
            case R.id.nick_layout:
                UserProfileEditItemActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_NICKNAME,
                        nickText.getText().toString());
                break;
            case R.id.gender_layout:
                UserProfileEditItemActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_GENDER,
                        String.valueOf(userInfo.getGenderEnum().getValue()));
                break;
            case R.id.birth_layout:
                UserProfileEditItemActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_BIRTH,
                        birthText.getText().toString());
                break;
            case R.id.idcard_layout:
                UserProfileEditItemActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_IDCARD,
                    idcardText.getText().toString());
                break;
            case R.id.firm_layout:
//                UserProfileEditItemActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_FIRM,
//                        firmText.getText().toString());
                break;
            case R.id.phone_layout:
//                UserProfileEditItemActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_PHONE,
//                        phoneText.getText().toString());
                break;
            case R.id.email_layout:
                UserProfileEditItemActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_EMAIL,
                        emailText.getText().toString());
                break;
            case R.id.signature_layout:
                UserProfileEditItemActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_SIGNATURE,
                        signatureText.getText().toString());
                break;
            }
    }

    public void uploadImage(File file){
        OkHttpUtils.post()
                .url(Constants.baseUrl+"/userapp/uploadUserHead")
                .addParams("userId",GetUserInfo.getUserId(this))
                .addFile("file","headerImg.png",file )
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                    }
                });

    }

    public void checkData(){
        if ("".equals(nickText.getText().toString())){
            Toast.makeText(this,"姓名不能为空",Toast.LENGTH_SHORT).show();
        }else if ("".equals(genderText.getText().toString())){
            Toast.makeText(this,"性别不能为空",Toast.LENGTH_SHORT).show();
        }else if ("".equals(birthText.getText().toString())){
            Toast.makeText(this,"生日不能为空",Toast.LENGTH_SHORT).show();
        }else if ("".equals(idcardText.getText().toString())){
            Toast.makeText(this,"身份证号不能为空",Toast.LENGTH_SHORT).show();
        }else if ("".equals(signatureText.getText().toString())){
            Toast.makeText(this,"签名不能为空",Toast.LENGTH_SHORT).show();
        }else if ("".equals(GetUserInfo.getHeaderUrl(this))){
            Toast.makeText(this,"头像不能为空",Toast.LENGTH_SHORT).show();
        }else {
            saveData(file);
        }
    }

    public void saveData(File file){
        PostFormBuilder postFormBuilder = OkHttpUtils.post()
                .url(Constants.baseUrl+"userapp/editUser");
        if (file !=null){
            postFormBuilder.addFile("file","headerImg.png",file);
        }
        postFormBuilder.addParams("uuid", GetUserInfo.getUserId(this))
                .addParams("username",nickText.getText().toString())
                .addParams("sex","0")
                .addParams("birthdayStr",birthText.getText().toString())
                .addParams("idcard",idcardText.getText().toString())
                .addParams("sign",signatureText.getText().toString())
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong1111111",response);
                        updateAvatar(path);
                        update(UserConstant.KEY_NICKNAME, nickText.getText().toString());
//                        update(UserConstant.KEY_GENDER, genderText.getText().toString());
//                        update(UserConstant.KEY_BIRTH, birthText.getText().toString());
                        update(UserConstant.KEY_PHONE, phoneText.getText().toString());
                        update(UserConstant.KEY_SIGNATURE, signatureText.getText().toString());
                        userData();
                    }
                });
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
                            GetUserInfo.putData(UserProfileSettingActivity.this,"deptId",object.optString("deptId"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"userId",object.optString("userId"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"userName",object.optString("userName"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"position",object.optString("position"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"mobileNum",object.optString("mobileNum"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"sign",object.optString("sign"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"idCard",object.optString("idCard"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"birthDay",object.optString("birthDay"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"sex",object.optString("sex"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"compName",object.optString("compName"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"reqStatus",object.optString("reqStatus"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"birthDayStr",object.optString("birthDayStr"));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"headUrl",object.optString("headUrl"));
                            EventBus.getDefault().post(new HeadUrlEventBusBean(object.optString("userName"),object.optString("headUrl")));
                            GetUserInfo.putData(UserProfileSettingActivity.this,"compId",object.optString("compId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == Activity.RESULT_OK && requestCode == PICK_AVATAR_REQUEST) {
//            String path = data.getStringExtra(com.qysd.uikit.business.session.constant.Extras.EXTRA_FILE_PATH);
//            updateAvatar(path);
//        }
        switch (requestCode) {
//            case ChoosePicturePop.PHOTOTAKE: // 拍照回执
//                if (resultCode != -1) {
//                    return;
//                }
//                try {
//                    //压缩后文件路径
//                    String filePath = BitmapUtils.compressImage(this, choosePicturePop.saveImagePath + "/" + choosePicturePop.getPicName(), choosePicturePop.getPicName(), 50);
//                    Glide.with(this).load(filePath).into(userHead);
//                    File file = new File(filePath);
//                    if (file.exists()) {
//                        //uploadImage(file,filePath,position);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                //uploadImage(choosePicturePop.getImageFile(),choosePicturePop.getPicName(),position);
//                break;
//            case ChoosePicturePop.PHOTOZOOM: // 相册回执
//                //resultPhotoZoom(data);
//                break;
            //头像剪切
            case HeadImageUtils.FROM_CRAMA:
                if (resultCode != -1) {
                    return;
                }
                if (HeadImageUtils.photoCamare != null) {
                    cutCorePhoto(HeadImageUtils.photoCamare);
                }
                break;
            case HeadImageUtils.FROM_LOCAL:
                if (data != null && data.getData() != null) {
                    cutCorePhoto(data.getData());
                }
                break;
            case HeadImageUtils.FROM_CUT:
                if (data == null)
                    return;
                if (HeadImageUtils.cutPhoto != null) {
                    uri = HeadImageUtils.cutPhoto;
                    String[] proj = {MediaStore.Images.Media.DATA,
                            MediaStore.Images.Thumbnails.DATA,
                            MediaStore.Images.ImageColumns.DATA};
                    android.support.v4.content.CursorLoader loader = new android.support.v4.content.CursorLoader(this, uri, proj, null,
                            null, null);
                    Cursor cursor = loader.loadInBackground();
                    if (cursor != null) {
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        path = cursor.getString(column_index);// 图片在的路径
                        cursor.close();
                    } else {
                        path = uri.toString().substring(7);
                    }
                    Log.e("songlonglong",path);
                    Glide.with(this).load(path).into(userHead);
                    file = new File(path);
                    if (file.exists()) {
                        //uploadImage(file);
                    } else {
                        Toast.makeText(this, "选择图片失败，请重试", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;
        }
    }

    /**
     * 头像剪切
     * @param uri
     */
    public void cutCorePhoto(Uri uri) {
        String urlpath = "";
        HeadImageUtils.cutPhoto = HeadImageUtils.setCutUriImage(this);

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 10;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        String[] proj = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.ImageColumns.DATA};
        android.support.v4.content.CursorLoader loader = new android.support.v4.content.CursorLoader(this, uri, proj, null, null,
                null);
        Cursor cursor = loader.loadInBackground();
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            urlpath = cursor.getString(column_index);// 图片在的路径
            cursor.close();
        } else {
            urlpath = uri.toString().substring(7);
        }
        File file = new File(urlpath);
        /**
         * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
         */
        cameraBitmap = PictureUtils.getSmallBitmap(urlpath);
        bitmap = cameraBitmap;
        //部分三星手机图片旋转90度解决方法,把图片旋转为正的方向
        int degree = HeadImageUtils.readPictureDegree(file.getAbsolutePath());
        bitmap = HeadImageUtils.rotaingImageView(degree, bitmap);
        uri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, null, null));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, HeadImageUtils.cutPhoto);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, HeadImageUtils.FROM_CUT);
    }

    /**
     * 提交网易云数据
     *
     * @param content
     */
    private void update(int key, Serializable content) {
        RequestCallbackWrapper callback = new RequestCallbackWrapper() {
            @Override
            public void onResult(int code, Object result, Throwable exception) {
                DialogMaker.dismissProgressDialog();
                if (code == ResponseCode.RES_SUCCESS) {
                    onUpdateCompleted();
                } else if (code == ResponseCode.RES_ETIMEOUT) {
                    //Toast.makeText(PersonalInfoActivity.this, R.string.user_info_update_failed, Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (key == UserConstant.KEY_ALIAS) {
            DialogMaker.showProgressDialog(this, null, true);
            Map<FriendFieldEnum, Object> map = new HashMap<>();
            map.put(FriendFieldEnum.ALIAS, content);
            NIMClient.getService(FriendService.class).updateFriendFields((String) content, map).setCallback(callback);
        } else {
            if (fieldMap == null) {
                fieldMap = new HashMap<>();
                fieldMap.put(UserConstant.KEY_NICKNAME, UserInfoFieldEnum.Name);
                fieldMap.put(UserConstant.KEY_PHONE, UserInfoFieldEnum.MOBILE);
                fieldMap.put(UserConstant.KEY_SIGNATURE, UserInfoFieldEnum.SIGNATURE);
                fieldMap.put(UserConstant.KEY_EMAIL, UserInfoFieldEnum.EMAIL);
                fieldMap.put(UserConstant.KEY_BIRTH, UserInfoFieldEnum.BIRTHDAY);
                fieldMap.put(UserConstant.KEY_GENDER, UserInfoFieldEnum.GENDER);
            }
            DialogMaker.showProgressDialog(this, null, true);
            UserUpdateHelper.update(fieldMap.get(key), content, callback);
        }
    }

    private void onUpdateCompleted() {
        showKeyboard(false);
        //Toast.makeText(PersonalInfoActivity.this, R.string.user_info_update_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 更新头像
     */
    private void updateAvatar(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file == null) {
            return;
        }

        DialogMaker.showProgressDialog(this, null, null, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelUpload(R.string.user_info_update_cancel);
            }
        }).setCanceledOnTouchOutside(true);

        LogUtil.i(TAG, "start upload avatar, local file path=" + file.getAbsolutePath());
        new Handler().postDelayed(outimeTask, AVATAR_TIME_OUT);
        uploadAvatarFuture = NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG);
        uploadAvatarFuture.setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int code, String url, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && !TextUtils.isEmpty(url)) {
                    LogUtil.i(TAG, "upload avatar success, url =" + url);

                    UserUpdateHelper.update(UserInfoFieldEnum.AVATAR, url, new RequestCallbackWrapper<Void>() {
                        @Override
                        public void onResult(int code, Void result, Throwable exception) {
                            if (code == ResponseCode.RES_SUCCESS) {
                                Toast.makeText(UserProfileSettingActivity.this, R.string.head_update_success, Toast.LENGTH_SHORT).show();
                                onUpdateDone();
                            } else {
                                Toast.makeText(UserProfileSettingActivity.this, R.string.head_update_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }); // 更新资料
                } else {
                    Toast.makeText(UserProfileSettingActivity.this, R.string.user_info_update_failed, Toast
                            .LENGTH_SHORT).show();
                    onUpdateDone();
                }
            }
        });
    }

    private void cancelUpload(int resId) {
        if (uploadAvatarFuture != null) {
            uploadAvatarFuture.abort();
            Toast.makeText(UserProfileSettingActivity.this, resId, Toast.LENGTH_SHORT).show();
            onUpdateDone();
        }
    }

    private Runnable outimeTask = new Runnable() {
        @Override
        public void run() {
            cancelUpload(R.string.user_info_update_failed);
        }
    };

    private void onUpdateDone() {
        uploadAvatarFuture = null;
        DialogMaker.dismissProgressDialog();
        getUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDataPersonalInfo(PersonalInfoBean data) {
        Log.e("jjj", "收到了事件" + data.getStr());
        if (data.getType() == UserConstant.KEY_NICKNAME)
            nickText.setText(data.getStr());
        if (data.getType() == UserConstant.KEY_IDCARD)
            idcardText.setText(data.getStr());
        if (data.getType() == UserConstant.KEY_PHONE)
            phoneText.setText(data.getStr());
        if (data.getType() == UserConstant.KEY_BIRTH)
            birthText.setText(data.getStr());
        if (data.getType() == UserConstant.KEY_SIGNATURE)
            signatureText.setText(data.getStr());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package com.qysd.lawtree.lawtreeactivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.TxlPersonBean;
import com.qysd.lawtree.lawtreebean.TxlPersonBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.LoadDialog;
import com.qysd.lawtree.lawtreeutils.NullStringToEmptyAdapterFactory;
import com.qysd.lawtree.lawtreeutils.VerificationUtil;
import com.qysd.lawtree.lawtreeview.fancyindexer.adapter.PingyinAdapter;
import com.qysd.lawtree.lawtreeview.fancyindexer.ui.FancyIndexer;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/26.
 * 添加好友获取用户手机自由通讯录的数据上传后台，请求下来，进行展示是否为添加或者邀请
 */

public class AddFriendActivity extends BaseActivity {
    private RelativeLayout rl_add_phone;//手机号添加
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private String usernameStr = "";//通讯录人名
    private String phoneStr = "";//通讯录电话
    private TxlPersonBean txlPersonBean;
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory
            (new NullStringToEmptyAdapterFactory())
            .create();
    private List<TxlPersonBean.Status> txlPersonBeanList = new ArrayList<>();
    private PingyinAdapter<TxlPersonBean.Status> adapter;
    private ExpandableListView lv_content;
    /**
     * 获取库Phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    private static final int PHONES_DISPLAY_NAME = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER = 1;
    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID = 3;

    /**
     * 联系人名称
     **/
    private ArrayList<String> mContactsName = new ArrayList<String>();

    /**
     * 联系人号码
     **/
    private ArrayList<String> mContactsNumber = new ArrayList<String>();

    /**
     * 联系人头像
     **/
    private ArrayList<Bitmap> mContactsImg = new ArrayList<Bitmap>();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_add_friend);
        initTitle(R.drawable.ic_left_jt, "添加好友");
    }

    @Override
    protected void initView() {
        rl_add_phone = findViewById(R.id.rl_add_phone);
        lv_content = findViewById(R.id.lv_content);
        lv_content.setGroupIndicator(null);//设置默认的ExpandableListView的左边箭头
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            doBackup();
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }

    private void doBackup() {
        getPhoneContacts();
    }

    @Override
    protected void bindListener() {
        rl_add_phone.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_phone:
                startActivity(new Intent(this, AddSearchPhoneActivity.class));
                break;
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                doBackup();

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("查找通讯录需要访问 “通讯录” 和 “外部存储器”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void getPhoneContacts() {
        LoadDialog.show(this);
        // rely=(RelativeLayout) findViewById(R.id.relationId);
        ContentResolver resolver = this.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        // 不为空
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);
                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID);
                // 得到联系人头像ID
                Long imgid = phoneCursor.getLong(PHONES_PHOTO_ID);
                // 得到联系人头像Bitamp
                Bitmap bitmap = null;
                // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (imgid > 0) {
                    Uri uri = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts
                            .openContactPhotoInputStream(resolver, uri);
                    bitmap = BitmapFactory.decodeStream(input);
                } else {
                    // 设置默认
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_account);
                }
                String[] split = phoneNumber.split(" ");
                String number = "";
                for (int i = 0; i < split.length; i++) {
                    number = number + split[i];
                }
                if (VerificationUtil.isValidTelNumber(number) && contactName != "") {
                    mContactsNumber.add(number);
                    mContactsName.add(contactName);
                    mContactsImg.add(bitmap);
                }

            }
            phoneCursor.close();
        }
        Log.e("info", "contactName---" + mContactsName.size() + "phoneNum---" + mContactsNumber.size());
        for (int i = 0; i < mContactsName.size(); i++) {
            usernameStr = usernameStr + mContactsName.get(i) + ";";
        }
        for (int i = 0; i < mContactsNumber.size(); i++) {
            phoneStr = phoneStr + mContactsNumber.get(i) + ";";
        }
        //进行本地数据的提交
        commitData(usernameStr, phoneStr);

    }

    private void commitData(String usernameStr, String phoneStr) {
        OkHttpUtils.get().url(Constants.baseUrl + "friend/selectTelphoneList")
                .addParams("usernameStr", usernameStr)
                .addParams("phoneStr", phoneStr)
                .addParams("userId", "fdb4f3e1e97e458db2d39f4a5267ece1")
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        //{"code":"0","status":"服务器异常"}
                        Log.e("lzq", response.toString());
                        LoadDialog.dismiss(AddFriendActivity.this);
                        if (response.contains("服务器异常")) {
                            return;
                        }
                        txlPersonBean = gson.fromJson(response, TxlPersonBean.class);
                        if ("1".equals(txlPersonBean.getCode())) {
                            txlPersonBeanList = txlPersonBean.getStatus();
                            adapter = new PingyinAdapter<TxlPersonBean.Status>(lv_content, txlPersonBeanList, R.layout.item_txl_friend) {

                                @Override
                                public String getItemName(TxlPersonBean.Status goodMan) {
                                    return goodMan.getUserName();
                                }

                                /**
                                 * 返回viewholder持有
                                 */
                                @Override
                                public ViewHolder getViewHolder(TxlPersonBean.Status goodMan) {
                                    /**View holder*/
                                    class DataViewHolder extends ViewHolder implements View.OnClickListener, View.OnLongClickListener {
                                        public TextView tv_name, tv_department;
                                        public ImageView iv_person;

                                        private TextView tv_add_state, tv_add, tv_yaoqing;

                                        public DataViewHolder(TxlPersonBean.Status goodMan) {
                                            super(goodMan);
                                        }

                                        /**初始化*/
                                        @Override
                                        public ViewHolder getHolder(View view) {
                                            tv_name = view.findViewById(R.id.tv_name);
                                            tv_department = view.findViewById(R.id.tv_department);
                                            iv_person = view.findViewById(R.id.iv_person);

                                            tv_add = view.findViewById(R.id.tv_add);
                                            tv_yaoqing = view.findViewById(R.id.tv_yaoqing);
                                            tv_add_state = view.findViewById(R.id.tv_add_state);
                                            /**在这里设置点击事件*/
                                            view.setOnClickListener(this);
                                            view.setOnLongClickListener(this);
                                            tv_yaoqing.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Toast.makeText(AddFriendActivity.this, "邀请", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            return this;
                                        }

                                        /**显示数据*/
                                        @Override
                                        public void show() {
                                            tv_name.setText(getItem().getUserName());
                                            tv_department.setText(getItem().getMobileNum());
                                            //GlideImgManager.loadCircleImage(LianXiRenActivity.this, getItem().getImageUrl(), iv_person);
                                            if ("1".equals(getItem().getStatus())) {
                                                tv_add.setVisibility(View.GONE);
                                                tv_yaoqing.setVisibility(View.VISIBLE);
                                                tv_add_state.setVisibility(View.GONE);
                                            } else if ("2".equals(getItem().getStatus())) {
                                                tv_add.setVisibility(View.VISIBLE);
                                                tv_yaoqing.setVisibility(View.GONE);
                                                tv_add_state.setVisibility(View.GONE);
                                            } else if ("3".equals(getItem().getStatus())) {
                                                tv_add.setVisibility(View.GONE);
                                                tv_yaoqing.setVisibility(View.GONE);
                                                tv_add_state.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        /**点击事件*/
                                        @Override
                                        public void onClick(View v) {
                                            //startActivity(new Intent(getActivity(), LianXiRenDetailActivity.class));
                                            //Toast.makeText(v.getContext(), getItem().getUserName(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public boolean onLongClick(View v) {
                                            return false;
                                        }
                                    }
                                    return new DataViewHolder(goodMan);
                                }

                                @Override
                                public void onItemClick(TxlPersonBean.Status data, View view, int position) {

                                }
                            };
                            /**展开并设置adapter*/
                            adapter.expandGroup();

                            FancyIndexer mFancyIndexer = (FancyIndexer) findViewById(R.id.bar);
                            mFancyIndexer.setOnTouchLetterChangedListener(new FancyIndexer.OnTouchLetterChangedListener() {

                                @Override
                                public void onTouchLetterChanged(String letter) {
                                    for (int i = 0, j = adapter.getKeyMapList().getTypes().size(); i < j; i++) {
                                        String str = adapter.getKeyMapList().getTypes().get(i);
                                        if (letter.toUpperCase().equals(str.toUpperCase())) {
                                            /**跳转到选中的字母表*/
                                            lv_content.setSelectedGroup(i);
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
    }
}

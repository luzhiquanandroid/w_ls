package com.qysd.lawtree.lawtreeactivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.GlideUtils;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class MyZxingPic extends BaseActivity {

    private ImageView iv_zxing_code,iv_header;
    private TextView tv_name;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_myzxing_pic);
        initTitle(R.drawable.ic_left_jt,"我的二维码");
        createEnglishQRCode();
    }

    @Override
    protected void initView() {
        iv_zxing_code = findViewById(R.id.iv_zxing_code);
        tv_name = findViewById(R.id.tv_name);
        iv_header = findViewById(R.id.iv_header);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        tv_name.setText((String)GetUserInfo.getData(this,"userName",""));
        GlideUtils.loadCircleImage(this,GetUserInfo.getHeaderUrl(this),iv_header);
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {

    }

    private void createEnglishQRCode() {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode("com.lvshu.addFriend/"+ GetUserInfo.getUserId(MyZxingPic.this), BGAQRCodeUtil.dp2px(MyZxingPic.this, 150), Color.parseColor("#000000"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_zxing_code.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(MyZxingPic.this, "生成英文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}

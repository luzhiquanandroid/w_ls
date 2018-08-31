package com.qysd.lawtree.lawtreeutils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.qysd.lawtree.R;

import java.io.File;

/**
 * Created by jcd on 2016/1/16.
 */
public class ChoosePicturePop {

    private PopupWindow popWindow;
    private Activity mContext;
    public static final int PHOTOZOOM = 32; // 相册
    public static final int PHOTOTAKE = 33; // 拍照
    public String saveImagePath = Environment.getExternalStorageDirectory().getPath() + "/lawtree/image";
    private File imageFile;
    private String photoSaveName;

    public ChoosePicturePop(Activity context) {
        mContext = context;
    }

    public void showPopupWindow(View parent) {
        if (popWindow == null) {
            View view = View.inflate(mContext, R.layout.pop_select_photo, null);
            popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, true);
            initPop(view);

            popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
            popWindow.setFocusable(true);
            popWindow.setOutsideTouchable(true);
            popWindow.setBackgroundDrawable(new BitmapDrawable());
            popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        }

    }

    private void initPop(View view) {
        view.findViewById(R.id.view_tran).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popWindow.dismiss();
                    }
                });
        TextView photograph = (TextView) view.findViewById(R.id.photograph);// 拍照
        TextView albums = (TextView) view.findViewById(R.id.albums);// 相册
        LinearLayout cancel = (LinearLayout) view.findViewById(R.id.cancel);// 取消
        // 相机拍照
        photograph.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
                if(listener == null) {
                    SelectPhotograph();
                }else{
                    listener.onPhotograph();
                }
            }
        });
        // 选取相册
        albums.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
                if (listener == null) {
                    SelectAlbums();
                }else{
                    listener.onAlbums();
                }
            }
        });
        // 取消，关闭pop
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
            }
        });
    }

    /**
     * 选择相册
     */
    private void SelectAlbums() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK, null);
        openAlbumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mContext.startActivityForResult(openAlbumIntent, PHOTOZOOM);
    }

    /**
     * 选择拍照
     */
    private void SelectPhotograph() {
        /*String */photoSaveName = System.currentTimeMillis() + ".jpg";
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(saveImagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        imageFile = new File(saveImagePath, photoSaveName);
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion < Build.VERSION_CODES.N) {
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            mContext.startActivityForResult(openCameraIntent, PHOTOTAKE);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());
            Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            mContext.startActivityForResult(openCameraIntent, PHOTOTAKE);
        }
    }

    //附件名
    public String getPicName(){
        return photoSaveName;
    }

    public File getImageFile(){
        return imageFile;
    }

    private OnPopClickListener listener;

    public void setOnPopClickListener(OnPopClickListener listener) {
        this.listener = listener;
    }

    public interface OnPopClickListener {
        void onPhotograph();

        void onAlbums();
    }
}

package com.qysd.lawtree.lawtreeactivity;

import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.qysd.lawtree.NimApplication;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.PickerCityBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.ChoosePicturePop;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.GlideUtils;
import com.qysd.lawtree.main.activity.MainActivity3;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.qysd.lawtree.lawtreeutils.BitmapUtils.compressImage;

/**
 * 我的企业界面
 */
public class MyBusinessActivity extends BaseActivity{

    private RelativeLayout rl_code,rl_createtime;
    private TextView tv_enterprise_coding,tv_createtime,tv_location,tv_exit;
    private TextView tv_name,tv_num,tv_address,tv_scope_of_business,tv_industry,
            tv_corporate_representative,tv_phone;
    private ImageView iv_logo;
    private ChoosePicturePop choosePicturePop;//选择图片工具证件
    private File file;
    //省市区集合
    private ArrayList<PickerCityBean> options1Items = NimApplication.options1Items;
    private ArrayList<ArrayList<PickerCityBean>> options2Items = NimApplication.options2Items;
    private ArrayList<ArrayList<ArrayList<PickerCityBean>>> options3Items = NimApplication.options3Items;
    private OptionsPickerView pvOptions;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_my_business);
        initTitle(R.drawable.ic_left_jt,"我的企业","");
        choosePicturePop = new ChoosePicturePop(this);
    }

    @Override
    protected void initView() {
        rl_code = findViewById(R.id.rl_code);
        rl_createtime = findViewById(R.id.rl_createtime);
        tv_enterprise_coding = findViewById(R.id.tv_enterprise_coding);
        tv_createtime = findViewById(R.id.tv_createtime);
        tv_location = findViewById(R.id.tv_location);
        tv_name = findViewById(R.id.tv_name);
        tv_num = findViewById(R.id.tv_num);
        tv_address = findViewById(R.id.tv_address);
        tv_scope_of_business = findViewById(R.id.tv_scope_of_business);
        tv_industry = findViewById(R.id.tv_industry);
        tv_corporate_representative = findViewById(R.id.tv_corporate_representative);
        tv_phone = findViewById(R.id.tv_phone);
        iv_logo = findViewById(R.id.iv_logo);
        tv_exit = findViewById(R.id.tv_exit);
        if ("1".equals(GetUserInfo.getData(this,"isAdmin",""))){
            tv_exit.setVisibility(View.GONE);
        }else {
            tv_exit.setVisibility(View.VISIBLE);
        }
        initOptionPicker();
    }

    @Override
    protected void bindListener() {
        tv_location.setOnClickListener(this);
        iv_logo.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_enterprise_coding.setText((String)GetUserInfo.getData(this,"entecode",""));
        tv_createtime.setText((String)GetUserInfo.getData(this,"createTimeStr",""));
        tv_name.setText((String)GetUserInfo.getData(this,"compname",""));
        tv_num.setText((String)GetUserInfo.getData(this,"registercode",""));
        tv_address.setText((String)GetUserInfo.getData(this,"address",""));
        tv_scope_of_business.setText((String)GetUserInfo.getData(this,"scopmuch",""));
        tv_location.setText(GetUserInfo.getData(this,"provinceName","")+" "
                            +GetUserInfo.getData(this,"cityName","")+" "
                            +GetUserInfo.getData(this,"countyName","")+" ");
        tv_industry.setText((String)GetUserInfo.getData(this,"trade",""));
        tv_corporate_representative.setText((String)GetUserInfo.getData(this,"legalperson",""));
        tv_phone.setText((String)GetUserInfo.getData(this,"contectnum",""));
        GlideUtils.loadCircleImage(this,(String) GetUserInfo.getData(this,"logourl",""),iv_logo);
    }

    @Override
    protected void initNav() {
        if (!"".equals(GetUserInfo.getData(this,"entecode",""))){
            rl_code.setVisibility(View.VISIBLE);
            rl_createtime.setVisibility(View.VISIBLE);
        }else {
            rl_code.setVisibility(View.GONE);
            rl_createtime.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onClickTitleRight(View v) {
        super.onClickTitleRight(v);
//        checkData();
        //Toast.makeText(MyBusinessActivity.this,"dkfsdf",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.tv_location:
//                if (pvOptions != null) {
//                    pvOptions.show(); //弹出地区选择器
//                }
//                break;
//            case R.id.iv_logo:
//                choosePicturePop.showPopupWindow(iv_logo);
//                break;
            case R.id.tv_exit:
                exitComp();
                break;
        }
    }

    public void exitComp(){
        OkHttpUtils.post()
                .url(Constants.baseUrl+"/company/updateOrgUserComp")
                .addParams("userId",GetUserInfo.getUserId(this))
                .addParams("compId",(String)GetUserInfo.getData(this,"compId",""))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("退出企业",response);
                        GetUserInfo.putData(MyBusinessActivity.this,"entecode","");
                        finish();
                    }
                });
    }

    /**
     * 判断必填信息是否为空
     */
//    public void checkData(){
//        if ("".equals(et_name.getText().toString())){
//            Toast.makeText(this,"企业名称不能为空",Toast.LENGTH_SHORT).show();
//        }else if ("".equals(et_num.getText().toString())){
//            Toast.makeText(this,"执业注册号不能为空",Toast.LENGTH_SHORT).show();
//        }else if ("".equals(et_address.getText().toString())){
//            Toast.makeText(this,"企业地址不能为空",Toast.LENGTH_SHORT).show();
//        }else if ("".equals(et_scope_of_business.getText().toString())){
//            Toast.makeText(this,"经营范围不能为空",Toast.LENGTH_SHORT).show();
//        }else if ("".equals(et_industry.getText().toString())){
//            Toast.makeText(this,"所属行业不能为空",Toast.LENGTH_SHORT).show();
//        }else if ("".equals(et_corporate_representative.getText().toString())){
//            Toast.makeText(this,"法人代表不能为空",Toast.LENGTH_SHORT).show();
//        }else if ("".equals(et_phone.getText().toString())){
//            Toast.makeText(this,"联系电话不能为空",Toast.LENGTH_SHORT).show();
//        }else if ("".equals(GetUserInfo.getData(this,"logourl",""))){
//            Toast.makeText(this,"企业logo不能为空",Toast.LENGTH_SHORT).show();
//        }else {
//            if (!"".equals(GetUserInfo.getData(this,"entecode",""))){
//                commitEditData();
//            }else {
//                commitData();
//            }
//
//        }
//    }

    /**
     * 上传企业信息
     */
//    public void commitData(){
//        OkHttpUtils.post()
//                .url(Constants.baseUrl+"company/add")
//                .addParams("userId", GetUserInfo.getUserId(this))
//                .addParams("compname",et_name.getText().toString())
//                .addParams("registercode",et_num.getText().toString())
//                .addParams("address",et_address.getText().toString())
//                .addParams("scopmuch",et_scope_of_business.getText().toString())
//                .addParams("province",(String) GetUserInfo.getData(this,"provinceId",""))
//                .addParams("city",(String) GetUserInfo.getData(this,"cityId",""))
//                .addParams("county",(String) GetUserInfo.getData(this,"districtId",""))
//                .addParams("trade",et_industry.getText().toString())
//                .addParams("legalperson",et_corporate_representative.getText().toString())
//                .addParams("contectnum",et_phone.getText().toString())
//                .addFile("file","logo.png",file)
//                .build()
//                .execute(new UserCallback() {
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.e("songlonglong",response);
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            if ("0".equals(object.optString("code"))){
//                                Toast.makeText(MyBusinessActivity.this,"用户已存在企业",Toast.LENGTH_SHORT).show();
//                            }else if ("1".equals(object.optString("code"))){
//                                Toast.makeText(MyBusinessActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
//                                myCompanyData();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        finish();
//                    }
//                });
//    }

    /**
     * 上传企业信息
     */
//    public void commitEditData(){
//        PostFormBuilder postFormBuilder = OkHttpUtils.post()
//                .url(Constants.baseUrl+"company/edit");
//        postFormBuilder.addParams("userId", GetUserInfo.getUserId(this))
//                .addParams("compid", (String)GetUserInfo.getData(this,"compId",""))
//                .addParams("compname",et_name.getText().toString())
//                .addParams("registercode",et_num.getText().toString())
//                .addParams("address",et_address.getText().toString())
//                .addParams("scopmuch",et_scope_of_business.getText().toString())
//                .addParams("province",(String) GetUserInfo.getData(this,"provinceId",""))
//                .addParams("city",(String) GetUserInfo.getData(this,"cityId",""))
//                .addParams("county",(String) GetUserInfo.getData(this,"districtId",""))
//                .addParams("trade",et_industry.getText().toString())
//                .addParams("legalperson",et_corporate_representative.getText().toString())
//                .addParams("contectnum",et_phone.getText().toString());
//                if (file != null) {
//                    postFormBuilder.addFile("file","logo.png",file);
//                }
//                postFormBuilder.build()
//                .execute(new UserCallback() {
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.e("songlonglong1",response);
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            if ("0".equals(object.optString("code"))){
//                                Toast.makeText(MyBusinessActivity.this,"用户已存在企业",Toast.LENGTH_SHORT).show();
//                            }else if ("1".equals(object.optString("code"))){
//                                myCompanyData();
//                                Toast.makeText(MyBusinessActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        finish();
//                    }
//                });
//    }

    public void myCompanyData() {
        OkHttpUtils.get()
                .url(Constants.baseUrl+"company/selectMyCompany")
                .addParams("userId",GetUserInfo.getUserId(this))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            GetUserInfo.putData(MyBusinessActivity.this,"compname",object.optString("compname"));
                            GetUserInfo.putData(MyBusinessActivity.this,"entecode",object.optString("entecode"));
                            GetUserInfo.putData(MyBusinessActivity.this,"trade",object.optString("trade"));
                            GetUserInfo.putData(MyBusinessActivity.this,"address",object.optString("address"));
                            GetUserInfo.putData(MyBusinessActivity.this,"logourl",object.optString("logourl"));
                            GetUserInfo.putData(MyBusinessActivity.this,"legalperson",object.optString("legalperson"));
                            GetUserInfo.putData(MyBusinessActivity.this,"contectnum",object.optString("contectnum"));
                            GetUserInfo.putData(MyBusinessActivity.this,"createby",object.optString("createby"));
                            GetUserInfo.putData(MyBusinessActivity.this,"createtime",object.optString("createtime"));
                            GetUserInfo.putData(MyBusinessActivity.this,"registercode",object.optString("registercode"));
                            GetUserInfo.putData(MyBusinessActivity.this,"scopmuch",object.optString("scopmuch"));
                            GetUserInfo.putData(MyBusinessActivity.this,"createTimeStr",object.optString("createTimeStr"));
                            GetUserInfo.putData(MyBusinessActivity.this,"provinceName",object.optString("provinceName"));
                            GetUserInfo.putData(MyBusinessActivity.this,"cityName",object.optString("cityName"));
                            GetUserInfo.putData(MyBusinessActivity.this,"countyName",object.optString("countyName"));
                            GetUserInfo.putData(MyBusinessActivity.this,"province",object.optString("province"));
                            GetUserInfo.putData(MyBusinessActivity.this,"city",object.optString("city"));
                            GetUserInfo.putData(MyBusinessActivity.this,"county",object.optString("county"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ChoosePicturePop.PHOTOTAKE: // 拍照回执
                if (resultCode != -1) {
                    return;
                }
                try {
                    //压缩后文件路径
                    //Log.e("filename", choosePicturePop.saveImagePath + "/" + choosePicturePop.getPicName());
                    String filePath = compressImage(this, choosePicturePop.saveImagePath + "/" + choosePicturePop.getPicName(), choosePicturePop.getPicName(), 50);
                    //String filePath = choosePicturePop.saveImagePath+ "/" + choosePicturePop.getPicName();
                    Log.e("songlonglong", filePath);
                    GlideUtils.loadCircleImage(this,filePath,iv_logo);
                    file = new File(filePath);
                    if (file.exists()) {
                        //uploadImage(file,filePath,position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ChoosePicturePop.PHOTOZOOM: // 相册回执
                resultPhotoZoom(data);
                break;
        }
    }

    /**
     * 相册回执
     *
     * @param data
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void resultPhotoZoom(Intent data) {
        String checkImagePath = "";//选择相册照片路径
        if (data == null) {
            return;
        }
        Uri uri = data.getData();
        if (uri == null) {
            return;
        }
        String[] proj = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.ImageColumns.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null,
                null);
        Cursor cursor = loader.loadInBackground();
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            checkImagePath = cursor.getString(column_index);// 图片在的路径
            cursor.close();
        } else {
            checkImagePath = uri.toString().substring(7);
        }
        try {
            //压缩后文件路径
            Log.e("songlonglong", checkImagePath);
            String filePath = compressImage(this, checkImagePath, checkImagePath.substring(checkImagePath.lastIndexOf("/")+1,checkImagePath.length()) , 50);
            Log.e("songlonglong", filePath);
            //String filePath = checkImagePath;
            GlideUtils.loadCircleImage(this,filePath,iv_logo);
            file = new File(filePath);
            if (file.exists()) {
                //uploadImage(file,filePath,position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOptionPicker() {
        //条件选择器初始化
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + " "
                        + options2Items.get(options1).get(option2).getName() + " "
                        + options3Items.get(options1).get(option2).get(options3).getPickerViewText();
//                String tx = options1Items.get(options1).getId()+" "
//                        +options2Items.get(options1).get(option2).getId()+" "
//                        +options3Items.get(options1).get(option2).get(options3).getId();
                //保存省市区id
                GetUserInfo.putData(MyBusinessActivity.this, "provinceId", options1Items.get(options1).getId());
                GetUserInfo.putData(MyBusinessActivity.this, "cityId", options2Items.get(options1).get(option2).getId());
                GetUserInfo.putData(MyBusinessActivity.this, "districtId", options3Items.get(options1).get(option2).get(options3).getId());
                tv_location.setText(tx);
            }
        })
//                .setSubmitText("确定")
//                .setCancelText("取消")
                .setTitleText("城市选择")
//                .setTitleSize(20)
//                .setSubCalSize(18)//确定取消按钮大小
//                .setTitleColor(Color.BLACK)
//                .setSubmitColor(Color.BLUE)
//                .setCancelColor(Color.BLUE)
//                .setBgColor(Color.WHITE)
//                .setLinkage(false)//default true
//                .setCyclic(false, false, false)//循环与否
//                .setOutSideCancelable(false)//点击外部dismiss, default true
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
//                .setLabels("省", "市", "区")//设置选择的三级单位
//                .setLineSpacingMultiplier(2.0f) //设置两横线之间的间隔倍数（范围：1.2 - 2.0倍 文字高度）
                //               .setDivi
                // derColor(Color.RED)//设置分割线的颜色
//                .setDividerType(WheelView.DividerType.WARP)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setContentTextSize(20)//设置滚轮文字大小
//                .setSelectOptions(0,1,2)  //设置默认选中项
                .isDialog(false)//设置为对话框模式
                .setOutSideCancelable(false)
                .build();
//       pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (pvDataStart.isShowing()) {
//                pvDataStart.dismiss();
//                return true;
//            }
//            if (pvDataEnd.isShowing()) {
//                pvDataEnd.dismiss();
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}

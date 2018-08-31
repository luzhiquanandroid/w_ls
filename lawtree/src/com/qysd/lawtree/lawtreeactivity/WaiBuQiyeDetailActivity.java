package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import static com.qysd.lawtree.lawtreeutils.PhoneUtils.openDial;

/**
 * Created by QYSD_AD on 2018/3/26.
 * 企业详情
 */

public class WaiBuQiyeDetailActivity extends BaseActivity {
    private RelativeLayout rl_qita_lianxiren;//其他联系人
    private WaiBuQiyeBean.Status status;
    private TextView tv_comp_name, tv_gys, tv_kh, tv_wx;//公司名 供应商 客户 外协
    //业务人  业务人电话 企业id  企业分管部门 企业分管人员
    private TextView tv_comp_yw_name, tv_comp_yw_tel, tv_comp_id, tv_comp_dept, tv_comp_fg_name;
    private HeadImageView iv_comp_logo;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_qiye_detail);
        initTitle(R.drawable.ic_left_jt, "企业详情");
    }

    @Override
    protected void initView() {
        Bundle detail = getIntent().getBundleExtra("detail");
        status = (WaiBuQiyeBean.Status) detail.getSerializable("status");
        rl_qita_lianxiren = findViewById(R.id.rl_qita_lianxiren);
        tv_comp_name = findViewById(R.id.tv_comp_name);
        tv_gys = findViewById(R.id.tv_gys);
        tv_kh = findViewById(R.id.tv_kh);
        tv_wx = findViewById(R.id.tv_wx);
        tv_comp_yw_name = findViewById(R.id.tv_comp_yw_name);
        tv_comp_yw_tel = findViewById(R.id.tv_comp_yw_tel);
        tv_comp_id = findViewById(R.id.tv_comp_id);
        tv_comp_dept = findViewById(R.id.tv_comp_dept);
        tv_comp_fg_name = findViewById(R.id.tv_comp_fg_name);
        iv_comp_logo = findViewById(R.id.iv_comp_logo);
    }

    @Override
    protected void bindListener() {
        rl_qita_lianxiren.setOnClickListener(this);
        tv_comp_yw_tel.setOnClickListener(this);
        tv_comp_yw_name.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        iv_comp_logo.loadAvatar(status.getLogoUrl());
        tv_comp_name.setText(status.getCompName());
        tv_comp_id.setText("企业ID:" + status.getSignId());
        tv_comp_yw_name.setText(status.getUserName());
        tv_comp_yw_tel.setText(status.getMobileNum());
        tv_comp_dept.setText(status.getDepName());
        tv_comp_fg_name.setText(status.getDepUserName());

        switch (status.getProperty()) {
            case "1":
                tv_kh.setVisibility(View.VISIBLE);
                tv_gys.setVisibility(View.GONE);
                break;
            case "2":
                tv_kh.setVisibility(View.GONE);
                tv_gys.setVisibility(View.VISIBLE);
                break;
            case "3":
                tv_kh.setVisibility(View.VISIBLE);
                tv_gys.setVisibility(View.VISIBLE);
                break;
            default:
                tv_kh.setVisibility(View.GONE);
                tv_gys.setVisibility(View.GONE);
                break;
        }
        if ("1".equals(status.getIfHelp())) {
            tv_wx.setVisibility(View.VISIBLE);
        } else {
            tv_wx.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initNav() {

    }

    private Intent intent;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_qita_lianxiren:
                intent = new Intent(this, WaiBuOtherLianxirenActivity.class);
                intent.putExtra("id", status.getOutCompId());
                startActivity(intent);
                break;
            case R.id.tv_comp_yw_tel:
                openDial(this, tv_comp_yw_tel.getText().toString());
                break;
            case R.id.tv_comp_yw_name:
                intent = new Intent(this, LianXiRenDetailActivity.class);
                intent.putExtra("mobile", status.getMobileNum());
                intent.putExtra("type", "");
                startActivity(intent);
                break;
        }
    }


}

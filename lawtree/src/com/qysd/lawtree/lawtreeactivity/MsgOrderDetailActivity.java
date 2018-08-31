package com.qysd.lawtree.lawtreeactivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.MsgOrderDetailAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.MsgOrderDetailBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class MsgOrderDetailActivity extends BaseActivity {

    private LinearLayout ll_botom;
    private TextView tv_order_num,tv_name,tv_linkname,tv_phone,tv_startTime,tv_endTime,tv_state
            ,tv_accept,tv_refuse;
    private RecyclerView recyclerview;
    private MsgOrderDetailAdapter productLIstAdapter;
    private MsgOrderDetailBean productLIstBean;
    private LinearLayoutManager manager;
    private Gson gson = new Gson();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_msgorder_detail);
        initTitle(R.drawable.ic_left_jt,"订单详情");
    }

    @Override
    protected void initView() {
        ll_botom = findViewById(R.id.ll_botom);
        tv_order_num = findViewById(R.id.tv_order_num);
        tv_name = findViewById(R.id.tv_name);
        tv_linkname = findViewById(R.id.tv_linkname);
        tv_phone = findViewById(R.id.tv_phone);
        tv_startTime = findViewById(R.id.tv_startTime);
        tv_endTime = findViewById(R.id.tv_endTime);
        tv_state = findViewById(R.id.tv_state);
        tv_accept = findViewById(R.id.tv_accept);
        tv_refuse = findViewById(R.id.tv_refuse);
        recyclerview = findViewById(R.id.recyclerview);
    }

    @Override
    protected void bindListener() {
        tv_accept.setOnClickListener(this);
        tv_refuse.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        tv_order_num.setText(getIntent().getStringExtra("ordercodenick"));
//        tv_name.setText(getIntent().getStringExtra("compName"));
//        tv_linkname.setText(getIntent().getStringExtra("userName"));
//        tv_phone.setText(getIntent().getStringExtra("mobileNum"));
//        tv_startTime.setText(getIntent().getStringExtra("dorderdateStr"));
//        tv_endTime.setText(getIntent().getStringExtra("performdateStr"));
//        tv_state.setText(getIntent().getStringExtra("订单描述："+"ordermemo"));
        productList();
    }

    @Override
    protected void initNav() {

    }

    public void productList(){
        String str = getIntent().getStringExtra("sysMsgId");
        Log.e("songlonglong",str+"88888"+str.substring(str.indexOf("/")+1));
        OkHttpUtils.get()
                .url(Constants.baseUrl+"/purchaseOrder/purchaseOrderDetails")
                .addParams("orderCode",str.substring(str.indexOf("/")+1))
                .addParams("userId", GetUserInfo.getUserId(this))
                .addParams("compid",(String) GetUserInfo.getData(this,"compId",""))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        productLIstBean = gson.fromJson(response,MsgOrderDetailBean.class);
                        if ("1".equals(productLIstBean.getStatus().get(0).getOrderstatus())
                                && "2".equals(productLIstBean.getStatus().get(0).getModelFlag())){
                            ll_botom.setVisibility(View.VISIBLE);
                        }else {
                            ll_botom.setVisibility(View.GONE);
                        }
                        if ("1".equals(productLIstBean.getCode())){
                            if (productLIstBean.getStatus().size()>0){
                                tv_order_num.setText(productLIstBean.getStatus().get(0).getOrderCodeNick());
                                tv_name.setText(productLIstBean.getStatus().get(0).getCompName());
                                tv_linkname.setText(productLIstBean.getStatus().get(0).getUserName());
                                tv_phone.setText(productLIstBean.getStatus().get(0).getMobileNum());
                                tv_startTime.setText(productLIstBean.getStatus().get(0).getDorderdateStr());
                                tv_endTime.setText(productLIstBean.getStatus().get(0).getPerformdateStr());
                                tv_state.setText(productLIstBean.getStatus().get(0).getOrdermemo());
                                setAdapter(productLIstBean.getStatus().get(0).getOrderMaterielList());
                            }
                        }
                    }
                });
    }

    private void setAdapter(final List<MsgOrderDetailBean.MsgList.OrderMaterielList> list) {
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        productLIstAdapter = new MsgOrderDetailAdapter(list);
        recyclerview.setAdapter(productLIstAdapter);
        //条目点击接口
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_accept:
                orderHttp("6");
                break;
            case R.id.tv_refuse:
                orderHttp("2");
                break;
        }
    }

    /**
     * 接单退回
     */
    public void orderHttp(String orderStatus){
        OkHttpUtils.get()
                .url(Constants.baseUrl+"/appsalesorder/updateOrderStatus")
                .addParams("salesId",productLIstBean.getStatus().get(0).getSalesid())
                .addParams("orderStatus",orderStatus)//订单状态（接单：6 退回：2）
                .addParams("userId",GetUserInfo.getUserId(this))
                .addParams("compId",(String) GetUserInfo.getData(this,"compId",""))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        ll_botom.setVisibility(View.GONE);
                    }
                });
    }
}

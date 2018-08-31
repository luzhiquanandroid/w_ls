package com.qysd.lawtree.lawtreeactivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.ProductLIstAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.OrderDetailBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class OrderDetailActivity extends BaseActivity {

    private TextView tv_order_num,tv_name,tv_linkname,tv_phone,tv_startTime,tv_endTime,tv_state;
    private RecyclerView recyclerview;
    private ProductLIstAdapter productLIstAdapter;
    private OrderDetailBean productLIstBean;
    private LinearLayoutManager manager;
    private Gson gson = new Gson();

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_xiaodan_detail);
        initTitle(R.drawable.ic_left_jt,"订单详情");
    }

    @Override
    protected void initView() {
        tv_order_num = findViewById(R.id.tv_order_num);
        tv_name = findViewById(R.id.tv_name);
        tv_linkname = findViewById(R.id.tv_linkname);
        tv_phone = findViewById(R.id.tv_phone);
        tv_startTime = findViewById(R.id.tv_startTime);
        tv_endTime = findViewById(R.id.tv_endTime);
        tv_state = findViewById(R.id.tv_state);
        recyclerview = findViewById(R.id.recyclerview);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        tv_order_num.setText(getIntent().getStringExtra("ordercodenick"));
        tv_name.setText(getIntent().getStringExtra("compName"));
        tv_linkname.setText(getIntent().getStringExtra("userName"));
        tv_phone.setText(getIntent().getStringExtra("mobileNum"));
        tv_startTime.setText(getIntent().getStringExtra("dorderdateStr"));
        tv_endTime.setText(getIntent().getStringExtra("performdateStr"));
        tv_state.setText(getIntent().getStringExtra("订单描述："+"ordermemo"));
        productList();
    }

    @Override
    protected void initNav() {

    }

    public void productList(){
        OkHttpUtils.get()
                .url(Constants.baseUrl+"/purchaseOrder/purchaseOrderDetails")
                .addParams("orderCode",getIntent().getStringExtra("orderCode"))
                .addParams("userId", GetUserInfo.getUserId(this))
                .addParams("compid",getIntent().getStringExtra("compid"))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("songlonglong",response);
                        productLIstBean = gson.fromJson(response,OrderDetailBean.class);
                        if ("1".equals(productLIstBean.getCode())){
                            if (productLIstBean.getStatus().size()>0){
//                                if (productLIstAdapter == null){
//                                    setAdapter(productLIstBean.getStatus());
//                                }else {
//                                    productLIstAdapter.notifyDataSetChanged();
//                                }
                                setAdapter(productLIstBean.getStatus().get(0).getOrderMaterielList());
                            }
                        }
                    }
                });
    }

    private void setAdapter(final List<OrderDetailBean.Status.OrderMaterielList> list) {
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        productLIstAdapter = new ProductLIstAdapter(list);
        recyclerview.setAdapter(productLIstAdapter);
        //条目点击接口
    }

    @Override
    public void onClick(View v) {

    }
}

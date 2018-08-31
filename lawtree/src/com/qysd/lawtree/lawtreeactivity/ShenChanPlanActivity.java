package com.qysd.lawtree.lawtreeactivity;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.FgtPagAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebusbean.ShenChanPlanEventBusBean;
import com.qysd.lawtree.lawtreefragment.ShengChanPlan.CompletFragment;
import com.qysd.lawtree.lawtreefragment.ShengChanPlan.NoCompletFragment;
import com.qysd.lawtree.lawtreeutils.CommonPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 生产计划
 */
public class ShenChanPlanActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String[] orderType = {"待完成", "已完成"};
    private List<Fragment> scPlanFragments = new ArrayList<>();//fragment的集合
    private CommonPopupWindow window;
    private CommonPopupWindow.LayoutGravity layoutGravity;
    private TimePickerView pvData;
    private TextView tv_title_right, tv_query;//右边筛选  筛选确定
    private TextView tv_startTime, tv_endTime;//开始时间 结束时间
    private EditText et_shop_name, et_shop_order;//商品名称 订单编号

    private Date startDate, endDate;
    private int currentItem = 0;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_shen_chan_plan);
    }

    @Override
    protected void initView() {
        initTitle(R.drawable.ic_left_jt, "生产计划", "筛选");
        tv_title_right = findViewById(R.id.tv_title_right);
        mViewPager = findViewById(R.id.mViewPager);
        mTabLayout = findViewById(R.id.mTabLayout);

        window = new CommonPopupWindow(this, R.layout.pop_all_xs_layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                tv_endTime = view.findViewById(R.id.tv_endTime);
                tv_startTime = view.findViewById(R.id.tv_startTime);
                et_shop_name = view.findViewById(R.id.et_shop_name);
                et_shop_order = view.findViewById(R.id.et_shop_order);
                tv_query = view.findViewById(R.id.tv_query);
                tv_startTime.setText("2018-01-01");
                tv_endTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            }

            @Override
            protected void initEvent() {
                //开始时间
                tv_startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initTimePicker("startTime");
                        if (pvData != null) {
                            pvData.show(); //弹出时间选择器
                        }
                    }
                });
                //结束时间
                tv_endTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initTimePicker("endTime");
                        if (pvData != null) {
                            pvData.show(); //弹出时间选择器
                        }
                    }
                });
                //筛选确认
                tv_query.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new ShenChanPlanEventBusBean(
                                currentItem,
                                tv_startTime.getText().toString(),
                                tv_endTime.getText().toString(),
                                et_shop_name.getText().toString().trim(),
                                et_shop_order.getText().toString().trim()
                        ));
                        window.getPopupWindow().dismiss();
                        et_shop_name.setText("");
                        et_shop_order.setText("");
                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
        layoutGravity = new CommonPopupWindow.LayoutGravity(CommonPopupWindow.LayoutGravity.ALIGN_LEFT | CommonPopupWindow.LayoutGravity.TO_BOTTOM);
    }


    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        initFragment();
    }

    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        scPlanFragments.add(new NoCompletFragment());
        scPlanFragments.add(new CompletFragment());
        // ViewPager设置适配器
        FgtPagAdapter adapter =
                new FgtPagAdapter(getSupportFragmentManager(), Arrays.asList(orderType),
                        scPlanFragments);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(adapter);
        // 将ViewPager与TableLayout 绑定在一起
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(mTabLayout, 60, 60);
            }
        });
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    protected void onClickTitleRight(View v) {
        super.onClickTitleRight(v);
        currentItem = mViewPager.getCurrentItem();
        window.showBashOfAnchor(tv_title_right, layoutGravity, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    /**
     * 设置时间选择器
     */
    private void initTimePicker(final String type) {
        //控制时间范围,setRange方法 要在setDate 之前才有效果(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        /*Calendar calendar = Calendar.getInstance();*/

        //时间选择器
        pvData = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (type.equals("startTime")) {
                    startDate = date;
                    if (startDate != null & endDate != null) {
                        if (endDate.getTime() < startDate.getTime()) {
                            Toast.makeText(ShenChanPlanActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    tv_startTime.setText(getTime(date));
                } else if (type.equals("endTime")) {
                    endDate = date;
                    if (startDate != null & endDate != null) {
                        if (endDate.getTime() < startDate.getTime()) {
                            Toast.makeText(ShenChanPlanActivity.this, "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    tv_endTime.setText(getTime(date));
                }
            }
        })
//                //.setType(TimePickerView.Type.ALL)//default is all
//                .setCancelText("Cancel")
//                .setSubmitText("Sure")
//                .setContentSize(14)
//                .setTitleSize(14)
//                .setTitleText("Title")
//                .setOutSideCancelable(false)// default is true
//                .isCyclic(true)// default is false
//                .setTitleColor(Color.BLACK)
//                .setDate(new Date())// default system
//                .setDividerColor(Color.WHITE)//设置分割线的颜色
//                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
//                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数,只能在1.2-2.0f之间
//                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
//                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
//                .setSubmitColor(Color.WHITE)
//                .setCancelColor(Color.WHITE)
//                .gravity(Gravity.BOTTOM)// default is center
//                .setDividerType(WheelView.DividerType.WARP)
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)//default is all
//                //.setContentSize(14)
//                //.setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setRangDate(new Date(2018, 3, 3, 0, 0, 0),
                        new Date(Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH) + 1,
                                Calendar.getInstance().get(Calendar.DATE),
                                Calendar.getInstance().get(Calendar.HOUR),
                                Calendar.getInstance().get(Calendar.MINUTE),
                                Calendar.getInstance().get(Calendar.SECOND)))
                .isDialog(true)
                .build();
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}

package com.qysd.lawtree.lawtreeactivity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.login.LoginActivity;


/**
 * 引导页
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private Intent intent;
    /**
     * ViewPager
     */
    private ViewPager viewPager;
    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private int[] imgIdArray;
    private boolean misScrolled;
    private ImageView mGomain;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void initView() {
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mGomain = (ImageView) findViewById(R.id.go_main);
        mGomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 进入主页按钮  存入缓存
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                GetUserInfo.putData(GuideActivity.this, "isFirstEntery", false);
                finish();
            }
        });

        //载入图片资源ID
        imgIdArray = new int[]{R.drawable.welcome_01, R.drawable.welcome_02, R.drawable.welcome_03};
        //将点点加入到ViewGroup中
        tips = new ImageView[imgIdArray.length];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(30, 30));
            tips[i] = imageView;
            if (i == 0) {
                //tips[i].setBackgroundResource(R.drawable.shape_guide_cir_ok);
            } else {
                //tips[i].setBackgroundResource(R.drawable.shape_guide_cir_no);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }


        //将图片装载到数组中
        mImageViews = new ImageView[imgIdArray.length];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(this);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }

        //设置Adapter
        viewPager.setAdapter(new MyAdapter());
        //设置监听，主要是设置点点的背景
        viewPager.setOnPageChangeListener(this);
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        switch (arg0) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                misScrolled = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                misScrolled = true;
                break;
            case ViewPager.SCROLL_STATE_IDLE:
//                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !misScrolled) {
//                    UIHelper.openActivity(WelcomeActivity.this, MainActivity.class);
//                    finish();
//                    SharedPreferenceUtils.saveBooleanDate(WelcomeActivity.this, Constants.ISFIRSTSPLASH, true);
//                }
                misScrolled = true;
                break;
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
        if (arg0 == viewPager.getAdapter().getCount() - 1) {
            mGomain.setVisibility(View.VISIBLE);
        } else {
            mGomain.setVisibility(View.GONE);
        }
    }

    /**
     * 设置选中的tip的背景arg0
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                //tips[i].setBackgroundResource(R.drawable.shape_guide_cir_ok);
            } else {
                //tips[i].setBackgroundResource(R.drawable.shape_guide_cir_no);
            }
        }
    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgIdArray.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
            return mImageViews[position % mImageViews.length];
        }


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
    public void onClick(View view) {

    }
}

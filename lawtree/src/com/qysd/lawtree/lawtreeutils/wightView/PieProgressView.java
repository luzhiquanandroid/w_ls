package com.qysd.lawtree.lawtreeutils.wightView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.qysd.lawtree.R;

public class PieProgressView extends View {

    /**
     * 颜色
     */
    private int mBackColor;
    private int mFrontColor;
    /**
     * 直径
     */
    private int mDiameter;

    /**
     * 绘制时控制绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;
    private int progressValue = 0;

    public PieProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieProgressView(Context context) {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    @SuppressLint("ResourceAsColor")
    public PieProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PieProgressView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.PieProgressView_backColor:
                    // 默认背景颜色设置为黑色
                    mBackColor = a.getColor(attr, Color.BLACK);
                    Log.i("log", "mBackColor=" + Integer.toHexString(mBackColor));
                    break;
                case R.styleable.PieProgressView_frontColor:
                    // 默认前景颜色设置为蓝色
                    mFrontColor = a.getColor(attr, Color.BLUE);
                    Log.i("log", "mFrontColor=" + Integer.toHexString(mFrontColor));
                    break;
                case R.styleable.PieProgressView_diameter:
                    // 默认设置为40dp
                    mDiameter = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 40, getResources().getDisplayMetrics()));
                    break;
            }

        }
        a.recycle();
        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mBound = new Rect();
        progressValue = 0;

    }

    public void setInputData(int inputData) {
        progressValue = inputData;
//		Log.i("log","progressValue="+progressValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                width = getPaddingLeft() + getPaddingRight() + mBound.width();
                break;
        }

        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
                height = getPaddingTop() + getPaddingBottom() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                height = getPaddingTop() + getPaddingBottom() + mBound.height();
                break;
        }

        //设置直径的最小值
        if (mDiameter <= 40) {
            mDiameter = 40;
        }
        height = mDiameter;
        width = mDiameter;

        Log.i("log", "w=" + width + " h=" + height);
        setMeasuredDimension(width, height);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.RED);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        RectF rect = new RectF(1, 1, width - 1, height - 1);//200, 200);
//		Log.i("log","w="+width+" h="+height);

        mPaint.setColor(mBackColor);
        canvas.drawArc(rect, 0, 360, true, mPaint);

        mPaint.setColor(mFrontColor);
        canvas.drawArc(rect, 0, progressValue * 360 / 100, true, mPaint);
    }

}

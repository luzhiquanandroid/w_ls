package com.qysd.lawtree.lawtreeutils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qysd.lawtree.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 说明: Glide工具类（glide 4.1+）
 * 作者：mint.zhang
 * 时间：2017/9/21
 * 邮箱：zyminsunshine@126.com
 */

public class GlideUtils {


    /*
     *加载图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.white) //占位图
                .error(R.color.white)       //错误图
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }


    /*
     *加载圆形图片
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(R.color.white)
                .error(R.color.white)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /*
     *加载圆角图片
     */
    public static void loadRoundCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(R.color.white)
                .error(R.color.white)
                //.priority(Priority.HIGH)
                .bitmapTransform(new RoundedCornersTransformation(45, 0, RoundedCornersTransformation.CornerType.ALL))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    /*
     *加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     */
    public static void loadCustRoundCircleImage(Context context, String url, ImageView imageView, RoundedCornersTransformation.CornerType type) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.white)
                .error(R.color.white)
                //.priority(Priority.HIGH)
                .bitmapTransform(new RoundedCornersTransformation(45, 0, type))
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /*
     *加载模糊图片（自定义透明度）
     */
    public static void loadBlurImage(Context context, String url, ImageView imageView, int blur) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.white)
                .error(R.color.white)
                //.priority(Priority.HIGH)
                .bitmapTransform(new BlurTransformation(blur))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /*
     *加载灰度(黑白)图片（自定义透明度）
     */
    public static void loadBlackImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.white)
                .error(R.color.white)
                //.priority(Priority.HIGH)
                .bitmapTransform(new GrayscaleTransformation())
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
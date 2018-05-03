package com.wuyou.worker.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.wuyou.worker.R;

/**
 * Created by hjn on 2016/11/1.
 */
public class GlideUtils {
    public static void loadImage(Context context, String url, final ImageView imageView) {
        if (url == null) return;
        Glide.with(context).load(url).placeholder(R.mipmap.default_pic).into(imageView);
    }

    public static void loadImageNoHolder(Context context, String url, final ImageView imageView) {
        if (url == null) return;
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, boolean isCircle) {
        if (url == null) return;
        Glide.with(context).load(url).placeholder(R.mipmap.default_pic).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
    }


    public interface OnLoadListener {
        void onLoaded();
    }

    public static void loadImageWithListener(Context context, String url, ImageView imageView, boolean isCircle, final OnLoadListener onLoadListener) {
        if (url == null) return;
        Glide.with(context).load(url).placeholder(R.mipmap.default_pic).bitmapTransform(new CropCircleTransformation(context)).into(new GlideDrawableImageViewTarget(imageView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                onLoadListener.onLoaded();
            }
        });
    }

    public static void loadImage(Context context, String url, ImageView imageView, int width, int height) {
        if (url == null) return;
        Glide.with(context).load(url).placeholder(R.mipmap.default_pic).override(width, height).into(imageView);
    }
}

package com.wuyou.worker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.gs.buluo.common.utils.DensityUtils;
import com.wuyou.worker.R;

import java.security.MessageDigest;

import static com.bumptech.glide.load.Key.CHARSET;

/**
 * Created by hjn on 2016/11/1.
 */
public class GlideUtils {
    public static void loadImage(Context context, String url, final ImageView imageView) {
        if (url == null) return;
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.default_pic);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadImageNoHolder(Context context, String url, final ImageView imageView) {
        if (url == null) return;
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadImageNoHolder(Context context, String url, final ImageView imageView, boolean isCircle) {
        if (url == null) return;
        if (isCircle) {
            RequestOptions options = new RequestOptions();
            options.apply(RequestOptions.circleCropTransform());
            Glide.with(context).load(url).apply(options).into(imageView);
        } else {
            loadImage(context, url, imageView);
        }
    }

    public static void loadImage(Context context, String url, ImageView imageView, boolean isCircle) {
        if (url == null) return;
        if (isCircle) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.default_pic).apply(RequestOptions.circleCropTransform());
            Glide.with(context).load(url).apply(options).into(imageView);
        } else {
            loadImage(context, url, imageView);
        }
    }


    public interface OnLoadListener {
        void onLoaded();
    }

    public static void loadImage(Context context, String url, ImageView imageView, int width, int height) {
        if (url == null) return;
        Glide.with(context).load(url).apply(RequestOptions.placeholderOf(R.mipmap.default_pic).override(width, height)).into(imageView);
    }


    public static byte[] loadRoundCornerImage(Context context, String url, ImageView imageView, int dp) {
        if (url == null) return null;
        RequestOptions options = new RequestOptions();
        GlideRoundTransform transformation = new GlideRoundTransform(context, dp, GlideRoundTransform.CornerType.ALL);
        options.optionalTransform(transformation);
        Glide.with(context).load(url).apply(options).into(imageView);
        return transformation.getBitmap();
    }

    public static void loadRoundCornerImage(Context context, String url, ImageView imageView) {
        if (url == null) return;
        RequestOptions options = new RequestOptions();
        options.optionalTransform(new GlideRoundTransform(context, DensityUtils.dip2px(context, 4), GlideRoundTransform.CornerType.ALL));
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static byte[] loadRoundCornerImageWithBitmap(Context context, String url, ImageView imageView) {
        if (url == null) return null;
        RequestOptions options = new RequestOptions();
        GlideRoundTransform transformation = new GlideRoundTransform(context, DensityUtils.dip2px(context, 4), GlideRoundTransform.CornerType.ALL);
        options.optionalTransform(transformation);
        Glide.with(context).load(url).apply(options).into(imageView);
        return transformation.getBitmap();
    }

    private static final int VERSION = 1;
    private static final String ID = "GlideRoundedCornersTransform." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    public static void loadWithBitmap(Context context, String url, ImageView imageView) {
        if (url == null) return;
        RequestOptions options = new RequestOptions();
        options.optionalTransform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap source, int outWidth, int outHeight) {
                if (source == null) {
                    return null;
                }
                int width = source.getWidth();
                int height = source.getHeight();
                Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);


                if (result == null) {
                    result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config
                            .ARGB_8888);
                }
                return result;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                messageDigest.update(ID_BYTES);
            }
        });
        Glide.with(context).load(url).apply(options).into(imageView);
    }
}

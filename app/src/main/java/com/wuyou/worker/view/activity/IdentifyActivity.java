package com.wuyou.worker.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.CustomGsonFactory;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.FaceResult;
import com.wuyou.worker.network.apis.FaceApis;
import com.wuyou.worker.util.CommonUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by DELL on 2018/4/3.
 */

public class IdentifyActivity extends BaseActivity {
    @BindView(R.id.iv_card_add)
    ImageView ivCardAdd;
    @BindView(R.id.tv_card_add_tip)
    TextView tvCardAddTip;
    @BindView(R.id.iv_card_face)
    ImageView ivCardFace;
    @BindView(R.id.tv_card_face_tip)
    TextView tvCardFaceTip;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_identify;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }


    @OnClick({R.id.upload_identify, R.id.iv_card_add, R.id.iv_card_face})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upload_identify:
                if (idPath == null) {
                    ToastUtils.ToastMessage(getCtx(), "请选择身份证照片");
                    return;
                }
                if (facePath == null) {
                    ToastUtils.ToastMessage(getCtx(), "请选择自拍照片");
                    return;
                }
                doCompareAndUpload();
                break;
            case R.id.iv_card_add:
                Matisse.from(this)
                        .choose(MimeType.ofImage())
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .captureStrategy(new CaptureStrategy(true, "com.wuyou.worker.FileProvider"))
                        .gridExpectedSize(DensityUtils.dip2px(this, 120))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(201);
                break;
            case R.id.iv_card_face:
                Matisse.from(this)
                        .choose(MimeType.ofImage())
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .captureStrategy(new CaptureStrategy(true, "com.wuyou.worker.FileProvider"))
                        .gridExpectedSize(DensityUtils.dip2px(this, 120))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(202);
                break;
        }
    }

    private void doCompareAndUpload() {
        showLoadingDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-cn.faceplusplus.com/")
                .addConverterFactory(CustomGsonFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        File idFile = new File(idPath);
        File faceFile = new File(facePath);


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), idFile);
        MultipartBody.Part body1 = MultipartBody.Part.createFormData("image_file1", "idFile", requestFile);

        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), faceFile);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("image_file2", "faceFile", requestFile2);
        retrofit.create(FaceApis.class).doCompare(Constant.FACE_KEY, Constant.FACE_SECRET, body1, body2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<FaceResult>() {
                    @Override
                    public void onSuccess(FaceResult faceResult) {
                        Log.e("Test", "onSuccess: " + faceResult);
                        if (faceResult.confidence > 30) {
                            compareSuccess();
                        } else {
                            ToastUtils.ToastMessage(getCtx(), "检测人脸不匹配，请更换清晰照片重试");
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    }
                });
    }

    private void compareSuccess() {
        ToastUtils.ToastMessage(getCtx(), "认证成功");
        finish();
    }

    String idPath;
    String facePath;
    long MAX_BYTES = 2 * 1024 * 1024;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK) {
            List<String> pathResult = Matisse.obtainPathResult(data);
            if (pathResult != null && pathResult.size() > 0) {
                idPath = pathResult.get(0);
                compressFile(idPath);
                Glide.with(this)
                        .load(Uri.parse("file://" + idPath))
                        .priority(Priority.HIGH)
                        .into(ivCardAdd);
            }
        } else if (requestCode == 202 && resultCode == RESULT_OK) {
            List<String> pathResult = Matisse.obtainPathResult(data);
            if (pathResult != null && pathResult.size() > 0) {
                facePath = pathResult.get(0);
                compressFile(facePath);
                Glide.with(this)
                        .load(Uri.parse("file://" + facePath))
                        .priority(Priority.HIGH)
                        .into(ivCardFace);
            }
        }
    }

    private void compressFile(String path) {
        try {
            showLoadingDialog();
            Bitmap bitmap = CommonUtil.compressByQuality(new File(path), MAX_BYTES, true);
            CommonUtil.save(bitmap, new File(path), Bitmap.CompressFormat.JPEG, true);
            dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

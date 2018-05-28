package com.wuyou.worker.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.CustomGsonFactory;
import com.gs.buluo.common.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.FaceResult;
import com.wuyou.worker.network.apis.FaceApis;
import com.wuyou.worker.util.GlideUtils;

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
    private boolean isFront;

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
                choosePhoto(201);
                break;
            case R.id.iv_card_face:
                choosePhoto(202);
                break;
        }
    }

    public void choosePhoto(int requestCode) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .compressSavePath(CarefreeApplication.getInstance().getApplicationContext().getFilesDir().getAbsolutePath())//压缩图片保存地址
                .setOutputCameraPath(CarefreeApplication.getInstance().getApplicationContext().getFilesDir().getAbsolutePath())
                .maxSelectNum(1)// 最大图片选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .withAspectRatio(16, 9)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(requestCode);//结果回调onActivityResult code
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
                        Log.e("FaceResult", "onSuccess: " + faceResult);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                LocalMedia localMedia = selectList.get(0);
                if (localMedia.isCompressed()) {
                    idPath = localMedia.getCompressPath();
                } else if (localMedia.isCut()) {
                    idPath = localMedia.getCutPath();
                } else {
                    idPath = localMedia.getPath();
                }
            }
            GlideUtils.loadImage(getCtx(), idPath, ivCardAdd);
        } else if (requestCode == 202 && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                LocalMedia localMedia = selectList.get(0);
                if (localMedia.isCompressed()) {
                    facePath = localMedia.getCompressPath();
                } else if (localMedia.isCut()) {
                    facePath = localMedia.getCutPath();
                } else {
                    facePath = localMedia.getPath();
                }
            }
            GlideUtils.loadImage(getCtx(), facePath, ivCardFace);
        }
    }
}

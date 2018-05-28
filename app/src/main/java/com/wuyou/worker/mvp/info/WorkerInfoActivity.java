package com.wuyou.worker.mvp.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.UserInfo;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.UserApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.GlideUtils;
import com.wuyou.worker.util.ImageUtil;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.BaseActivity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by DELL on 2018/3/26.
 */

public class WorkerInfoActivity extends BaseActivity {
    @BindView(R.id.info_nickname)
    TextView infoNickname;
    @BindView(R.id.info_mobile)
    TextView infoMobile;
    @BindView(R.id.info_email)
    TextView infoEmail;
    @BindView(R.id.info_sex)
    TextView infoSex;
    @BindView(R.id.info_avatar)
    ImageView infoAvatar;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_worker_info;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
        GlideUtils.loadImage(this, CarefreeDaoSession.getAvatar(userInfo), infoAvatar, true);
        infoMobile.setText(CommonUtil.getPhoneWithStar(userInfo.getMobile()));
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .getUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        setUserData(userInfoBaseResponse.data);
                    }
                });
    }

    @OnClick({R.id.info_nickname_area, R.id.info_mobile_area, R.id.info_email_area, R.id.info_sex_area, R.id.info_head_area})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.info_head_area:
                CommonUtil.choosePhoto(this, PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.info_nickname_area:
                intent.setClass(getCtx(), ModifyNickActivity.class);
                startActivityForResult(intent.putExtra(Constant.FROM, Constant.NICK), Constant.REQUEST_NICK);
                break;
            case R.id.info_mobile_area:
                intent.setClass(getCtx(), ModifyPhoneActivity.class);
                startActivityForResult(intent.putExtra(Constant.FROM, Constant.PHONE), Constant.REQUEST_PHONE);
                break;
            case R.id.info_email_area:
                intent.setClass(getCtx(), ModifyNickActivity.class);
                startActivityForResult(intent.putExtra(Constant.FROM, Constant.EMAIL), Constant.REQUEST_EMAIL);
                break;
            case R.id.info_sex_area:
                intent.setClass(getCtx(), ModifyGenderActivity.class);
                intent.putExtra(Constant.GENDER, gender);
                startActivityForResult(intent, Constant.REQUEST_GENDER);
                break;
        }
    }

    private void chooseBirthday() {
        Calendar calendar = Calendar.getInstance();
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setRangeStart(1940, 1, 1);
        picker.setSelectedItem(1980, 1, 1);
        calendar.setTime(new Date(System.currentTimeMillis() + 24 * 3600 * 1000));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        picker.setRangeEnd(year, month, day);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year1, month1, day1) -> updateBirthday(year1 + "-" + month1 + "-" + day1));
        picker.show();
    }

    private void updateBirthday(String birth) {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .updateUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns()
                        .put("field", "birthday")
                        .put("value", birth)
                        .buildPost())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), R.string.update_success);
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                String path = "";
                if (selectList != null && selectList.size() > 0) {
                    LocalMedia localMedia = selectList.get(0);
                    if (localMedia.isCompressed()) {
                        path = localMedia.getCompressPath();
                    } else if (localMedia.isCut()) {
                        path = localMedia.getCutPath();
                    } else {
                        path = localMedia.getPath();
                    }
                }
                GlideUtils.loadImage(getCtx(), path, infoAvatar, true);
                uploadAvatar(path);
            } else if (requestCode == Constant.REQUEST_NICK) {
                infoNickname.setText(data.getStringExtra("info"));
            } else if (requestCode == Constant.REQUEST_PHONE) {
                infoMobile.setText(CommonUtil.getPhoneWithStar(data.getStringExtra("info")));
            } else if (requestCode == Constant.REQUEST_EMAIL) {
                infoEmail.setText(data.getStringExtra("info"));
            } else if (requestCode == Constant.REQUEST_GENDER) {
                gender = data.getIntExtra("info", 0);
                infoSex.setText(getGenderString(gender));
            }
        }
    }

    private String getGenderString(int gender) {
        if (gender == 0) return "男";
        else if (gender == 1) return "女";
        else return "保密";
    }

    private int gender = 0;

    private static final long MAX_NUM_PIXELS_THUMBNAIL = 64 * 64;

    private void uploadAvatar(final String path) {
        Observable.just(path)
                .flatMap(imagePath -> {
                    Bitmap bitmap = ImageUtil.getBitmap(new File(imagePath));
                    Bitmap compressBitmap = ImageUtil.compressByQuality(bitmap, MAX_NUM_PIXELS_THUMBNAIL);
                    ImageUtil.save(compressBitmap, imagePath, Bitmap.CompressFormat.JPEG);
                    File file = new File(imagePath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                    return CarefreeRetrofit.getInstance().createApi(UserApis.class).updateAvatar(CarefreeDaoSession.getInstance().getUserId(), body, QueryMapBuilder.getIns().buildPost());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        CarefreeDaoSession.tempAvatar = null;
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    }
                });
    }

    public void setUserData(UserInfo userInfo) {
        if (userInfo.getName() != null) infoNickname.setText(userInfo.getWorker_name());
        if (userInfo.getGender() != null) {
            gender = Integer.parseInt(userInfo.getGender());
            infoSex.setText(getGenderString(gender));
        }
        if (userInfo.getAvatar() != null)
            GlideUtils.loadImage(this, CarefreeDaoSession.getAvatar(userInfo), infoAvatar, true);
    }
}

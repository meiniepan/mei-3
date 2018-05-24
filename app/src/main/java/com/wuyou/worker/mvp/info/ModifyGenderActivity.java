package com.wuyou.worker.mvp.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.UserApis;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/3/14.
 */

public class ModifyGenderActivity extends BaseActivity {
    @BindView(R.id.cb_1)
    CheckBox cbMale;
    @BindView(R.id.cb_2)
    CheckBox cbFemale;
    @BindView(R.id.cb_3)
    CheckBox cbSecret;
    private int sGender;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        sGender = getIntent().getIntExtra(Constant.GENDER, 0);
        if (sGender == 0) {
            cbMale.setChecked(true);
        } else if (sGender == 1) {
            cbFemale.setChecked(true);
        } else {
            cbSecret.setChecked(true);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_gender;
    }


    @OnClick({R.id.btn_modify_gender, R.id.cb_1, R.id.cb_2, R.id.cb_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_modify_gender:
                updateGender();
                break;
            case R.id.cb_1:
                initCheck();
                cbMale.setChecked(true);
                sGender = 0;
                break;
            case R.id.cb_2:
                initCheck();
                cbFemale.setChecked(true);
                sGender = 1;
                break;
            case R.id.cb_3:
                initCheck();
                cbSecret.setChecked(true);
                sGender = 2;
                break;
        }
    }

    private void updateGender() {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .updateUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns()
                        .put("field", "gender")
                        .put("value", sGender + "").buildPost())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        setResult(RESULT_OK, new Intent().putExtra("info", sGender));
                        finish();
                    }
                });
    }

    private void initCheck() {
        cbMale.setChecked(false);
        cbFemale.setChecked(false);
        cbSecret.setChecked(false);
    }
}

package com.wuyou.worker.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.info_birthday)
    TextView infoBirthday;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_worker_info;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
    }

    @OnClick({R.id.info_nickname_area, R.id.info_mobile_area, R.id.info_email_area, R.id.info_sex_area, R.id.info_birthday_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.info_nickname_area:
                break;
            case R.id.info_mobile_area:
                break;
            case R.id.info_email_area:
                break;
            case R.id.info_sex_area:
                break;
            case R.id.info_birthday_area:
                break;
        }
    }
}

package com.wuyou.worker.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.DataCleanManager;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.mvp.login.LoginActivity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.UserApis;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/7.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.mine_switch)
    Switch mSwitch;
    @BindView(R.id.setting_cache_size)
    TextView tvCache;
    private CustomAlertDialog customAlertDialog;
    private static final String TAG = "SettingActivity";

    @Override
    protected void bindView(Bundle savedInstanceState) {
//        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        findViewById(R.id.exit).setOnClickListener(this);
        findViewById(R.id.setting_clear_cache).setOnClickListener(this);
        findViewById(R.id.setting_feedback).setOnClickListener(this);
        findViewById(R.id.setting_update).setOnClickListener(this);
        findViewById(R.id.setting_about_us).setOnClickListener(this);

        String cacheSize = null;
        try {
            cacheSize = DataCleanManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cacheSize != null)
            tvCache.setText(cacheSize);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.setting_feedback:
                intent.setClass(getCtx(), FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_about_us:
                intent.setClass(getCtx(), WebActivity.class);
                intent.putExtra(Constant.WEB_URL, "http://39.105.52.20:8086/apphtml/about-us.html");
                startActivity(intent);
                break;
            case R.id.setting_clear_cache:
                new CustomAlertDialog.Builder(this).setTitle(R.string.prompt).setMessage("确定清除所有缓存?").setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        tvCache.setText("0K");
                    }
                }).setNegativeButton(getCtx().getString(R.string.cancel), null).create().show();
                break;
            case R.id.setting_update:
//                checkUpdate();
                break;
            case R.id.exit:
                customAlertDialog = new CustomAlertDialog.Builder(this).setTitle(R.string.prompt).setMessage("您确定要退出登录吗?")
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> logout())
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> customAlertDialog.dismiss()).create();
                customAlertDialog.show();
                break;
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onBuglyUpgradeDialogShow(DialogShowEvent event) {
//        CommonUtils.backgroundAlpha(this, 0.7f);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onBuglyUpgradeDialogDismiss(DialogDismissEvent event) {
//        CommonUtils.backgroundAlpha(this, 1f);
//    }

    private void logout() {
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .doLogout(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                    }

                    @Override
                    protected void onFail(ApiException e) {
                    }
                });
        CarefreeDaoSession.getInstance().clearUserInfo();
        Intent intent = new Intent(getCtx(), LoginActivity.class);
        startActivity(intent);
        finish();
        AppManager.getAppManager().finishActivity(MainActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

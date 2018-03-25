package com.wuyou.worker.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.common.utils.DataCleanManager;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

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
//                intent.setClass(mCtx, FeedbackActivity.class);
//                startActivity(intent);
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
        CarefreeDaoSession.getInstance().clearUserInfo();
//        EventBus.getDefault().post(new LoginEvent());
        Intent intent = new Intent(getCtx(), MainActivity.class);
        startActivity(intent);
        finish();
//        CarefreeRetrofit.getInstance().createApi(UserApis.class).
//                doLogout(CarefreeDaoSession.getInstance().getUserId(), com.wuyou.user.network.QueryMapBuilder.getIns().buildPost())
//                .subscribeOn(Schedulers.io())
//                .flatMap(new Function<BaseResponse, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(BaseResponse baseResponse) throws Exception {
//                        CarefreeApplication.getInstance().getUserInfoDao().deleteAll();
//                        CarefreeApplication.getInstance().setUserInfo(null);
//                        return (ObservableSource) observer -> {};
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new com.wuyou.user.network.BaseSubscriber() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        Intent intent = new Intent();
//                        intent.setClass(getCtx(), LoginActivity.class);
//                        startActivity(intent);
////        AppManager.getAppManager().finishActivity(MainActivity.class);
//                        finish();
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

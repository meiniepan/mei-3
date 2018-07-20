package com.wuyou.worker.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.SystemBarTintManager;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.R;
import com.wuyou.worker.mvp.BasePresenter;
import com.wuyou.worker.mvp.IBaseView;
import com.wuyou.worker.mvp.login.LoginActivity;

import butterknife.ButterKnife;


/**
 * Created by admin on 2016/11/1.
 */
public abstract class BaseActivity<V extends IBaseView, P extends BasePresenter<V>> extends AppCompatActivity implements IBaseView {
    View mRoot;
    protected P mPresenter;
    protected Toolbar mToolbar;
    private int color = R.color.main_blue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }
        super.onCreate(savedInstanceState);
        init();
        AppManager.getAppManager().addActivity(this);
        setExplode();//new Slide()  new Fade()
        initContentView(R.layout.layout_base_activity);

        mToolbar = findViewById(getToolBarId());
        setSupportActionBar(mToolbar);
        bindView(savedInstanceState);
        initSystemBar(this);
    }

    private void initContentView(int layout_base_activity) {
        setContentView(layout_base_activity);
        findViewById(R.id.back_base).setOnClickListener(v -> onBackPressed());

        createView();

    }

    protected void setTitleVisiable(int type) {
        findViewById(R.id.id_title).setVisibility(type);
    }

    protected void setTitleText(String title) {
        findViewById(R.id.id_title).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }
    protected void setTitleIcon(int resId, View.OnClickListener listener) {
        findViewById(R.id.iv_title_icon).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_title_icon).setBackgroundResource(resId);
        findViewById(R.id.iv_title_icon).setOnClickListener(listener);
    }

    protected void setTitleText(int titleId) {
        findViewById(R.id.id_title).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_title)).setText(titleId);
    }

    protected void setBackVisiable(int type) {
        findViewById(R.id.back).setVisibility(type);
    }

    public StatusLayout baseStatusLayout;

    protected void showErrMessage(String message) {
        baseStatusLayout.showErrorView(message);
    }

    protected void showErrMessage(int msgId) {
        baseStatusLayout.showErrorView(getString(msgId));
    }

    protected void showErrMessage() {
        baseStatusLayout.showErrorView(null);
    }

    private void createView() {
        baseStatusLayout = findViewById(R.id.id_status);
        ViewStub viewStub = findViewById(R.id.id_stub);
        viewStub.setLayoutResource(getContentLayout());
        mRoot = viewStub.inflate();
        View back = mRoot.findViewById(R.id.back);
        if (back != null)
            back.setOnClickListener(v -> onBackPressed());
        ButterKnife.bind(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setExplode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void init() {

    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    public View getRootView() {
        return mRoot;
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorInt
     */
    public void setBarColor(int colorInt) {
        color = colorInt;
        initSystemBar(this);
    }

    public void setBarTextColorDark() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void setBarTextColorLight() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
        setBarTextColorDark();
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void showLoadingDialog() {
        LoadingDialog.getInstance().show(mRoot.getContext(), R.string.loading, true);
    }

    protected void showLoadingDialog(boolean cancel) {
        LoadingDialog.getInstance().show(mRoot.getContext(), R.string.loading, cancel);
    }

    protected void dismissDialog() {
        LoadingDialog.getInstance().dismissDialog();
    }

    protected abstract int getContentLayout();

    protected abstract void bindView(Bundle savedInstanceState);


    protected boolean checkUser(Context context) {
        if (CarefreeApplication.getInstance().getUserInfo() == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    protected P getPresenter() {
        return null;
    }

//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onUpdate(UpdateEvent event) {
//        UpdatePanel updatePanel = new UpdatePanel(AppManager.getAppManager().currentActivity(), event);
//        updatePanel.setCancelable(event.supported);
//        updatePanel.show();
//        Beta.checkUpgrade(false, false);
//    }


    protected Context getCtx() {
        return this;
    }

    @Override
    public void showError(String message, int res) {
        showErrMessage(message);
    }

    public int getToolBarId() {
        return 0;
    }
    protected void disableFitSystemWindow() {
        findViewById(R.id.base_root).setFitsSystemWindows(false);
    }
}



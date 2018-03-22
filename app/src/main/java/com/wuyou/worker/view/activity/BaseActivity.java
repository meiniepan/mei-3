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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.SystemBarTintManager;
import com.gs.buluo.common.widget.LoadingDialog;
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
    private int color = R.color.night_blue;

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
        mRoot = createView();
        setContentView(mRoot);
        View view = mRoot.findViewById(R.id.back);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        mToolbar = findViewById(getToolBarId());
        setSupportActionBar(mToolbar);
        bindView(savedInstanceState);
        initSystemBar(this);
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

    private View createView() {
        View view = LayoutInflater.from(this).inflate(getContentLayout(), null);
        ButterKnife.bind(this, view);
        return view;
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

    }

    public int getToolBarId() {
        return 0;
    }
}



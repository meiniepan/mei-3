package com.wuyou.worker.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.gs.buluo.common.utils.DensityUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.MainPagerAdapter;
import com.wuyou.worker.mvp.circle.CircleFragment;
import com.wuyou.worker.mvp.message.MessageFragment;
import com.wuyou.worker.mvp.order.MyOrderFragment;
import com.wuyou.worker.mvp.order.OrderFragment;
import com.wuyou.worker.mvp.store.StoreFragment;
import com.wuyou.worker.mvp.wallet.WalletFragment;
import com.wuyou.worker.view.fragment.BaseFragment;
import com.wuyou.worker.view.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_tab)
    BottomNavigationViewEx bottomView;
    @BindView(R.id.main_pager)
    NoScrollViewPager viewPager;
    List<BaseFragment> fragments = new ArrayList<>();
    MyOrderFragment orderFragment = new MyOrderFragment();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

        fragments.add(orderFragment);
        fragments.add(new WalletFragment());
        fragments.add(new StoreFragment());
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        bottomView.setupWithViewPager(viewPager, false);
        bottomView.enableAnimation(false);
        bottomView.setIconVisibility(true);
        bottomView.enableShiftingMode(false);
        bottomView.enableItemShiftingMode(false);
        bottomView.setTextTintList(0, getResources().getColorStateList(R.color.main_blue));
        bottomView.setIconSize(DensityUtils.dip2px(getCtx(), 20), DensityUtils.dip2px(getCtx(), 20));
        bottomView.setIconsMarginTop(DensityUtils.dip2px(getCtx(), -8));
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int pos = bottomView.getMenuItemPosition(item);
                bottomView.setItemTextColor(getResources().getColorStateList(R.color.common_dark));
                bottomView.setTextTintList(pos, getResources().getColorStateList(R.color.main_blue));
                return true;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        setIntent(intent);
//        orderFragment.loadDatas();
    }
}

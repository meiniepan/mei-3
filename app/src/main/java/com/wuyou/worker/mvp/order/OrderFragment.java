package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.wuyou.worker.R;
import com.wuyou.worker.view.fragment.BaseFragment;
import com.wuyou.worker.view.widget.NoScrollViewPager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by solang on 2018/2/5.
 */

public class OrderFragment extends BaseFragment {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.vp_pager)
    NoScrollViewPager vpPager;
    MyOrderFragment myOrderFragment = new MyOrderFragment();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_order;
    }

    @Override
    public void showError(String message, int res) {
    }


    @Override
    protected void bindView(Bundle savedInstanceState) {
        initViewPager();

    }

    private void initViewPager() {
        vpPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            //此方法用来显示tab上的名字

            @Override
            public Fragment getItem(int position) {
                //创建Fragment并返回
                Fragment fragment = null;
                if (position == 0)
                    fragment = myOrderFragment;
                else if (position == 1)
                    fragment = new AllianceOrderFragment();
                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }


    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                clickLeft();
                break;
            case R.id.tv_right:
                clickRight();
                break;
        }
    }

    private void clickRight() {
        tvLeft.setTextColor(getActivity().getResources().getColor(R.color.main_blue));
        tvRight.setTextColor(getActivity().getResources().getColor(R.color.white));
        tvLeft.setBackgroundResource(R.drawable.shape_order_left_unclick);
        tvRight.setBackgroundResource(R.drawable.shape_order_right_click);
        vpPager.setCurrentItem(1);
    }

    private void clickLeft() {
        tvLeft.setTextColor(getActivity().getResources().getColor(R.color.white));
        tvRight.setTextColor(getActivity().getResources().getColor(R.color.main_blue));
        tvLeft.setBackgroundResource(R.drawable.shape_order_left_click);
        tvRight.setBackgroundResource(R.drawable.shape_order_right_unclick);
        vpPager.setCurrentItem(0);
    }
}

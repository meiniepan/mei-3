package com.wuyou.worker.util;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.gs.buluo.common.utils.DensityUtils;

/**
 * Created by Solang on 2017/9/25.
 */

public class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private final Context mCtx;
    View mView;

    private boolean mIsAnimatingOut = false;
    private boolean isOut = true;
    private boolean isRefresh;

    public MyRecyclerViewScrollListener(Context context, View view) {
        mView = view;
        mCtx = context;
    }

    public void setRefresh() {
        isRefresh = true;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) isRefresh = false;
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == 0 && !isOut && !isRefresh) {
            animateOut(mView);
        } else if (firstVisibleItemPosition != 0 && isOut && !isRefresh) {
            animateIn(mView);
        }
    }

    private void animateOut(final View button) {
        isOut = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "TranslationY", 0, DensityUtils.dip2px(mCtx, 70));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    // Same animation that FloatingActionButton.Behavior uses to show the FAB when the AppBarLayout enters
    private void animateIn(View button) {
        button.setVisibility(View.VISIBLE);
        isOut = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "TranslationY", DensityUtils.dip2px(mCtx, 70), 0);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }
}

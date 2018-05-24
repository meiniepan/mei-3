package com.wuyou.worker.view.widget.recyclerHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.worker.R;


/**
 * Created by hjn on 2017/7/10.
 */

public class NewRefreshRecyclerView extends FrameLayout {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private StatusLayout statusLayout;

    public NewRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public NewRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        Drawable errorDrawable;
        Drawable emptyDrawable;
        try {
            errorDrawable = a.getDrawable(R.styleable.NewRefreshRecyclerView_errorDrawable);
            emptyDrawable = a.getDrawable(R.styleable.NewRefreshRecyclerView_emptyDrawable);
        } finally {
            a.recycle();
        }
        statusLayout.getEmptyImageView().setImageDrawable(emptyDrawable);
        statusLayout.getErrorImageView().setImageDrawable(errorDrawable);
    }

    public NewRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View view = inflate(context, R.layout.status_refresh_recycler, this);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout = view.findViewById(R.id.recycler_swipe);
        statusLayout = view.findViewById(R.id.recycler_status);
        mSwipeRefreshLayout.setEnabled(false);
        setSwipeRefreshColorsFromRes(R.color.main_tab, R.color.custom_color, R.color.custom_color_shallow);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) {
        mSwipeRefreshLayout.setColorSchemeResources(colors);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    public void setRefreshAction(final OnRefreshListener action) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                action.onAction();
            }
        });
    }

    //刷新完成先调
    public void setRefreshFinished() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.clearData();
    }

    public void showEmptyView() {
        statusLayout.showEmptyView();
    }

    public void showEmptyView(String msg) {
        statusLayout.showEmptyView(msg);
    }

    public void showErrorView() {
        statusLayout.showErrorView();
    }

    public void showErrorView(String msg) {
        statusLayout.showErrorView(msg);
    }

    public void showContentView() {
        statusLayout.showContentView();
    }

    public StatusLayout getStatusLayout() {
        return statusLayout;
    }

    public void showProgressView() {
        statusLayout.showProgressView();
    }
}

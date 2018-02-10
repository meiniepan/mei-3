package com.wuyou.worker.view.widget.recyclerHelper.loadmore;


import com.wuyou.worker.R;

/**
 * Created by BlingBling on 2016/10/11.
 */

public final class SimpleLoadMoreView extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.view_status;
    }

    @Override protected int getLoadingViewId() {
        return R.id.load_more_view;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.load_error_view;
    }

    @Override protected int getLoadEndViewId() {
        return R.id.no_more_view;
    }
}

package com.wuyou.worker.view.EAEhelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Solang on 2018/8/14.
 */

public class EaeRecyclerView extends RecyclerView {
    private StateViewHelper emptyViewHelper;
    private StateViewHelper errorViewHelper;

    public EaeRecyclerView(Context context) {
        super(context);
    }

    public EaeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EaeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //初始化空白页辅助类
        emptyViewHelper = newStateViewHelper("列表数据为空");
        //初始化错误页辅助类
        errorViewHelper = newStateViewHelper("数据加载错误");
    }

    /**
     * 创建新的状态页辅助类
     *
     * @param message 状态页展示的信息
     * @return StateViewHelper
     */
    private StateViewHelper newStateViewHelper(String message) {
        //初始化状态Item
        BaseItemState stateItem = new ItemEmptyAndError(message);
        //初始化辅助类，需要一个BaseItemState
        StateViewHelper stateViewHelper = new StateViewHelper(this, stateItem);
        //设置状态页按钮的点击事件监听，处理状态页隐藏
        stateItem.setOnStateClickListener(() -> errorViewHelper.hide());
        return stateViewHelper;
    }

    public void showEmptyView() {
        emptyViewHelper.show();
    }

    public void resetEaeView() {
        if (emptyViewHelper != null) {
            emptyViewHelper.hide();
        }
        if (errorViewHelper != null) {
            errorViewHelper.hide();
        }
    }

    public void showErrorView() {
        errorViewHelper.show();
    }

}

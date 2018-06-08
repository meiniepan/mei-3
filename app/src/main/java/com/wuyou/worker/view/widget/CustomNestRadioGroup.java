package com.wuyou.worker.view.widget;

/**
 * Created by hjn on 2018/2/6.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

//这个类是源码RadioGroup复制出来,增加了递归查找子控件RadioButton
public class CustomNestRadioGroup extends LinearLayout {
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    public CustomNestRadioGroup(Context context) {
        super(context);
        init();
    }

    public CustomNestRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCheckedId = View.NO_ID;
        setOrientation(VERTICAL);   //可以在这里设置线性布局方向
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != View.NO_ID) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    /**
     * 递归查找具有选中属性的子控件
     */
    private static CompoundButton findCheckedView(View child) {
        if (child instanceof CompoundButton)
            return (CompoundButton) child;
        if (child instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) child;
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                CompoundButton check = findCheckedView(group.getChildAt(i));
                if (check != null)
                    return check;
            }
        }
        return null;// 没有找到
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        final CompoundButton view = findCheckedView(child);
        if (view != null) {
            if (view.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(view.getId());
            }
        }
        super.addView(child, index, params);
    }

    public void check(int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof CompoundButton) {
            ((CompoundButton) checkedView).setChecked(checked);
        }
    }

    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }


    public void clearCheck() {
        check(-1);
    }


    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomNestRadioGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CustomNestRadioGroup.LayoutParams;
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }


        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }


        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr,
                                         int heightAttr) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    /**
     * <p>
     * Interface definition for a callback to be invoked when the checked radio
     * .     * button changed in this group.
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>
         * Called when the checked radio button has changed. When the selection
         * is cleared, checkedId is -1.
         * </p>
         *
         * @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(CustomNestRadioGroup group, int checkedId);
    }

    private class CheckedStateTracker implements
            CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>
     * A pass-through listener acts upon the events and dispatches them to
     * another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.
     * </p>
     */
    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void onChildViewAdded(View parent, View child) {
            if (parent == CustomNestRadioGroup.this) {
                CompoundButton view = findCheckedView(child);// 查找子控件
                if (view != null) {
                    int id = view.getId();
                    // generates an id if it's missing
                    if (id == View.NO_ID
                            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        id = View.generateViewId();
                        view.setId(id);
                    }
                    view.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
                }
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == CustomNestRadioGroup.this) {
                CompoundButton view = findCheckedView(child);// 查找子控件
                if (view != null) {
                    view.setOnCheckedChangeListener(null);
                }
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}


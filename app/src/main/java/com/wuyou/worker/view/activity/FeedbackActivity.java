package com.wuyou.worker.view.activity;

import android.os.Bundle;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.R;

/**
 * Created by hjn on 2016/12/23.
 */
public class FeedbackActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.feedback_finish).setOnClickListener(v -> {
            ToastUtils.ToastMessage(FeedbackActivity.this, "意见反馈成功");
            finish();
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_feedback;
    }
}

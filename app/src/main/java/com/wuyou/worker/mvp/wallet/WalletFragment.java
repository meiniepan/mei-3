package com.wuyou.worker.mvp.wallet;

import android.os.Bundle;

import com.wuyou.worker.R;
import com.wuyou.worker.view.fragment.BaseFragment;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class WalletFragment extends BaseFragment {


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    public void showError(String message, int res) {

    }
}

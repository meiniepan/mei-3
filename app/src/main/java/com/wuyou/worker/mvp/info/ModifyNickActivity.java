package com.wuyou.worker.mvp.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.UserApis;
import com.wuyou.worker.view.activity.BaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/3/14.
 */

public class ModifyNickActivity extends BaseActivity {
    @BindView(R.id.et_input_nick)
    EditText editText;
    @BindView(R.id.tv_info_title)
    TextView tvTitle;
    private String from;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        from = getIntent().getStringExtra(Constant.FROM);
        switch (from) {
            case Constant.NICK:
                tvTitle.setText("修改昵称");
                editText.setHint("请填写昵称");
                break;
            case Constant.EMAIL:
                findViewById(R.id.info_update_mark).setVisibility(View.GONE);
                tvTitle.setText("修改邮箱");
                editText.setHint("请填写邮箱");
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_nick;
    }


    @OnClick({R.id.btn_modify_nick})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_modify_nick:
                String input = editText.getText().toString();
                switch (from) {
                    case Constant.NICK:
                        if (editText.length() < 1 || editText.length() > 12) {
                            ToastUtils.ToastMessage(getCtx(), getString(R.string.nickname_not_right));
                            return;
                        }
                        Pattern p = Pattern.compile("^[a-zA-Z0-9_\\-\\u4e00-\\u9fa5]+$");
                        Matcher m = p.matcher(input);
                        if (!m.matches()) {
                            ToastUtils.ToastMessage(getCtx(), getString(R.string.nickname_not_right));
                            return;
                        }
                        updateInfo("name", input);
                        break;
                    case Constant.EMAIL:
                        Pattern pattern = Pattern.compile("^[0-9a-z]+\\w*@([0-9a-z]+\\.)+[0-9a-z]+$");
                        Matcher matcher = pattern.matcher(input);
                        if (!matcher.matches()) {
                            ToastUtils.ToastMessage(getCtx(), getString(R.string.please_input_right_email));
                            return;
                        }
                        updateInfo("email", input);
                        break;
                }

                break;
        }
    }


    private void updateInfo(String key, String value) {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .updateUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns()
                        .put("field", key)
                        .put("value", value)
                        .buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        setResult(RESULT_OK, new Intent().putExtra("info", editText.getText().toString()));
                        finish();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    }
                });

    }
}

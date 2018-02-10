package com.wuyou.worker.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.ContractEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solang on 2018/2/5.
 */

public class ContractRvAdapter extends BaseQuickAdapter<ContractEntity, BaseHolder> {
    private Activity activity;

    public ContractRvAdapter(Activity activity, int layoutResId, @Nullable List<ContractEntity> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseHolder helper, ContractEntity item) {
        String b_time = TribeDateUtils.dateFormat5(new Date(item.start_at*1000));
        String e_time = TribeDateUtils.dateFormat5(new Date(item.end_at*1000));
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_time, b_time+" 至 "+e_time)
                .setText(R.id.tv_times, item.times)
                .setText(R.id.tv_rule,item.allocation_rule);
        helper.setOnClickListener(R.id.btn_sign, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                        .signContract(CarefreeApplication.getInstance().getUserInfo().getUid(),
                                QueryMapBuilder.getIns().put("contract_id", item.id).buildPost())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSubscriber<BaseResponse>() {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ToastUtils.ToastMessage(activity, "签署成功！");
//                                Intent intent = new Intent(getActivity(), MainActivity.class);
//                                getActivity().startActivity(intent);
                            }

                        });

            }
        });

    }
}

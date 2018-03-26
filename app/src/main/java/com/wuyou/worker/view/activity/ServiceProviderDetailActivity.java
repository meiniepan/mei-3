package com.wuyou.worker.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.ContractRvAdapter;
import com.wuyou.worker.adapter.UnionAvatarRvAdapter;
import com.wuyou.worker.bean.entity.ContractEntity;
import com.wuyou.worker.bean.entity.MerchantDetailEntity;
import com.wuyou.worker.bean.entity.UnionListEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solang on 2018/2/6.
 */

public class ServiceProviderDetailActivity extends BaseActivity {
    List<Integer> data = new ArrayList<>();
    String id;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rb_star)
    RatingBar rbStar;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_qualification)
    TextView tvQualification;
    @BindView(R.id.tv_union_num)
    TextView tvUnionNum;
    @BindView(R.id.rv_avatar)
    RecyclerView rvAvatar;
    @BindView(R.id.tv_tag_more)
    TextView tvTagMore;
    @BindView(R.id.rv_contract)
    RecyclerView rvContract;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_service_provider_detail;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        id = getIntent().getStringExtra(Constant.MERCHANT_ID);
        initData();
    }

    private void initData() {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getMerchantDetail(CarefreeApplication.getInstance().getUserInfo().getWorker_id(), id, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<MerchantDetailEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<MerchantDetailEntity> response) {
                        initUI(response.data);
                    }

                });
    }

    private void initUI(MerchantDetailEntity data) {
        rbStar.setRating(data.star);
        tvName.setText(data.name);
        tvCategory.setText(data.category);
        tvAddress.setText(data.address);
        tvPhone.setText(data.tel_number);
        tvQualification.setText(data.qualification);
        if (data.unions != null) {
            tvUnionNum.setText(data.unions.count + "个盟友");
            initAvatarList(data.unions);
        }
        if (data.contracts != null) {
            initContractList(data.contracts);
        }
    }

    private void initContractList(List<ContractEntity> contracts) {
        rvContract.setLayoutManager(new LinearLayoutManager(getCtx()));
        ContractRvAdapter adapter = new ContractRvAdapter(this, R.layout.item_service_pro_detail, contracts);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                Intent intent = new Intent(getCtx(), ContractDetailActivity.class);
                intent.putExtra(Constant.CONTRACT_ID, adapter.getItem(position).id);
                startActivity(intent);
            }
        });
        rvContract.setAdapter(adapter);
    }

    private void initAvatarList(UnionListEntity unions) {
        rvAvatar.setLayoutManager(new LinearLayoutManager(getCtx()));
        UnionAvatarRvAdapter adapter = new UnionAvatarRvAdapter(this, R.layout.item_union_avatar, unions.list);
        rvAvatar.setAdapter(adapter);
    }


    @OnClick(R.id.rv_avatar)
    public void onViewClicked() {

    }
}

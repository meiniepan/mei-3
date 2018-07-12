package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.ChooseService2Adapter;
import com.wuyou.worker.adapter.ChooseServiceAdapter;
import com.wuyou.worker.bean.entity.ChosenServiceEntity;
import com.wuyou.worker.bean.entity.ResponseListEntity;
import com.wuyou.worker.bean.entity.ServiceSort2;
import com.wuyou.worker.bean.entity.ServiceSort2Entity;
import com.wuyou.worker.bean.entity.ServiceSortEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Solang on 2018/7/5.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ExtraChooseServiceActivity extends BaseActivity implements AddReduceNumListener {
    @BindView(R.id.rv_extra_choose_service_l)
    RecyclerView rvExtraChooseServiceL;
    @BindView(R.id.rv_extra_choose_service_r)
    RecyclerView rvExtraChooseServiceR;
    @BindView(R.id.tv_extra_sum)
    TextView tvExtraSum;
    @BindView(R.id.tv_extra_settle)
    TextView tvExtraSettle;
    @BindView(R.id.tv_extra_text)
    TextView tvExtraText;
    @BindView(R.id.sl_extra_choose_service)
    StatusLayout statusLayout;
    @BindView(R.id.sl_extra_choose_service_2)
    StatusLayout statusLayout2;
    ChooseServiceAdapter adapterLeft;
    ChooseService2Adapter adapterRight;
    List<ServiceSortEntity> dataLeft = new ArrayList();
    List<ServiceSort2> dataRight = new ArrayList();
    ArrayList<ChosenServiceEntity> chosenData = new ArrayList<>();
    private float totalSum;
    private String categoryId;
    private String loadMoreStartId = "0";

    @Override
    protected int getContentLayout() {
        return R.layout.activity_extra_choose_service;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("选择服务");
        initLeftRv();
        initRightRv();
        setOnNoChosen();
        getData();
    }

    private void getData() {
        statusLayout.showProgressView();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getServiceSort(CarefreeDaoSession.getInstance().getUserInfo().getWorker_id(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ResponseListEntity<ServiceSortEntity>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ResponseListEntity<ServiceSortEntity>> response) {
                        dataLeft = response.data.list;
                        if (dataLeft.size() > 0) {
                            statusLayout.showContentView();
                            adapterLeft.setNewData(dataLeft);
                        } else statusLayout.showEmptyView();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        statusLayout.showErrorView(e.getDisplayMessage());
                    }
                });
    }

    private void initLeftRv() {
        adapterLeft = new ChooseServiceAdapter(R.layout.item_service_sort, dataLeft);
        rvExtraChooseServiceL.setLayoutManager(new LinearLayoutManager(this));
        rvExtraChooseServiceL.setAdapter(adapterLeft);
        adapterLeft.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                categoryId = adapterLeft.getItem(i).category_id;
                for (int j = 0; j < dataLeft.size(); j++) {

                    if (adapterLeft.getItem(j).category_id.equals(adapterLeft.getItem(i).category_id)) {
                        adapterLeft.getItem(j).click = true;
                    } else {
                        adapterLeft.getItem(j).click = false;
                    }
                }
                adapterLeft.notifyDataSetChanged();
                getSubData(adapterLeft.getItem(i).category_id);
            }
        });
    }

    Map<String, List<ServiceSort2>> cacheData = new ArrayMap<>();

    private void getSubData(String category_id) {
        if (cacheData.containsKey(category_id)) {
            adapterRight.setNewData(cacheData.get(category_id));
        } else {
            getFromNet(category_id);
        }
    }

    private void getFromNet(String category_id) {
        statusLayout2.showProgressView();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getServiceSubSort(CarefreeDaoSession.getInstance().getUserInfo().getWorker_id(), QueryMapBuilder.getIns().put("category_id", category_id).put("start_id", "0").put("flag", "1").buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ServiceSort2Entity>>() {
                    @Override
                    public void onSuccess(BaseResponse<ServiceSort2Entity> response) {
                        dataRight = response.data.list;
                        if (dataRight.size() > 0) {
                            statusLayout2.showContentView();
                            cacheData.put(category_id, dataRight);
                            adapterRight.setNewData(cacheData.get(category_id));
                        } else statusLayout2.showEmptyView();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        statusLayout2.showErrorView(e.getDisplayMessage());
                    }
                });
    }


    private void initRightRv() {
        adapterRight = new ChooseService2Adapter(R.layout.item_service_sort_2, dataRight);
        adapterRight.setAddReduceNumLis(this);
        adapterRight.setOnLoadMoreListener(() -> getMore(), rvExtraChooseServiceR);
        rvExtraChooseServiceR.setLayoutManager(new LinearLayoutManager(this));
        rvExtraChooseServiceR.setAdapter(adapterRight);
    }

    private void getMore() {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getServiceSubSort(CarefreeDaoSession.getInstance().getUserInfo().getWorker_id(), QueryMapBuilder.getIns().put("category_id", categoryId).put("start_id", loadMoreStartId).put("flag", "2").buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ServiceSort2Entity>>() {
                    @Override
                    public void onSuccess(BaseResponse<ServiceSort2Entity> response) {
                        if (response.data.list.size() > 0) {
                            loadMoreStartId = response.data.list.get(response.data.list.size() - 1).id;
                        }
                        adapterRight.addData(response.data.list);
                        if (response.data.has_more.equals("0")) {
                            adapterRight.loadMoreEnd(true);
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        adapterRight.loadMoreFail();
                    }
                });
    }


    private void setOnNoChosen() {
        tvExtraText.setText("未选择服务");
        tvExtraSum.setVisibility(View.INVISIBLE);
        tvExtraSettle.setBackgroundResource(R.drawable.dark_orange_round_bac);
        tvExtraSettle.setClickable(false);
    }

    private void setOnChosen() {
        tvExtraText.setText("合计:");
        tvExtraSum.setVisibility(View.VISIBLE);
        tvExtraSettle.setBackgroundResource(R.drawable.orange_round_bac);
        tvExtraSettle.setClickable(true);
    }


    @Override
    public void addNum(ChosenServiceEntity entity) {
        if (chosenData.size() == 0) {
            setOnChosen();
            chosenData.add(entity);
            totalSum = totalSum + entity.price;
            tvExtraSum.setText("¥" + CommonUtil.formatPrice(totalSum));
            return;
        }
        boolean isHave = false;
        for (ChosenServiceEntity e : chosenData
                ) {
            if (entity.id.equals(e.id) && !TextUtils.isEmpty(entity.subId)) {
                if (entity.subId.equals(e.subId)) {
                    e.number = entity.number;
                    isHave = true;
                }
            } else if (entity.id.equals(e.id) && TextUtils.isEmpty(entity.subId)) {
                e.number = entity.number;
                isHave = true;
            }
        }
        if (!isHave) chosenData.add(entity);
        totalSum = totalSum + entity.price;
        tvExtraSum.setText("¥" + CommonUtil.formatPrice(totalSum));
    }

    @Override
    public void reduceNum(ChosenServiceEntity entity) {
        if (entity.number <= 0) {
            ChosenServiceEntity ee = new ChosenServiceEntity();
            for (ChosenServiceEntity e : chosenData
                    ) {
                if (TextUtils.isEmpty(entity.subId)) {
                    if (entity.id == e.id) ee = e;
                } else {
                    if (entity.id == e.id && entity.subId == e.subId) ee = e;
                }
            }
            chosenData.remove(ee);
            if (chosenData.size() == 0) {
                setOnNoChosen();
            }

        } else {
            for (ChosenServiceEntity e : chosenData
                    ) {
                if (entity.id.equals(e.id) && !TextUtils.isEmpty(entity.subId)) {
                    if (entity.subId.equals(e.subId)) {
                        e.number = entity.number;
                    }
                } else if (entity.id.equals(e.id) && TextUtils.isEmpty(entity.subId)) {
                    e.number = entity.number;
                }
            }
        }
        totalSum = totalSum - entity.price;
        tvExtraSum.setText("¥" + CommonUtil.formatPrice(totalSum));
    }

    @OnClick(R.id.tv_extra_settle)
    public void onViewClicked() {
        Intent intent = new Intent(getCtx(), ExtraChooseServiceConfirmActivity.class);
        intent.putExtra(Constant.CHOSEN_SERVICE_TOTAL, totalSum);
        intent.putParcelableArrayListExtra(Constant.CHOSEN_SERVICE, chosenData);
        startActivity(intent);
    }
}

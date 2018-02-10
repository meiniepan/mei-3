/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wuyou.worker.mvp.companies;

import android.support.annotation.NonNull;

import com.wuyou.worker.bean.Company;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.CompanyApis;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompaniesPresenter extends CompaniesContract.Presenter {
    @NonNull
    private CompaniesContract.View view;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public CompaniesPresenter() {
        this.view = view;
//        this.companiesRepository = companiesRepository;
        compositeDisposable = new CompositeDisposable();
//        this.view.setPresenter(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    private void getCompanies() {
        Disposable disposable = CarefreeRetrofit.getInstance().createApi(CompanyApis.class)
                .getCompaniesList("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Company>>() {
                    @Override
                    public void onNext(List<Company> value) {
                        view.showCompanies(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    void getCompanyInfo() {

    }
}

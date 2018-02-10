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

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.wuyou.worker.R;
import com.wuyou.worker.bean.Company;
import com.wuyou.worker.view.activity.BaseActivity;

import java.util.List;

/**
 * Created by hjn on 2017/2/10.
 */

public class CompaniesActivity extends BaseActivity<CompaniesContract.View, CompaniesContract.Presenter> implements CompaniesContract.View {
    private RecyclerView recyclerView;
    private CompaniesAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected CompaniesContract.Presenter getPresenter() {
        return new CompaniesPresenter();
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mPresenter.getCompanyInfo();
    }

    @Override
    public void showCompanies(List<Company> list) {
    }


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_main, container, false);
//        setHasOptionsMenu(true);
//        return view;
//    }


//    @Override
//    public void showGetCompaniesError() {
//        Snackbar.make(recyclerView, R.string.something_wrong, Snackbar.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void showCompanies(final List<Company> list) {
//        if (adapter == null) {
//            adapter = new CompaniesAdapter(getContext(), list);
//            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
//                @Override
//                public void OnItemClick(View v, int position) {
//                    Intent intent = new Intent(getContext(), CompanyDetailActivity.class);
//                    intent.putExtra(CompanyDetailActivity.COMPANY_ID, list.get(position).getId());
//                    startActivity(intent);
//                }
//            });
//            recyclerView.setAdapter(adapter);
//        }
//    }

}

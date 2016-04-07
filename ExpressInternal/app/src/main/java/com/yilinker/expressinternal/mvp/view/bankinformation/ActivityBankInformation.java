package com.yilinker.expressinternal.mvp.view.bankinformation;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.model.BankInformation;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.bankinformation.BankInformationPresenter;
import com.yilinker.expressinternal.mvp.view.BaseActivity;

import java.util.List;

/**
 * Created by patrick-villanueva on 4/6/2016.
 */
public class ActivityBankInformation extends BaseActivity implements IBankInformationView{

    private SwipeRefreshLayout refreshLayout;

    private BankInformationPresenter presenter;

    private BankAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setActionBarLayout(R.layout.layout_toolbar_registration);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_information);

        if (savedInstanceState == null){
            presenter = new BankInformationPresenter();
            initializeViews(null);

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

    }

    @Override
    public void initializeViews(View parent) {

        setActionBarTitle(getString(R.string.tools_bank_information));
        setRecyclerAdapter();
    }

    private void setRecyclerAdapter(){

        adapter = new BankAdapter();

        RecyclerView rvBanks = (RecyclerView) findViewById(R.id.rvBanks);
        rvBanks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBanks.setAdapter(adapter);

        refreshLayout =  (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                presenter.requestBankInformation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
        presenter.requestBankInformation();
        showLoader(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    @Override
    public void showLoader(boolean isShown) {

        refreshLayout.setRefreshing(isShown);
    }

    @Override
    public void addAllBanks(List<BankInformation> banks) {

        adapter.clearAndAddAll(banks);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }
}

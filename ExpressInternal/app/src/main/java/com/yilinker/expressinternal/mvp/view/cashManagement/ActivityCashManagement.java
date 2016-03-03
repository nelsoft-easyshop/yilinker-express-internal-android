package com.yilinker.expressinternal.mvp.view.cashManagement;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.cashmanagement.AdapterCashHistory;
import com.yilinker.expressinternal.model.CashDetail;
import com.yilinker.expressinternal.model.CashHistory;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.cashManagement.CashManagementPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.List;

/**
 * Created by Patrick on 3/3/2016.
 */
public class ActivityCashManagement extends BaseFragmentActivity implements ICashManagementView {

    private TextView tvCashOnHand;
    private TextView tvCashLimit;
    private RelativeLayout rlProgress;
    SwipeRefreshLayout refreshLayout;

    private CashManagementPresenter presenter;
    private CashHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){
            presenter = new CashManagementPresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        setContentView(R.layout.activity_cash_management);

        initializeViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
        presenter.requestCashDetails();
        showLoader();

    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.onPause();
        presenter.unbindView();
    }

    @Override
    public void initializeViews() {

        tvCashLimit = (TextView) findViewById(R.id.tvCashLimit);
        tvCashOnHand = (TextView) findViewById(R.id.tvCashOnHand);
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

        RecyclerView rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapter = new CashHistoryAdapter();
        rvHistory.setAdapter(adapter);

        /***set-up pull to refresh*/
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                presenter.requestCashDetails();
            }
        });
    }

    @Override
    public void handleCashDetails(List<CashHistory> cashHistories) {
        adapter.clearAndAddAll(cashHistories);
    }

    @Override
    public void clearCashHistory(CashDetail cashDetail) {
        adapter.clearAndAddAll(cashDetail.getCashHistory());
    }

    @Override
    public void setCashLimit(String cashLimit) {
        tvCashLimit.setText(cashLimit);

    }

    @Override
    public void setCashOnHand(String cashOnHand) {
        tvCashOnHand.setText(cashOnHand);

    }

    @Override
    public void showLoader(boolean isVisible) {
        refreshLayout.setRefreshing(isVisible);

    }

    /***to show the loader at first load of the page*/
    private void showLoader(){
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void showErrorMessage(boolean isVisible, String errorMessage) {
        TextView tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        tvErrorMessage.setVisibility(isVisible? View.VISIBLE:View.GONE);
        tvErrorMessage.setText(errorMessage);
    }

}

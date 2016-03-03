package com.yilinker.expressinternal.mvp.view.cashManagement;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private List<CashHistory> historyList;
    private CashDetail cashDetail;

    private CashManagementPresenter presenter;

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
        //TODO set adapter here
    }

    @Override
    public void handleCashDetails(CashDetail cashDetail) {
        tvCashLimit.setText(PriceFormatHelper.formatPrice(cashDetail.getCashLimit()));
        tvCashOnHand.setText(PriceFormatHelper.formatPrice(cashDetail.getCashOnHand()));

        //TODO notify changes for the adapter here

    }

    @Override
    public void showLoader(boolean isVisible) {
        rlProgress.setVisibility(isVisible? View.VISIBLE: View.GONE);
    }

    @Override
    public void showErrorMessage(boolean isVisible, String errorMessage) {
        //TODO add error message
    }

}

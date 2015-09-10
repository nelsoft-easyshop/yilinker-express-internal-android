package com.yilinker.expressinternal.controllers.cashmanagement;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.CashDetail;
import com.yilinker.expressinternal.model.CashHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class ActivityCashManagement extends BaseActivity implements ResponseHandler{

    private static final int REQUEST_GET_CASH_DETAILS = 1000;

    private TextView tvCashOnHand;
    private TextView tvCashLimit;
    private RecyclerView rvHistory;
    private RelativeLayout rlProgress;

    private List<CashHistory> historyList;
    private AdapterCashHistory adapter;
    private  CashDetail cashDetail;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_management);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        setActionBarTitle(getString(R.string.actionbar_title_cashmanagement));

        hideMenuButton();

        initViews();

        setAdapter();

    }

    @Override
    protected void onPause() {
        super.onPause();

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestGetCashDetails();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        switch (requestCode){

            case REQUEST_GET_CASH_DETAILS:

                cashDetail = new CashDetail((com.yilinker.core.model.express.internal.CashDetail) object);
                resetDetails();
                break;

        }

        rlProgress.setVisibility(View.GONE);

    }

    @Override
    public void onFailed(int requestCode, String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
    }

    private void initViews(){

        tvCashLimit = (TextView) findViewById(R.id.tvCashLimit);
        tvCashOnHand = (TextView) findViewById(R.id.tvCashOnHand);
        rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

    }

    private void resetDetails(){

        tvCashLimit.setText(String.format("%.02f PHP",cashDetail.getCashLimit()));
        tvCashOnHand.setText(String.format("%.02f PHP",cashDetail.getCashOnHand()));

        historyList.clear();
        historyList.addAll(cashDetail.getCashHistory());
        adapter.notifyDataSetChanged();
    }

    private void setAdapter(){

        historyList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistory.setLayoutManager(layoutManager);

        adapter = new AdapterCashHistory(historyList, null);

        rvHistory.setAdapter(adapter);

    }

    private void requestGetCashDetails(){

        rlProgress.setVisibility(View.VISIBLE);

        Request request = RiderAPI.getCashDetails(REQUEST_GET_CASH_DETAILS, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);
    }


}

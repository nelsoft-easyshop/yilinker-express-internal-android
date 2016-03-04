package com.yilinker.expressinternal.mvp.presenter.cashManagement;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.model.CashDetail;
import com.yilinker.expressinternal.model.CashHistory;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.cashManagement.ICashManagementView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.List;

/**
 * Created by Patrick on 3/3/2016.
 */
public class CashManagementPresenter  extends RequestPresenter<CashDetail, ICashManagementView>
        implements ICashManagementPresenter{

    private final static int REQUEST_GET_CASH_DETAIL = 2000;

    private ApplicationClass applicationClass;
    private RequestQueue requestQueue;

    private CashDetail cashDetail;

    public CashManagementPresenter(){
        applicationClass = (ApplicationClass) ApplicationClass.getInstance();
        requestQueue = applicationClass.getRequestQueue();
    }

    @Override
    protected void updateView() {
        view().handleCashDetails(cashDetail.getCashHistory());
        view().setCashLimit(getFormattedCashLimit());
        view().setCashOnHand(getFormattedCashOnHand());
    }


    public String getFormattedCashOnHand(){
        return PriceFormatHelper.formatPrice(cashDetail.getCashOnHand());
    }


    public String getFormattedCashLimit(){
        return PriceFormatHelper.formatPrice(cashDetail.getCashLimit());
    }


    @Override
    public void requestCashDetails() {
//        view().showLoader(true);
        view().showErrorMessage(false,"");

        Request request = RiderAPI.getCashDetails(REQUEST_GET_CASH_DETAIL, this);
        request.setTag(ApplicationClass.REQUEST_TAG);
        requestQueue.add(request);

//        /***clear list of cash history every request*/
//        if (cashDetail!=null){
//            if (cashDetail.getCashHistory().size()!=0){
//
//                cashDetail.getCashHistory().clear();
//                view().clearCashHistory(cashDetail);
//            }
//        }
    }

    @Override
    public void onPause() {
     requestQueue.cancelAll(applicationClass.REQUEST_TAG);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        switch (requestCode){

            case REQUEST_GET_CASH_DETAIL:
                view().showLoader(false);
                handleCashDetails(object);
                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        view().showLoader(false);
        view().showErrorMessage(true, message);
    }

    private void handleCashDetails(Object object){
        cashDetail = new CashDetail((com.yilinker.core.model.express.internal.CashDetail) object);
        updateView();
    }
}

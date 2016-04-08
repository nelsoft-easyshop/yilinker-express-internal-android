package com.yilinker.expressinternal.mvp.presenter.cashManagement;

import com.android.volley.Request;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.expressinternal.model.CashDetail;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.cashManagement.ICashManagementView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/3/2016.
 */
public class CashManagementPresenter  extends RequestPresenter<CashDetail, ICashManagementView>
        implements ICashManagementPresenter{

    private final static int REQUEST_GET_CASH_DETAIL = 2000;
    private final static String GET_CASH_DETAILS_REQUEST_TAG = "request-tag";

    private CashDetail cashDetail;

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
        view().showErrorMessage(false,"");

        Request request = RiderAPI.getCashDetails(REQUEST_GET_CASH_DETAIL, this);
        request.setTag(GET_CASH_DETAILS_REQUEST_TAG);
        view().addRequest(request);

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
    public void onPause()
    {
     view().cancelAllRequest(getRequestTags());
    }

    private List<String> getRequestTags(){
        List<String> lists = new ArrayList<>();
        lists.add(GET_CASH_DETAILS_REQUEST_TAG);

        return lists;
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_GET_CASH_DETAIL:
                view().showLoader(false);
                handleCashDetails(object);
                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onSuccess(requestCode, message);

        view().showLoader(false);
        view().showErrorMessage(true, message);
    }

    private void handleCashDetails(Object object){
        cashDetail = new CashDetail((com.yilinker.core.model.express.internal.CashDetail) object);
//        updateView();
        setModel(cashDetail);
    }
}

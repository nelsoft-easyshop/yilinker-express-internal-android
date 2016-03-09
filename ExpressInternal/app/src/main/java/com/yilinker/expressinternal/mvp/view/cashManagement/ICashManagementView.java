package com.yilinker.expressinternal.mvp.view.cashManagement;

import com.android.volley.Request;
import com.yilinker.expressinternal.model.CashDetail;
import com.yilinker.expressinternal.model.CashHistory;

import java.util.List;

/**
 * Created by Patrick on 3/3/2016.
 */
public interface ICashManagementView {

    public void handleCashDetails(List<CashHistory> cashHistories);
    public void clearCashHistory(CashDetail cashDetail);
    public void setCashLimit(String cashLimit);
    public void setCashOnHand(String cashOnHand);
    public void showLoader(boolean isVisible);
    public void showErrorMessage(boolean isVisible, String errorMessage);
    public void addRequest(Request request);
    public void cancelAllRequest(List<String> requestTags);
}

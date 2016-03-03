package com.yilinker.expressinternal.mvp.view.cashManagement;

import com.yilinker.expressinternal.model.CashDetail;

/**
 * Created by Patrick on 3/3/2016.
 */
public interface ICashManagementView {

    public void handleCashDetails(CashDetail cashDetail);
    public void showLoader(boolean isVisible);
    public void showErrorMessage(boolean isVisible, String errorMessage);
}

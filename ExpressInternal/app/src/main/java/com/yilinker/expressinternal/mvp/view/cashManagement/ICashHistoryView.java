package com.yilinker.expressinternal.mvp.view.cashManagement;

import com.yilinker.expressinternal.model.CashHistory;

/**
 * Created by Patrick on 3/3/2016.
 */
public interface ICashHistoryView {

    public void setViews(CashHistory cashHistory);
    public void setFormattedDate(String date);
    public void setFormattedAmount(String amount);
    public void setFormatterRunningTotal(String runningTotal);
}

package com.yilinker.expressinternal.mvp.view.cashManagement;


/**
 * Created by Patrick on 3/3/2016.
 */
public interface ICashHistoryView {

    public void setFormattedDate(String date);
    public void setFormattedAmount(String amount);
    public void setFormatterRunningTotal(String runningTotal);
    public void setWaybillNumber(String waybillNumber);
    public void setType(String type);
}

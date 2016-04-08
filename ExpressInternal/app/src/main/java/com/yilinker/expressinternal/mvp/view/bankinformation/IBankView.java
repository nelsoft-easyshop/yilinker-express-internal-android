package com.yilinker.expressinternal.mvp.view.bankinformation;

/**
 * Created by patrick-villanueva on 4/6/2016.
 */
public interface IBankView {

    public void setBankName(String bankName);
    public void setAccountName(String accountName);
    public void setAccountNumber(String accountNumber);
    public void setLogoURL(String logoURL);
    public void setDropDownOpen(boolean isToOpen);

}

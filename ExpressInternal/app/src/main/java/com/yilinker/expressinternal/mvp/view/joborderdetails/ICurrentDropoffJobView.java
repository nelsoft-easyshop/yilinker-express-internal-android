package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.mvp.view.RequestBaseView;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface ICurrentDropoffJobView extends IJobDetailsView {

    public void setDateAcceptedText(String dateAccepted);
    public void setTimeElapsedText(String timeElapsed);
    public void setDropoffAddress(String dropoffAddress);
    public void setItemText(String items);
    void goBackToList();

}

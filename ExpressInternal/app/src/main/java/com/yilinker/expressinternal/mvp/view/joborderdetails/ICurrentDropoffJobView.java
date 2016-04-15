package com.yilinker.expressinternal.mvp.view.joborderdetails;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface ICurrentDropoffJobView extends IJobDetailsView {

    public void setDateAcceptedText(String dateAccepted);
    public void setTimeElapsedText(String timeElapsed);
    public void setDropoffAddress(String dropoffAddress);
    public void setItemText(String items);
    void goBackToList();
    boolean ifUpdated(String jobOrderNo);
    void showSyncStatus(boolean updated);
    void startSyncService();
    void initializeReceivers();
    void showNoInternetConnection();
    void restartMain();

}

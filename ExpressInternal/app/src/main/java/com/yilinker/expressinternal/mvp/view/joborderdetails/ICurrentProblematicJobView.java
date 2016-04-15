package com.yilinker.expressinternal.mvp.view.joborderdetails;

import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface ICurrentProblematicJobView extends IJobDetailsView {

    public void setDateAcceptedText(String dateAccepted);
    public void setDeliveryAddress(String deliveryAddress);
    public void setItemText(String items);
    public void setProblemType(String problemType);
    public void showReportedImages(List<String> imageUrls);
    public void showNoImageError();
    boolean ifUpdated(String jobOrderNo);
    void showSyncStatus(boolean updated);
    void startSyncService();
    void initializeReceivers();
    void showNoInternetConnection();
    void restartMain();

}

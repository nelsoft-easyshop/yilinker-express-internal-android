package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.view.RequestBaseView;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface ICurrentDeliveryJobView extends IJobDetailsView {

    void setTimeElapsedText(String time);
    void setDeliveryAddressText(String deliveryAddress);
    void setDateAccepted(String dateAccepted);
    void setShipperName(String deliveryName);
    void setContactNumber(String shipperNo);
    void setItemText(String items);
    void setAmountToCollectText(String amountToCollect);
    void openChecklistDelivery(JobOrder jobOrder);
    void openProblematicOptions(String jobOrderNo);

}

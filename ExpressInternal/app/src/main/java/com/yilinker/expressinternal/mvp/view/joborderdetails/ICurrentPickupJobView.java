package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.view.RequestBaseView;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface ICurrentPickupJobView extends IJobDetailsView {

    public void setTimeElapsedText(String time);
    public void setDateAccepted(String dateAccepted);
    public void setPickupAddressText(String pickupAddress);
    public void setConsigneeNameText(String consigneeName);
    public void setConsigneeNoText(String consigneeNo);
    public void setItemText(String items);
    public void setAmountToCollectText(String amountToCollect);
    public void showProblematicForm(JobOrder jobOrder);
}

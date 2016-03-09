package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.view.RequestBaseView;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface ICurrentDeliveryJobView extends IJobDetailsView {

    public void setTimeElapsedText(String time);
    public void setDeliveryAddressText(String deliveryAddress);
    public void setDeliveryNameText(String deliveryName);
    public void setShipperNoText(String shipperNo);
    public void setItemText(String items);
    public void setAmountToCollectText(String amountToCollect);

}

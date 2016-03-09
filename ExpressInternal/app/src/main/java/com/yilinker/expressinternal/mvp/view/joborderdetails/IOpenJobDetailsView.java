package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.mvp.view.RequestBaseView;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface IOpenJobDetailsView extends IJobDetailsView,RequestBaseView {

    public void setPickupAddressText(String pickupAddress);
    public void setShipperNameText(String shipperName);
    public void setShipperContactNo(String contactNo);
    public void setDeliveryAddressText(String deliveryAddress);
    public void setConsigneeNameText(String consigneeName);
    public void setConsigneeContactNo(String contactNo);
    public void goBackToList();
}

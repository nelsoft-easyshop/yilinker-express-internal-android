package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.android.volley.Request;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.view.RequestBaseView;

import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface ICurrentPickupJobView extends IJobDetailsView,RequestBaseView {

    public void setTimeElapsedText(String time);
    public void setDateAccepted(String dateAccepted);
    public void setPickupAddressText(String pickupAddress);
    public void setConsigneeNameText(String consigneeName);
    public void setConsigneeNoText(String consigneeNo);
    public void setItemText(String items);
    public void setAmountToCollectText(String amountToCollect);
    public void showProblematicForm(JobOrder jobOrder);
    public void showOutOfStock();
    public void goToChecklist();
    public void addToRequestQueue(Request request);
    public void showErrorMessage(String errorMessage);
    public void handleOutOfStockResponse();
}

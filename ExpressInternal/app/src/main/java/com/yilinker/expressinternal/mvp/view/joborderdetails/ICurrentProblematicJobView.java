package com.yilinker.expressinternal.mvp.view.joborderdetails;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface ICurrentProblematicJobView extends IJobDetailsView {

    public void setDateAcceptedText(String dateAccepted);
    public void setDeliveryAddress(String deliveryAddress);
    public void setItemText(String items);
    void setProblemType(String problemType);

}

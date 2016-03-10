package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;

import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public interface IJobDetailsMainView {

    public void showContactScreen(JobOrder jobOrder);
    public void showPrintScreen(JobOrder jobOrder);
    public void showQRCodeScreen(JobOrder jobOrder);
    public void showImages(List<String> imageUrls);
    public void showNavigationMapScreen(JobOrder jobOrder);
    public void showOpenDetails(JobOrder jobOrder);
    public void showCurrentPickupDetails(JobOrder jobOrder);
    public void showDropoffDetails(JobOrder jobOrder);
    public void showCurrentDeliveryDetails(JobOrder jobOrder);
    public void showProblematicDeliveryDetails(JobOrder jobOrder);
    public void showCompletedDetails(JobOrder jobOrder);
    void updateViewForJobOrder(String status);
}

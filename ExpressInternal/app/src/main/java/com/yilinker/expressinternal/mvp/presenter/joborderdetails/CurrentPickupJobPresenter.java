package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentPickupJobView;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentPickupJobPresenter extends BasePresenter<JobOrder, ICurrentPickupJobView> {


    @Override
    protected void updateView() {

        view().setEarningText(String.format("P%.2f", model.getEarning()));
        view().setWaybillNoText(model.getWaybillNo());
        view().setStatusText(model.getStatus());
        view().setDateCreatedText("Sample Date Created");
        view().setAmountToCollectText(String.format("P%.2f", model.getEarning()));
        view().setConsigneeNameText(model.getRecipient());
        view().setConsigneeNoText(model.getContactNo());
        view().setPickupAddressText(model.getPickupAddress());
        view().setItemText("Items");
    }


}

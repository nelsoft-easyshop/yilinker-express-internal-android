package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.IJobDetailsView;
import com.yilinker.expressinternal.mvp.view.joborderdetails.IOpenJobDetailsView;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.IOpenJobView;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class OpenJobDetailsPresenter extends BasePresenter<JobOrder, IOpenJobDetailsView>{


    @Override
    protected void updateView() {

        view().setConsigneeContactNo(model.getContactNo());
        view().setConsigneeNameText(model.getRecipient());
        view().setDeliveryAddressText(model.getDeliveryAddress());
        view().setPickupAddressText(model.getPickupAddress());
        view().setShipperNameText("Shipper Name");
        view().setShipperContactNo("099999999");
        view().setDateCreatedText("Date Created");
        view().setEarningText(String.format("P%.2f", model.getEarning()));
        view().setStatusText(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
    }

}

package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentDeliveryJobView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentDeliveryJobPresenter extends BasePresenter<JobOrder, ICurrentDeliveryJobView> {


    @Override
    protected void updateView() {

        view().setStatusText(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
        view().setDeliveryAddressText(model.getDeliveryAddress());
        view().setAmountToCollectText(PriceFormatHelper.formatPrice(model.getAmountToCollect()));
        view().setContactNumber(model.getRecipientContactNo());
        view().setShipperName(model.getRecipientName());
        view().setDateCreatedText("date");
        view().setItemText("items");
        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));

    }

    public void openDeliveryChecklist() {
        view().openChecklistDelivery(model);
    }

    public void openProblematicOptions() {
        view().openProblematicOptions(model.getJobOrderNo());
    }


}

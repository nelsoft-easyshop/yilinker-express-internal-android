package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentDropoffJobView;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentProblematicJobView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentProblematicJobPresenter extends BasePresenter<JobOrder, ICurrentProblematicJobView> {


    @Override
    protected void updateView() {

        view().setStatusText(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
        view().setDateCreatedText("date");
        view().setDateAcceptedText("date");
        view().setDeliveryAddress(model.getDeliveryAddress());
        view().setItemText(model.getRemarks());
        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));

    }


}

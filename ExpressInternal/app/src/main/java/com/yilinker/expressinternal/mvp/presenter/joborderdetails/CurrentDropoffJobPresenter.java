package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentDropoffJobView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentDropoffJobPresenter extends BasePresenter<JobOrder, ICurrentDropoffJobView> {


    @Override
    protected void updateView() {

        view().setStatusText(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
        view().setDateCreatedText("date");
        view().setDateAcceptedText("date");
        view().setDropoffAddress(model.getDropoffAddress());
        view().setItemText("items");
        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));

    }

    public void buttonOkPressed() {
        view().goBackToList();
    }


}

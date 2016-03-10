package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentDropoffJobView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentDropoffJobPresenter extends BasePresenter<JobOrder, ICurrentDropoffJobView> {

    private static final String CURRENT_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";

    @Override
    protected void updateView() {

        view().setStatusText(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
        view().setDateCreatedText(DateUtility.convertDateToString(model.getDateCreated(), CURRENT_DATE_FORMAT));
        view().setDateAcceptedText(DateUtility.convertDateToString(model.getDateAccepted(), CURRENT_DATE_FORMAT));
        view().setDropoffAddress(model.getDropoffAddress());
        view().setItemText(model.getPackageDescription());
        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));

    }

    public void buttonOkPressed() {
        view().goBackToList();
    }


}

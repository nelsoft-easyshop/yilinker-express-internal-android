package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.yilinker.core.model.Date;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentDropoffJobView;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentProblematicJobView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.List;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentProblematicJobPresenter extends BasePresenter<JobOrder, ICurrentProblematicJobView> {

    private static final String CURRENT_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";

    @Override
    protected void updateView() {

        view().setStatusText(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
        view().setDateCreatedText(DateUtility.convertDateToString(model.getDateCreated(), CURRENT_DATE_FORMAT));
        view().setDateAcceptedText(DateUtility.convertDateToString(model.getDateAccepted(), CURRENT_DATE_FORMAT));
        view().setDeliveryAddress(model.getDeliveryAddress());
        view().setItemText(model.getRemarks());
        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));

    }

    public void onViewImageClick(){

        List<String> imageUrls = model.getProblematicImages();

        if(imageUrls.size() > 0){

            view().showReportedImages(imageUrls);

        }
        else{

            view().showNoImageError();
        }
    }

}

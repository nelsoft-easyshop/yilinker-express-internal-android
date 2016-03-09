package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.IJobDetailsMainView;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class JobDetailsMainPresenter extends BasePresenter<JobOrder, IJobDetailsMainView> {


    @Override
    protected void updateView() {

        String status = model.getStatus();
        if(model.isOpen()){
            view().showOpenDetails(model);
        }
        else if(!model.isOpen() && status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){
            view().showCurrentPickupDetails(model);
        }
        else if(!model.isOpen() && status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)){
            view().showDropoffDetails(model);
        }

    }

    public void onQrButtonClick(){

        view().showQRCodeScreen(model);
    }

    public void onMapButtonClick(){

        view().showNavigationMapScreen(model);
    }

    public void onContactButtonClick(){

        view().showContactScreen(model);
    }

    public void onImageButtonClick(){

        view().showImages(model.getImages());
    }

    public void onPrintButtonClick(){

        view().showPrintScreen(model);
    }
}

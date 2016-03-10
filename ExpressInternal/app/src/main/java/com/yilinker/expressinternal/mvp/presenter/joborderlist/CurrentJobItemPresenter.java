package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.CurrentJobsViewHolder;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.OpenJobsViewHolder;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class CurrentJobItemPresenter extends JobItemPresenter<CurrentJobsViewHolder> {

    @Override
    protected void updateView() {


        view().setWaybillNo(model.getWaybillNo());
        view().setSize(model.getSize());
        view().setStatus(model.getStatus());
        view().setEarning(formatEarning(model.getEarning()));
        view().setAddressLabelText(model.getStatus());
        view().setAddressText(getAddressByStatus(model.getStatus()));

        if(model.getStatus().equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)){

            view().setProblematicTypeText(model.getProblemType());
        }


    }

    public void onClick(){

        view().showDetails(model);
    }

    //TODO Move this method so this can be reuse
    private String formatEarning(double earning){

        return PriceFormatHelper.formatPrice(earning);
    }

    private String formatDateCreated(Date date){

        String format = "dd MMM - hh:mm aa";

        String stringDate = DateUtility.convertDateToString(date, format);

        return stringDate;
    }

    private String getAddressByStatus(String status){

        String address = null;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            address = model.getPickupAddress();
        }
        else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            address = model.getDeliveryAddress();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)){


            address = model.getDropoffAddress();
        }
        else {

//            //TEMP
//            if(model.getType().equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY)){
//
//                address = model.getDeliveryAddress();
//            }
//            else {
//
//                address = model.getPickupAddress();
//            }

        }

        return address;
    }


}

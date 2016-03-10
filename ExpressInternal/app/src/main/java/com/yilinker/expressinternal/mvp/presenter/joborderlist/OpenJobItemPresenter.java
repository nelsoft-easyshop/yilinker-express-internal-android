package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.OpenJobsViewHolder;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class OpenJobItemPresenter extends JobItemPresenter<OpenJobsViewHolder> {

    @Override
    protected void updateView() {

        view().setWaybillNo(model.getWaybillNo());
        view().setSize(model.getSize());
        view().setStatus(model.getStatus());
        view().setEarning(formatEarning(model.getEarning()));
        view().setFromAddress(model.getPickupAddress());
        view().setToAddress(model.getDeliveryAddress());
        view().setDateCreated(formatDateCreated(model.getDateCreated()));


//        if(model.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)) {
//            view().setToAddress(getToAddress(model.getStatus()));
//            view().setFromAddress(model.getBranchName());
//        }
//        else{
//            view().setToAddress(getToAddress(model.getBranchName()));
//            view().setFromAddress(getToAddress(model.getStatus()));
//        }
    }


    private String formatEarning(double earning){

        return PriceFormatHelper.formatPrice(earning);
    }

    private String formatDateCreated(Date date){

        String format = "dd MMM - hh:mm aa";

        String stringDate = DateUtility.convertDateToString(date, format);

        return stringDate;
    }

    public void onClick(){

        view().showDetails(model);
    }

    private String getToAddress(String status){

        String address = null;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            address = model.getPickupAddress();
        }
        else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            address = model.getDeliveryAddress();
        }

        return address;

    }

}

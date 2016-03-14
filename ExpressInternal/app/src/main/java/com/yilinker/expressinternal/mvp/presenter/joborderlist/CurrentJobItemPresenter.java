package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import android.os.Handler;
import android.widget.TextView;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.CurrentJobsViewHolder;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.OpenJobsViewHolder;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
        else{

            view().showForSyncLabel(model.isForSyncing());
            view().setTimeElapsed(convertTimeElapsedToString(model.getDateAccepted()));

        }

    }


    public void onClick(){

        view().showDetails(model);
    }

    public void onTimerTick(){

        if(model.getDateAccepted() != null && !model.getStatus().equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC) && view() != null) {

            view().setTimeElapsed(convertTimeElapsedToString(model.getDateAccepted()));
        }

    }

    private String formatEarning(double earning){

        return PriceFormatHelper.formatPrice(earning);
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

        }else if (status.equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)){

            address = model.getDropoffAddress();
        }

        return address;
    }


    private String getAddressByStatusId(int typeId){

        String address = null;

        if(typeId==2){


        }
        else if (typeId==1){

            address = model.getDeliveryAddress();
        }


        return address;
    }

    private String convertTimeElapsedToString(Date dateAccepted){

        String timeElapsed = null;

            if (dateAccepted == null) {

                timeElapsed = "-";

            } else {


                Calendar calendar = Calendar.getInstance();

                long difference = calendar.getTimeInMillis() - dateAccepted.getTime();

                long secondsInMilli = 1000;
                long minsInMilli = secondsInMilli * 60;
                long hoursInMilli = 60 * minsInMilli;

                long hour =  difference / hoursInMilli;
                difference = difference % hoursInMilli;

                long minute = difference / minsInMilli;
                difference = difference % minsInMilli;

                long second = difference / secondsInMilli;
                difference = difference % secondsInMilli;

                timeElapsed = String.format("%02dh:%02dm:%02ds", hour, minute, second);

            }

        return timeElapsed;
    }
}

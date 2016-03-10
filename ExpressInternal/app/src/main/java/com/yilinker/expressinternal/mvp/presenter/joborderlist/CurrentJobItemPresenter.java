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

import java.util.Calendar;
import java.util.Date;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class CurrentJobItemPresenter extends JobItemPresenter<CurrentJobsViewHolder> {

    protected Handler timerHandler;

    public CurrentJobItemPresenter(){

        timerHandler = new Handler();
    }



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

    @Override
    public void bindView(CurrentJobsViewHolder view) {
        super.bindView(view);

//        if(!model.getStatus().equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC))
//        {
//            startTimer();
//        }
    }

    @Override
    public void unbindView() {

//        if(!model.getStatus().equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC))
//        {
//            stopTimer();
//        }

        super.unbindView();
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

    public void startTimer() {

        timerHandler = new Handler();
        timerHandler.postDelayed(mRunnable, 0);
    }

    public void stopTimer() {

        timerHandler = null;
    }

    //For Timer
    private  Runnable mRunnable = new Runnable() {

        public void run() {

            // if counters are active
            if (!model.getStatus().equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)) {

                if(model.getDateAccepted() != null) {
                    Calendar calendar = Calendar.getInstance();

                    long difference = calendar.getTimeInMillis() - model.getDateAccepted().getTime();
                    Calendar newDate = Calendar.getInstance();
                    newDate.setTimeInMillis(difference);

                    view().setTimeElapsed(String.format("%d:%02d:%02d", newDate.get(Calendar.HOUR), newDate.get(Calendar.MINUTE), newDate.get(Calendar.SECOND)));

                }
            }

            // update every second
            timerHandler.postDelayed(this, 1000);
        }
    };



}

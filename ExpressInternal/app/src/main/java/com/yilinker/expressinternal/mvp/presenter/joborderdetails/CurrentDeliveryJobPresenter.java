package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import android.os.Handler;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentDeliveryJobView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentDeliveryJobPresenter extends BasePresenter<JobOrder, ICurrentDeliveryJobView> implements ICurrentDeliveryJobPresenter{

    private static final String CURRENT_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";

    private boolean isTimerAvailable;
    private Handler timerHandler;

    @Override
    protected void updateView() {

        view().setStatusText(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
        view().setDeliveryAddressText(model.getDeliveryAddress());
        view().setAmountToCollectText(PriceFormatHelper.formatPrice(model.getAmountToCollect()));
        view().setContactNumber(model.getRecipientContactNo());
        view().setShipperName(model.getRecipientName());
        view().setDateCreatedText(DateUtility.convertDateToString(model.getDateCreated(), CURRENT_DATE_FORMAT));
        view().setDateAccepted(getDateAccepted());
        view().setItemText(model.getPackageDescription());
        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));

    }


    private String getDateAccepted(){

        String dateAccepted = null;
        if (model.getDateAccepted() != null){
            if (model.getDateAccepted() != null)
            {
                dateAccepted = DateUtility.convertDateToString(model.getDateAccepted(), CURRENT_DATE_FORMAT);
            }
        }else {
            dateAccepted = "-";
        }

        return dateAccepted;
    }

    public void openDeliveryChecklist() {
        view().openChecklistDelivery(model);
    }

    public void openProblematicOptions() {
        view().openProblematicOptions(model.getJobOrderNo());
    }

    @Override
    public void onPause() {
        stopTimer();
    }

    @Override
    public void startTimer() {

        isTimerAvailable = true;
        timerHandler = new Handler();
        timerHandler.postDelayed(mRunnable,0);
    }

    private void stopTimer(){

        isTimerAvailable = false;
        timerHandler = null;

    }

    private final Runnable mRunnable = new Runnable() {

        public void run() {

            if (isTimerAvailable){

                onTimerTick();
                timerHandler.postDelayed(this, 1000);

            }
        }

    };

    private void onTimerTick(){

        if (model.getDateAccepted()!= null){
            view().setTimeElapsedText(convertTimeElapsedToString(model.getDateAccepted()));
        }

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

            timeElapsed = String.format("%02d:%02d:%02d", hour, minute, second);

        }

        return timeElapsed;
    }

}

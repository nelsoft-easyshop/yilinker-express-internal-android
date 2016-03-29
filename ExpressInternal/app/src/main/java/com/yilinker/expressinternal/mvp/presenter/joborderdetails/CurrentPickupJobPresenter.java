package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import android.os.Handler;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.model.express.internal.ProblematicJobOrder;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentPickupJobView;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.JobsViewHolder;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentPickupJobPresenter extends RequestPresenter<JobOrder, ICurrentPickupJobView> implements ICurrentPickupJobPresenter {

    private final static int OUT_OF_STOCK_REQUEST_CODE = 2000;
    private static final String CURRENT_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";
    private final static String REQUEST_TAG = "request-tag";

    private Handler timerHandler;
    private boolean isTimerAvailable;

    @Override
    protected void updateView() {

        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));
        view().setWaybillNoText(model.getWaybillNo());
        view().setStatusText(model.getStatus());
        view().setDateCreatedText(DateUtility.convertDateToString(model.getDateCreated(), CURRENT_DATE_FORMAT));
        view().setDateAccepted(getDateAccepted());
        view().setAmountToCollectText(PriceFormatHelper.formatPrice(model.getAmountToCollect()));
        view().setConsigneeNameText(model.getRecipientName());
        view().setConsigneeNoText(model.getRecipientContactNo());
        view().setPickupAddressText(model.getPickupAddress());
        view().setItemText(model.getPackageDescription());
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

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case OUT_OF_STOCK_REQUEST_CODE:
                view().showLoader(false);
                view().handleOutOfStockResponse();
                break;

            default:
                break;
        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case OUT_OF_STOCK_REQUEST_CODE:
                view().showLoader(false);
                view().showErrorMessage(message);
                break;

            default:
                break;
        }
    }

    @Override
    public void reportOutOfStock(String problemType, String notes) {

        ProblematicJobOrder report = new ProblematicJobOrder();
        report.setProblemType(problemType);
        report.setJobOrderNo(model.getJobOrderNo());
        report.setNotes(notes);

        requestOutOfStock(report);

    }

    @Override
    public void onPause() {

        stopTimer();

        if(view() != null) {
            List<String> tags = new ArrayList<>();
            tags.add(REQUEST_TAG);
            view().cancelRequests(tags);

        }
    }

    @Override
    public void startTimer() {
        isTimerAvailable = true;
        timerHandler = new Handler();
        timerHandler.postDelayed(mRunnable, 0);

    }

    private void stopTimer(){

        isTimerAvailable = false;
        timerHandler = null;

    }

    private void requestOutOfStock(ProblematicJobOrder report){

        view().showLoader(true);
        Request request = JobOrderAPI.reportProblematic(OUT_OF_STOCK_REQUEST_CODE, report, this);
        request.setTag(REQUEST_TAG);
        view().addToRequestQueue(request);
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

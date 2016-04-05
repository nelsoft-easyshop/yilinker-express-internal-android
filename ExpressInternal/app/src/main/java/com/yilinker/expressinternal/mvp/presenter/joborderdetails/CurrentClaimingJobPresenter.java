package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import android.os.Handler;

import com.android.volley.Request;
import com.yilinker.core.api.express.JobOrderApi;
import com.yilinker.core.model.express.internal.ProblematicJobOrder;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentClaimingJobView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jaybryantc on 4/4/16.
 */
public class CurrentClaimingJobPresenter extends RequestPresenter<JobOrder, ICurrentClaimingJobView> implements ICurrentClaimingJobPresenter {

    private static final String CURRENT_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";

    private static final int REQUEST_CLAIM = 1000;
    private static final int REQUEST_OUT_OF_STOCK = 1001;

    private boolean isTimerAvailable = false;
    private Handler timerHandler;

    @Override
    protected void updateView() {
        view().setStatusText(model.getStatus());
        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));
        view().setWaybillNoText(model.getWaybillNo());
        view().setDateCreatedText(DateUtility.convertDateToString(model.getDateCreated(), CURRENT_DATE_FORMAT));
        view().setDateAcceptedLabel(model.getDateAccepted() == null ?
                "-" : DateUtility.convertDateToString(model.getDateAccepted(), CURRENT_DATE_FORMAT));
        view().setClaimingAddressLabel(model.getPickupAddress());
        view().setContactNumberLabel(model.getShipperContactNo());
        view().setItemLabel(getItems());

        if (!view().ifUpdated(model.getJobOrderNo())) {
            view().showOutdated();
        }

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        view().showLoader(false);

        switch (requestCode) {

            case REQUEST_CLAIM:

                view().goToCompleteScreen(model, false);

                break;

            case REQUEST_OUT_OF_STOCK:

                view().showOutOfStock();

                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        view().showLoader(false);

        switch (requestCode) {

            case REQUEST_CLAIM:

                view().showToast(message);
                doOfflineCompletion();

                break;

            case REQUEST_OUT_OF_STOCK:

                view().showToast(message);

                break;

        }

    }

    @Override
    public void startTimer() {

        isTimerAvailable = true;
        timerHandler = new Handler();
        timerHandler.postDelayed(mRunnable, 0);

    }

    @Override
    public void stopTimer() {

        isTimerAvailable = false;
        timerHandler = null;

    }

    @Override
    public void claimJobOrder() {

        Request request = JobOrderApi.updateStatus(REQUEST_CLAIM, model.getJobOrderNo(),
                JobOrderConstant.JO_CURRENT_DELIVERY, this,
                new ExpressErrorHandler(this, REQUEST_CLAIM));
        request.setTag(REQUEST_CLAIM);
        view().addRequestToQueue(request);
        view().showLoader(true);

    }

    @Override
    public void reportOutOfStock() {

        Request request = JobOrderApi.reportProblematic(REQUEST_OUT_OF_STOCK, getProblematicJobOrder(),
                this, new ExpressErrorHandler(this, REQUEST_OUT_OF_STOCK));
        request.setTag(REQUEST_OUT_OF_STOCK);
        view().addRequestToQueue(request);
        view().showLoader(true);

    }

    @Override
    public void doOfflineCompletion() {

        view().startClaimService(createPackage());
        view().goToCompleteScreen(model, true);

    }

    private Package createPackage() {

        Package pack = new Package();
        pack.setJobOrderNo(model.getJobOrderNo());
        pack.setNewStatus(JobOrderConstant.JO_CURRENT_DELIVERY);
        pack.setIsUpdated(false);

        return pack;
    }

    private ProblematicJobOrder getProblematicJobOrder() {

        ProblematicJobOrder jobOrder = new ProblematicJobOrder();
        jobOrder.setJobOrderNo(model.getJobOrderNo());
        jobOrder.setNotes("");
        jobOrder.setProblemType(JobOrderConstant.PROBLEM_OUT_OF_STOCK);

        return jobOrder;
    }

    private String getItems() {

        if (model.getItems() == null)
            return "";

        StringBuilder builder = new StringBuilder("");

        for (int pos = 0; pos < model.getItems().size(); pos++) {
            builder.append(String.format(pos != model.getItems().size() - 1 ? "%s, " : "%s",
                    model.getItems().get(pos)));
        }

        return builder.toString();
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
            view().setTimeElapsedLabel(convertTimeElapsedToString(model.getDateAccepted()));
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
package com.yilinker.expressinternal.controllers.checklist;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rlcoronado on 13/01/2016.
 */
public class ServiceDeliveryChecklist extends Service implements ResponseHandler {

    private ApplicationClass appClass;

    private RequestQueue requestQueue;
    private SyncDBTransaction dbTransaction;

    private String wayBillNo, jobOrderNo, signature, imageIds, receivedBy, relationship;
    private int rating;
//    private List<String> images;
    private String[] images;
    private boolean isSubmitSignatureDone = false, isSubmitImagesDone = false, isSubmitReceivedByDone = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public ServiceDeliveryChecklist(){}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("RESULT", "Start Service");
        getData(intent);

        requestSubmitReceivedBy();
        requestSubmitSignature();
//        requestSubmitRating();
        requestSubmitImages();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appClass = (ApplicationClass) ApplicationClass.getInstance();
        requestQueue = ApplicationClass.getInstance().getRequestQueue();
        dbTransaction = new SyncDBTransaction(this);
    }

    private void getData(Intent intent){

        wayBillNo = intent.getStringExtra(ActivityChecklist.ARG_WAYBILL_NO);
        jobOrderNo = intent.getStringExtra(ActivityChecklist.ARG_JOBORDER_NO);
        signature = intent.getStringExtra(ActivityChecklist.ARG_SIGNATURE);
        rating = Integer.valueOf(intent.getStringExtra(ActivityChecklist.ARG_RATING));
        images = intent.getStringArrayExtra(ActivityChecklist.ARG_IMAGES);
        receivedBy = intent.getStringExtra(ActivityChecklist.ARG_RECEIVED_BY);
        relationship = intent.getStringExtra(ActivityChecklist.ARG_RELATIONSHIP);

    }

    private void requestSubmitReceivedBy(){
        Request request = JobOrderAPI.submitReceivedBy(ActivityChecklist.REQUEST_RECEIVED_BY,
                jobOrderNo, receivedBy, relationship , this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);
    }

    private void requestSubmitImages() {

//        Request request = JobOrderAPI.uploadJobOrderImages(ActivityChecklist.REQUEST_UPLOAD_IMAGES, wayBillNo, Arrays.asList(images), this);
        Request request = JobOrderAPI.uploadJobOrderImages(ActivityChecklist.REQUEST_UPLOAD_IMAGES, wayBillNo, Arrays.asList(images),ApplicationClass.DELIVERY_IMAGE_TYPE ,this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestSubmitRating() {

        Request request = JobOrderAPI.addRating(ActivityChecklist.REQUEST_SUBMIT_RATING, jobOrderNo, rating, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestSubmitSignature() {

        Request request = JobOrderAPI.uploadSignature(ActivityChecklist.REQUEST_SUBMIT_SIGNATURE, jobOrderNo, signature, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        //do nothing
        switch(requestCode) {

            case ActivityChecklist.REQUEST_SUBMIT_SIGNATURE:
                Log.i("RESULT", "Signature done.");
                isSubmitSignatureDone = true;
                isToStopService();
                break;

            case ActivityChecklist.REQUEST_SUBMIT_RATING:

                Log.i("RESULT", "Rating done.");
                break;

            case ActivityChecklist.REQUEST_UPLOAD_IMAGES:

                Log.i("RESULT", "Images done.");
                isSubmitImagesDone = true;
                isToStopService();
                break;

            case ActivityChecklist.REQUEST_RECEIVED_BY:

                Log.i("RESULT", "Received By done.");
                isSubmitReceivedByDone = true;
                isToStopService();
                break;

        }


    }

    @Override
    public void onFailed(int requestCode, String message) {

        //save to db for syncing

        switch(requestCode) {

            case ActivityChecklist.REQUEST_SUBMIT_SIGNATURE:

                handleFailedSubmitSignature();
                Log.i("RESULT", "Signature done.");
                isSubmitSignatureDone = true;
                isToStopService();
                break;

            case ActivityChecklist.REQUEST_SUBMIT_RATING:

                handleFailedSubmitRating();
                Log.i("RESULT", "Rating done.");

                break;

            case ActivityChecklist.REQUEST_UPLOAD_IMAGES:

                handleFailedUploadImages();
                isSubmitImagesDone = true;
                Log.i("RESULT", "Images done.");
                isToStopService();
                break;

            case ActivityChecklist.REQUEST_RECEIVED_BY:
                handleFailedSubmitReceivedBy();
                isSubmitReceivedByDone = true;
                Log.i("RESULT", "Received By done.");
                isToStopService();
                break;

        }


    }

    private void isToStopService(){
        if (isSubmitSignatureDone && isSubmitImagesDone && isSubmitReceivedByDone){

            Log.i("RESULT", "Stop Service");
            stopSelf();
        }

    }


    private void handleFailedSubmitSignature(){

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivityChecklist.REQUEST_SUBMIT_SIGNATURE);
        request.setKey(String.format("%s%s", jobOrderNo, String.valueOf(ActivityChecklist.REQUEST_SUBMIT_SIGNATURE)));
        request.setId(jobOrderNo);
        request.setData(signature);
        request.setSync(false);

        dbTransaction.add(request);

    }

    private void handleFailedSubmitReceivedBy(){

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivityChecklist.REQUEST_RECEIVED_BY);
        request.setKey(String.format("%s%s", jobOrderNo, String.valueOf(ActivityChecklist.REQUEST_RECEIVED_BY)));
        request.setId(jobOrderNo);
        request.setData(String.format("%s|%s",relationship,receivedBy));
        request.setSync(false);

        dbTransaction.add(request);

    }


    private void handleFailedSubmitRating(){

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivityChecklist.REQUEST_SUBMIT_RATING);
        request.setKey(String.format("%s%s", jobOrderNo, String.valueOf(ActivityChecklist.REQUEST_SUBMIT_RATING)));
        request.setId(jobOrderNo);
        request.setData(String.valueOf(rating));
        request.setSync(false);

        dbTransaction.add(request);

    }

    private void handleFailedUploadImages(){

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivityChecklist.REQUEST_UPLOAD_IMAGES);
        request.setKey(String.format("%s%s", wayBillNo, String.valueOf(ActivityChecklist.REQUEST_UPLOAD_IMAGES)));
        request.setId(wayBillNo);
//        request.setData(String.valueOf(images));
        request.setData(Arrays.toString(images));
        request.setSync(false);

        dbTransaction.add(request);


    }



}

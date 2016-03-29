package com.yilinker.expressinternal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.controllers.sync.ActivitySync2;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.mvp.model.DeliveryPackage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by rlcoronado on 13/01/2016.
 */
public class ServiceDeliveryChecklist extends Service implements ResponseHandler {

    public static final String ARG_DELIVERY_JO = "deliveryPackage";

    private static final int REQUEST_UPDATE_STATUS = 1004;
    private static final int REQUEST_SUBMIT_SIGNATURE = 1005;
    private static final int REQUEST_UPLOAD_IMAGES = 1006;

    private ApplicationClass appClass;

    private RequestQueue requestQueue;
    private SyncDBTransaction dbTransaction;

    private boolean isSubmitSignatureDone = false, isSubmitImagesDone = false;

    private DeliveryPackage deliveryPackage;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public ServiceDeliveryChecklist(){}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        getData(intent);

        if(!deliveryPackage.isUpdated()) {

            requestUpdateStatus();
        }
        else{

            requestSubmitSignature();
            requestSubmitImages();
        }

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

        deliveryPackage = intent.getParcelableExtra(ARG_DELIVERY_JO);

    }

    private void requestUpdateStatus(){

        Request request = JobOrderAPI.updateStatus(REQUEST_UPDATE_STATUS, deliveryPackage.getJobOrderNo(), JobOrderConstant.JO_COMPLETE, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        request.setTag(ApplicationClass.REQUEST_TAG);
        requestQueue.add(request);
    }

    private void requestSubmitImages() {

        String wayBillNo = deliveryPackage.getWaybillNo();
        List<String> images = deliveryPackage.getImages();

        Request request = JobOrderAPI.uploadJobOrderImages(ActivityChecklist.REQUEST_UPLOAD_IMAGES, wayBillNo, images, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestSubmitSignature() {

        String jobOrderNo = deliveryPackage.getJobOrderNo();
        String signature = deliveryPackage.getSignature();

        Request request = JobOrderAPI.uploadSignature(ActivityChecklist.REQUEST_SUBMIT_SIGNATURE, jobOrderNo, signature, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        //do nothing
        switch(requestCode) {

            case REQUEST_UPDATE_STATUS:

                requestSubmitImages();
                requestSubmitSignature();

                break;

            case REQUEST_SUBMIT_SIGNATURE:

                isSubmitSignatureDone = true;
                isToStopService();
                break;

            case REQUEST_UPLOAD_IMAGES:

                isSubmitImagesDone = true;
                isToStopService();
                break;

        }


    }

    @Override
    public void onFailed(int requestCode, String message) {

        //save to db for syncing

        switch(requestCode) {

            case REQUEST_UPDATE_STATUS:

                handleFailedUpdate();
                handleFailedSubmitSignature();
                handleFailedUploadImages();
                isToStopService();

                break;

            case REQUEST_SUBMIT_SIGNATURE:

                handleFailedSubmitSignature();
                isToStopService();
                break;

            case REQUEST_UPLOAD_IMAGES:

                handleFailedUploadImages();
                isToStopService();
                break;

        }


    }

    private void isToStopService(){

        if (isSubmitSignatureDone && isSubmitImagesDone){

            stopSelf();
        }

    }

    private void handleFailedUpdate(){

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivitySync2.TYPE_UPDATE_STATUS);
        request.setKey(String.format("%s%s", deliveryPackage.getJobOrderNo(), String.valueOf(ActivitySync2.TYPE_UPDATE_STATUS)));
        request.setId(deliveryPackage.getJobOrderNo());
        request.setData(JobOrderConstant.JO_COMPLETE);
        request.setSync(false);

        dbTransaction.add(request);
    }


    private void handleFailedSubmitSignature(){

        String jobOrderNo = deliveryPackage.getJobOrderNo();
        String signature = deliveryPackage.getSignature();

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivitySync2.TYPE_SIGNATURE);
        request.setKey(String.format("%s%s", jobOrderNo, String.valueOf(ActivitySync2.TYPE_SIGNATURE)));
        request.setId(jobOrderNo);
        request.setData(signature);
        request.setSync(false);

        dbTransaction.add(request);

        isSubmitSignatureDone = true;

    }


    private void handleFailedUploadImages(){

        String wayBillNo = deliveryPackage.getWaybillNo();
        List<String> imageList = deliveryPackage.getImages();

        String[] images = new String[imageList.size()];

        int i = 0;
        for(String item : imageList){

            images[i] = item;
            i++;
        }

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivitySync2.TYPE_UPLOAD_IMAGE);
        request.setKey(String.format("%s%s", wayBillNo, String.valueOf(ActivitySync2.TYPE_SIGNATURE)));
        request.setId(wayBillNo);
        request.setData(Arrays.toString(images));
        request.setSync(false);

        dbTransaction.add(request);

        isSubmitImagesDone = true;

    }



}

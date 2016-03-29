package com.yilinker.expressinternal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.api.express.JobOrderApi;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.express.internal.request.PackageRequest;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.controllers.confirmpackage.ActivityConfirmPackage;
import com.yilinker.expressinternal.controllers.sync.ActivitySync2;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.model.PackageType;
import com.yilinker.expressinternal.mvp.model.*;
import com.yilinker.expressinternal.mvp.model.Package;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rlcoronado on 13/01/2016.
 */
public class ServicePickupChecklist extends Service implements ResponseHandler {

    public static final String ARG_PACKAGE = "package";

    private static final int REQUEST_UPDATE_STATUS = 1004;
    private static final int REQUEST_CALCULATE_FEE = 1005;

    private ApplicationClass appClass;

    private RequestQueue requestQueue;
    private SyncDBTransaction dbTransaction;

    private Package selectedPackage;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public ServicePickupChecklist() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getData(intent);

        if(selectedPackage.isUpdated()) {

            calculateShippingFee();
        }
        else {

            requestUpdateStatus();

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

    private void getData(Intent intent) {

        selectedPackage = intent.getParcelableExtra(ARG_PACKAGE);

    }

    private void calculateShippingFee() {

        PackageRequest packageRequest = createPackageRequest(selectedPackage);

        Request request = JobOrderApi.calculateShippingFee(REQUEST_CALCULATE_FEE, packageRequest, Boolean.toString(true), this, new ExpressErrorHandler(this, REQUEST_CALCULATE_FEE));

        requestQueue.add(request);

    }

    private void requestUpdateStatus(){

        Request request = JobOrderAPI.updateStatus(REQUEST_UPDATE_STATUS, selectedPackage.getJobOrderNo(), selectedPackage.getNewStatus(), this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        request.setTag(ApplicationClass.REQUEST_TAG);
        requestQueue.add(request);
    }


    @Override
    public void onSuccess(int requestCode, Object object) {

        switch (requestCode) {

            case REQUEST_UPDATE_STATUS:

                calculateShippingFee();
                break;

            case REQUEST_CALCULATE_FEE:

                stopSelf();
                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {

        //save to db for syncing

        switch (requestCode) {

            case REQUEST_UPDATE_STATUS:

                handleFailedUpdate();
                handleFailedCalculation();

                stopSelf();

                break;

            case REQUEST_CALCULATE_FEE:

                handleFailedCalculation();
                stopSelf();

                break;

        }


    }

    private void handleFailedUpdate(){

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivitySync2.TYPE_UPDATE_STATUS);
        request.setKey(String.format("%s%s", selectedPackage.getJobOrderNo(), String.valueOf(ActivitySync2.TYPE_UPDATE_STATUS)));
        request.setId(selectedPackage.getJobOrderNo());
        request.setData(JobOrderConstant.JO_COMPLETE);
        request.setSync(false);

        dbTransaction.add(request);
    }

    private void handleFailedCalculation() {

        List<String> packageData = new ArrayList<>();

        packageData.add(String.valueOf(selectedPackage.getSelectedId()));
        packageData.add(String.valueOf(selectedPackage.getLength()));
        packageData.add(String.valueOf(selectedPackage.getWidth()));
        packageData.add(String.valueOf(selectedPackage.getHeight()));
        packageData.add(String.valueOf(selectedPackage.getWeight()));


        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivitySync2.TYPE_CONFIRM_PACKAGE);
        request.setKey(String.format("%s%s", selectedPackage.getJobOrderNo(),
                String.valueOf(ActivitySync2.TYPE_CONFIRM_PACKAGE)));
        request.setId(selectedPackage.getJobOrderNo());
        request.setData(packageData.toString());
        request.setSync(false);

        dbTransaction.add(request);
    }

    private static PackageRequest createPackageRequest(Package selectedPackage){

        PackageRequest packageRequest = new PackageRequest();

        packageRequest.setPackageTypeId(selectedPackage.getSelectedId());
        packageRequest.setJobOrderNo(selectedPackage.getJobOrderNo());
        packageRequest.setLength(String.valueOf(selectedPackage.getLength()));
        packageRequest.setWidth(String.valueOf(selectedPackage.getWidth()));
        packageRequest.setHeight(String.valueOf(selectedPackage.getHeight()));
        packageRequest.setWeight(String.valueOf(selectedPackage.getWeight()));


        return packageRequest;
    }
}

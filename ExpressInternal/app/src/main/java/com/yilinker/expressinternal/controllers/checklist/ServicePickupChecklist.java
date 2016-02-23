package com.yilinker.expressinternal.controllers.checklist;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.confirmpackage.ActivityConfirmPackage;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.model.PackageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rlcoronado on 13/01/2016.
 */
public class ServicePickupChecklist extends Service implements ResponseHandler {

    private ApplicationClass appClass;

    private RequestQueue requestQueue;
    private SyncDBTransaction dbTransaction;

    private String jobOrderNo;
    private PackageType packageFee;

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
        calculateShippingFee();

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

        jobOrderNo = intent.getStringExtra(ActivityChecklist.ARG_JOBORDER_NO);
        packageFee = intent.getParcelableExtra(ActivityConfirmPackage.ARG_PACKAGE_FEE);

    }

    private void calculateShippingFee() {

        Request request = JobOrderAPI.calculateShippingFee(ActivityConfirmPackage.REQUEST_CODE_CALCULATE_SHIPPING_FEE,
                packageFee.getId(), packageFee.getLength(),
                packageFee.getWidth(), packageFee.getHeight(), packageFee.getWeight(),
                jobOrderNo, "1", this);

        requestQueue.add(request);

    }


    @Override
    public void onSuccess(int requestCode, Object object) {

        //do nothing

    }

    @Override
    public void onFailed(int requestCode, String message) {

        //save to db for syncing

        switch (requestCode) {

            case ActivityChecklist.REQUEST_SUBMIT_SIGNATURE:

                handleFailedCalculation();

                break;

        }


    }

    private void handleFailedCalculation() {

        List<String> packageData = new ArrayList<>();

        packageData.add(String.valueOf(packageFee.getId()));
        packageData.add(packageFee.getLength());
        packageData.add(packageFee.getWidth());
        packageData.add(packageFee.getHeight());
        packageData.add(packageFee.getWeight());

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivityConfirmPackage.REQUEST_CODE_CALCULATE_SHIPPING_FEE);
        request.setKey(String.format("%s%s", jobOrderNo,
                String.valueOf(ActivityConfirmPackage.REQUEST_CODE_CALCULATE_SHIPPING_FEE)));
        request.setId(jobOrderNo);
        request.setData(packageData.toString());
        request.setSync(false);

        dbTransaction.add(request);
    }



}

package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.express.internal.ProblematicJobOrder;
import com.yilinker.core.model.seller.ProductEditDetails;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;

import java.util.Arrays;
import java.util.List;

/**
 * Created by patVillanueva on 7/21/2016.
 */
public class ServiceReportProblematic extends Service implements ResponseHandler {

    private ApplicationClass appClass;
    private RequestQueue requestQueue;
    private SyncDBTransaction dbTransaction;
    private String[] images;
    private String waybillNumber;

    public ServiceReportProblematic(){}

    @Override
    public void onCreate() {
        super.onCreate();

        appClass = (ApplicationClass) ApplicationClass.getInstance();
        requestQueue = ApplicationClass.getInstance().getRequestQueue();
        dbTransaction = new SyncDBTransaction(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("RESULT", "Start Report Problematic Service");
        getData(intent);

        requestUploadProblematicImages();

        return super.onStartCommand(intent, flags, startId);
    }

    private void getData(Intent intent){
        images = intent.getStringArrayExtra(ActivityProblematic.ARG_PROBLEMATIC_IMAGES);
        waybillNumber = intent.getStringExtra(ActivityProblematic.ARG_WAYBILL_NUMBER);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void requestUploadProblematicImages(){
        Request request = JobOrderAPI.uploadJobOrderImages(ActivityProblematic.REQUEST_UPLOAD_PROBLEMATIC_IMAGES, waybillNumber,
                Arrays.asList(images), ApplicationClass.PROBLEMATIC_IMAGE_TYPE, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        switch (requestCode){
            case ActivityProblematic.REQUEST_UPLOAD_PROBLEMATIC_IMAGES:
                Log.i("RESULT", "Successful image upload");
                Log.i("RESULT", "Stop Service");
                stopSelf();
                break;
        }
    }

    @Override
    public void onFailed(int requestCode, String message) {

        switch (requestCode){
            case ActivityProblematic.REQUEST_UPLOAD_PROBLEMATIC_IMAGES:
                handleFailedUploadImage();
                Log.i("RESULT", "Failed image upload");
                Log.i("RESULT", "Stop Service");
                stopSelf();
                break;
        }
    }

    private void handleFailedUploadImage(){

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivityProblematic.REQUEST_UPLOAD_PROBLEMATIC_IMAGES);
        request.setKey(String.format("%s%s", waybillNumber, String.valueOf(ActivityProblematic.REQUEST_UPLOAD_PROBLEMATIC_IMAGES)));
        request.setId(waybillNumber);
        request.setData(Arrays.toString(images));
        request.setSync(false);

        dbTransaction.add(request);

    }

}

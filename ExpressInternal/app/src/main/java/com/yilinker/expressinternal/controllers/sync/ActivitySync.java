package com.yilinker.expressinternal.controllers.sync;

import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.model.express.internal.ProblematicJobOrder;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.controllers.confirmpackage.ActivityConfirmPackage;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityProblematic;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by wagnavu on 2/1/16.
 */
public class ActivitySync extends BaseActivity{

    public static final int REQUEST_SYNC = 600;

    private static final String DB_KEY = "key";

    private SyncDBTransaction dbTransaction;
    private RequestQueue requestQueue;
    private ApplicationClass appClass;

    private Realm realm;
    private List<SyncDBObject> requests;
    private int requestCounter = 0;
//    private AtomicInteger requestCounter ;
    private int numberOfRequest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        hideActionBar();

//        requestCounter = new AtomicInteger(0);
        appClass = (ApplicationClass) ApplicationClass.getInstance();
        dbTransaction = new SyncDBTransaction(this);
        requestQueue = appClass.getRequestQueue();
        realm = Realm.getInstance(this);

        syncDataToServer();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

    }

    @Override
    protected void handleRefreshToken() {

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        handleSyncSuccess(requestCode);

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        handleFailedSync();
    }

    /**
     * Gets all failed request from local db
     * then syncs it accordingly
     */

    private void syncDataToServer() {

        requests = dbTransaction.getAll(SyncDBObject.class);
//
//        requestCounter = 0;
        numberOfRequest = requests.size();


        for (int i = 0; i < requests.size(); i++) {

            SyncDBObject request = requests.get(i);

            if (!request.isSync()) {

                if (request.getRequestType() == ActivityChecklist.REQUEST_RECEIVED_BY){

                    requestSubmitReceivedBy(i, request.getId(), request.getData());

                }else if (request.getRequestType() == ActivityChecklist.REQUEST_SUBMIT_SIGNATURE) {

                    requestSubmitSignature(i, request.getId(), request.getData());

                } else if (request.getRequestType() == ActivityChecklist.REQUEST_SUBMIT_RATING) {

                    requestSubmitRating(i, request.getId(), Integer.valueOf(request.getData()));

                } else if (request.getRequestType() == ActivityChecklist.REQUEST_UPLOAD_IMAGES) {

                    requestSubmitImages(i, request.getId(), request.getData(), ApplicationClass.DELIVERY_IMAGE_TYPE);

                } else if (request.getRequestType() == ActivityChecklist.REQUEST_UPDATE) {

                    requestUpdate(i, request.getId(), request.getData());

                } else if (request.getRequestType() == ActivityConfirmPackage.REQUEST_CODE_CALCULATE_SHIPPING_FEE) {

                    requestCalculateShippingFee(i, request.getId(), request.getData());

                }else if(request.getRequestType() == ActivityProblematic.REQUEST_UPLOAD_PROBLEMATIC_IMAGES){

                    requestSubmitImages(i, request.getId(), request.getData(), ApplicationClass.PROBLEMATIC_IMAGE_TYPE);

                }else if (request.getRequestType() == ActivityProblematic.REQUEST_SUBMIT_REPORT){

                    requestReportProblematic(i, request.getId(), request.getData());
                }

            } else {
                dbTransaction.delete(request);
            }

        }

    }

    /**
     * Functions for Sync Requests
     */

    private void requestSubmitImages(int position, String wayBillNo, String images, int type) {

        images = images.replace("[", "");
        images = images.replace("]", "");

        List<String> imageList = new ArrayList<String>(Arrays.asList(images.split(",")));
        List<String> listImages = new ArrayList<>();
        for(String text : imageList){

            text = text.trim();
//            text = text.replace("\"", "");
//            text = text.substring(1, text.length());

            listImages.add(text);
        }


        Request request = JobOrderAPI.uploadJobOrderImages(position, wayBillNo, listImages, type, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestReportProblematic(int position, String jobOrderNo, String data){

        String notes = data.substring(0,data.indexOf("|"));
        String problemTypeId = data.substring(data.indexOf("|")+1,data.length());

        ProblematicJobOrder report = new ProblematicJobOrder();
        report.setNotes(notes);
        report.setJobOrderNo(jobOrderNo);
        report.setProblemTypeId(Integer.parseInt(problemTypeId));

        Request request = JobOrderAPI.reportProblematicJO(position, report, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);
    }

    private void requestUpdate(int position, String jobOrderNo, String data) {

        String relationship="";
        String recipient="";

        String newStatus = data.substring(0,data.indexOf("|"));
        if (newStatus.equals(JobOrderConstant.JO_COMPLETE)){

            relationship = data.substring(data.indexOf("|")+1,data.indexOf(":"));
            recipient = data.substring(data.indexOf(":")+1, data.length());

        }

        Request request = JobOrderAPI.updateStatus(position, jobOrderNo, newStatus, relationship, recipient, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestSubmitRating(int position, String jobOrderNo, Integer rating) {

        Request request = JobOrderAPI.addRating(position, jobOrderNo, rating, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestSubmitReceivedBy(int position, String jobOrderNo, String data) {

        String relationship = data.substring(0,data.indexOf("|"));
        String receivedBy = data.substring(data.indexOf("|")+1,data.length());

        Request request = JobOrderAPI.submitReceivedBy(position, jobOrderNo, receivedBy, relationship, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }


    private void requestSubmitSignature(int position, String jobOrderNo, String signature) {

        Request request = JobOrderAPI.uploadSignature(position, jobOrderNo, signature, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestCalculateShippingFee(int position, String jobOrderNo, String packageData) {

        packageData = packageData.replace("[", "");
        packageData = packageData.replace("]", "");


        List<String> packageFee = new ArrayList<String>(Arrays.asList(packageData.split(",")));

        String sizeId, length, width, height, weight;

        sizeId = packageFee.get(0);
        length = packageFee.get(1);
        width  = packageFee.get(2);
        height = packageFee.get(3);
        weight = packageFee.get(4);

        Request request = JobOrderAPI.calculateShippingFee(position,
                Integer.valueOf(sizeId), length, width, height, weight, jobOrderNo, "1", this);

        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    /**
     * End function for Sync Requests
     */


    private  void handleSyncSuccess(int position) {

        synchronized (this) {
            requestCounter++;
//        requestCounter.incrementAndGet();

            RealmQuery<SyncDBObject> query = realm.where(SyncDBObject.class);

            query.equalTo(DB_KEY, requests.get(position).getKey());

            SyncDBObject result = query.findFirst();

            realm.beginTransaction();
            result.setSync(true);
            realm.commitTransaction();

            dbTransaction.update(result);

//        appClass.setHasItemsForSyncing(false);

            //TODO Set activity result
            if (requestCounter >= numberOfRequest) {
//        if (requestCounter.get() >= numberOfRequest) {

                purgeData();

                setResult(RESULT_OK);
                finish();
            }

        }

    }

    private  void handleFailedSync() {

        synchronized (this) {

            requestCounter++;
//        requestCounter.incrementAndGet();

            if (requestCounter >= numberOfRequest) {

                purgeData();

                setResult(RESULT_CANCELED);
                finish();
            }
        }

    }

    private void purgeData(){


        HashMap<String, Object> mapQuery = new HashMap<>();
        mapQuery.put("isSync", true);

        dbTransaction.deleteAll(SyncDBObject.class, mapQuery);

        //Clean cache. Remove images
        if(!appClass.hasItemsForSyncing())
            ImageUtility.clearCachedImages(this);

    }
}

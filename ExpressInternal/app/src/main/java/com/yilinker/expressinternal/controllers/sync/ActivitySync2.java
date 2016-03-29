package com.yilinker.expressinternal.controllers.sync;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.controllers.confirmpackage.ActivityConfirmPackage;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by wagnavu on 2/1/16.
 */
public class ActivitySync2 extends BaseActivity{

    public static final int TYPE_UPLOAD_IMAGE = 2000;
    public static final int TYPE_UPDATE_STATUS = 2001;
    public static final int TYPE_SIGNATURE = 2002;
    public static final int TYPE_CONFIRM_PACKAGE = 2003;

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

        numberOfRequest = requests.size();


        for (int i = 0; i < requests.size(); i++) {

            SyncDBObject request = requests.get(i);

            if (!request.isSync()) {

                int type = request.getRequestType();

                switch (type){

                    case  TYPE_SIGNATURE:

                        requestSubmitSignature(i, request.getId(), request.getData());
                        break;

                    case  TYPE_UPLOAD_IMAGE:

                        requestSubmitImages(i, request.getId(), request.getData());
                        break;

                    case TYPE_UPDATE_STATUS:

                        requestUpdate(i, request.getId(), request.getData());
                        break;

                    case TYPE_CONFIRM_PACKAGE:

                        requestCalculateShippingFee(i, request.getId(), request.getData());
                        break;
                    }

            } else {
                dbTransaction.delete(request);
            }

        }

    }

    /**
     * Functions for Sync Requests
     */

    private void requestSubmitImages(int position, String wayBillNo, String images) {

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


        Request request = JobOrderAPI.uploadJobOrderImages(position, wayBillNo, listImages, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestUpdate(int position, String jobOrderNo, String newStatus) {


        Request request = JobOrderAPI.updateStatus(position, jobOrderNo, newStatus, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestSubmitRating(int position, String jobOrderNo, Integer rating) {

        Request request = JobOrderAPI.addRating(position, jobOrderNo, rating, this);
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

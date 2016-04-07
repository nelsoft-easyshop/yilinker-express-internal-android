package com.yilinker.expressinternal.mvp.presenter.sync;

import android.os.Handler;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.sync.ISyncView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jaybryantc on 4/6/16.
 */
public class SyncPresenter extends RequestPresenter<List<SyncDBObject>, ISyncView> {

    public static final int TYPE_UPLOAD_IMAGE = 2000;
    public static final int TYPE_UPDATE_STATUS = 2001;
    public static final int TYPE_SIGNATURE = 2002;
    public static final int TYPE_CONFIRM_PACKAGE = 2003;

    private Handler handler;

    private int progress = 0;
    private int success = 0;
    private int failed = 0;

    @Override
    protected void updateView() {

        view().showNotification();

    }

    public void startRequest(SyncDBObject object, int position) {

        view().updateSyncStatus(true);

        if (!object.isSync()) {

            int type = object.getRequestType();

            switch (type){

                case  TYPE_SIGNATURE:

                    requestSubmitSignature(object, position);

                    break;

                case  TYPE_UPLOAD_IMAGE:

                    requestSubmitImages(object, position);

                    break;

                case TYPE_UPDATE_STATUS:

                    requestUpdate(object, position);

                    break;

                case TYPE_CONFIRM_PACKAGE:

                    requestCalculateShippingFee(object, position);

                    break;
            }

        } else {

            view().deleteObject(object);
//            view().setProgress(progress++);
            updateProgress();

        }

    }


    public void startRequests() {

        handler = new Handler();
        handler.postDelayed(runnable, 1000);

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

//        SyncDBObject syncDBObject = model.get(progress);
//        syncDBObject.setSync(true);
//        view().updateObject(syncDBObject);

        success++;
        updateProgress();

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        failed++;
        updateProgress();

    }

    private void updateProgress() {

        if (progress++ != model.size()-1) {

            //add request
            view().setProgress(progress);
            startRequest(model.get(progress), progress);

        } else {

            //all requests done
            view().setProgress(progress);
            view().purgeData();
            view().showResults(success, failed);
            view().updateSyncStatus(false);
            view().stopService();

        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            progress++;

            view().setProgress(progress);

            if (progress == 100) {

                view().showResults(100, 0);
                handler.removeCallbacks(runnable);

            } else {

                handler.postDelayed(runnable, 200);

            }

        }
    };

    private void requestSubmitSignature(SyncDBObject object, int position) {

        Request request = JobOrderAPI.uploadSignature(position, object.getId(), object.getData(), this);
        request.setTag(position);

        view().addToRequestQueue(request);

    }

    private void requestSubmitImages(SyncDBObject object, int position) {

        String images = object.getData().replace("[", "");
        images = images.replace("]", "");

        List<String> imageList = new ArrayList<String>(Arrays.asList(images.split(",")));
        List<String> listImages = new ArrayList<>();
        for(String text : imageList){
            text = text.trim();
            listImages.add(text);
        }

        Request request = JobOrderAPI.uploadJobOrderImages(position, object.getId(), listImages, this);
        request.setTag(position);

        view().addToRequestQueue(request);

    }

    private void requestUpdate(SyncDBObject object, int position) {

        Request request = JobOrderAPI.updateStatus(position, object.getId(), object.getData(), this);
        request.setTag(position);

        view().addToRequestQueue(request);

    }

    private void requestCalculateShippingFee(SyncDBObject object, int position) {

        String packageData = object.getData().replace("[", "");
        packageData = packageData.replace("]", "");


        List<String> packageFee = new ArrayList<String>(Arrays.asList(packageData.split(",")));

        String sizeId, length, width, height, weight;

        sizeId = packageFee.get(0);
        length = packageFee.get(1);
        width  = packageFee.get(2);
        height = packageFee.get(3);
        weight = packageFee.get(4);

        Request request = JobOrderAPI.calculateShippingFee(position,
                Integer.valueOf(sizeId), length, width, height, weight, object.getId(), "1", this);

        request.setTag(position);

        view().addToRequestQueue(request);

    }

}

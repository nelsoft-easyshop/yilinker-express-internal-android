package com.yilinker.expressinternal.mvp.presenter.checklist;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistDeliveryView;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistPickupView;

import java.io.File;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class ChecklistDeliveryPresenter extends ChecklistBasePresenter<IChecklistDeliveryView> {

    public static final int TYPE_VALID_ID = 1;
    public static final int TYPE_RECIPIENT_PIC = 2;

    private static final int REQUEST_UPDATE = 1003;

    private ChecklistItem currentItem;

    private Uri photoUri;


    public void onValidIdClick(ChecklistItem checklistItem){

        currentItem = checklistItem;

        if(checklistItem.getAttachedItem() == null){

            createImageFile();
            view().launchCamera(photoUri, TYPE_VALID_ID);

        }
        else {

            //TODO Launch gallery
        }


    }

    public void onValidIdResult(){

//        String validImage = photoUri.toString();

        currentItem.setIsChecked(true);
//        currentItem.setAttachedItem(validImage);

        view().updateItem(currentItem);
    }


    @Override
    public void onCompleteButtonClick() {


    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_UPDATE:

                view().showMessage(object.toString());
                handleCompleteRequest();
                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case REQUEST_UPDATE:

                handleFailedUpdate();
                break;

        }
    }



    private void requestCompletePickup(String newStatus){

        Request request = JobOrderAPI.updateStatus(REQUEST_UPDATE, model.getJobOrderNo(), newStatus, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        view().addRequest(request);
    }

    private void handleCompleteRequest(){

        view().goToMainScreen();
    }

    private void handleFailedUpdate(){


    }

    private void createImageFile(){

        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));

        try {

            File folder = new File(Environment.getExternalStorageDirectory().toString(), ImageUtility.TEMP_IMAGE_FOLDER);

            if (!folder.exists()) folder.mkdirs();

            File outputFile = new File(String.format("%s/%s/%s.jpeg", Environment.getExternalStorageDirectory().toString(), ImageUtility.TEMP_IMAGE_FOLDER, tempFileName));

            photoUri = Uri.fromFile(outputFile);

        }
        catch (Exception e){

            Log.d("Image Exception", e.getMessage());

        }

    }
}

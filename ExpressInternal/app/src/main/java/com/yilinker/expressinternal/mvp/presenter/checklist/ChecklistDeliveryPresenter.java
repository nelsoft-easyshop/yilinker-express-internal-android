package com.yilinker.expressinternal.mvp.presenter.checklist;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.mvp.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.model.DeliveryPackage;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistDeliveryView;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistPickupView;

import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class ChecklistDeliveryPresenter extends ChecklistBasePresenter<IChecklistDeliveryView> {

    public static final int TYPE_VALID_ID = 1;
    public static final int TYPE_RECIPIENT_PIC = 2;

    private static final int REQUEST_UPDATE = 1003;

    private Uri photoUri;
    private String recipientPicture;
    private String validId;
    private String signature;

    public void onValidIdClick(ChecklistItem checklistItem, String pageName){

        handleImageItemClick(checklistItem, TYPE_VALID_ID, pageName);

    }

    public void onRecipientPictureClick(ChecklistItem checklistItem, String pageName){

        handleImageItemClick(checklistItem, TYPE_RECIPIENT_PIC, pageName);
    }

    public void onValidIdResult(){

        validId = photoUri.toString();
        setPhotoUri();
    }

    public void onValidIdRetake(String newUri){

        validId = newUri;
        updatePhoto(newUri);
    }


    public void onRecipientPictureResult(){

        recipientPicture = photoUri.toString();
        setPhotoUri();
    }

    public void onRecipientPictureRetake(String newUri){

        recipientPicture = newUri;
        updatePhoto(newUri);
    }

    public void onSignatureImageClick(ChecklistItem item){

        currentItem = item;

        view().showSignatureScreen(signature);
    }

    public void onSignatureResult(String data){

        signature = data;

        currentItem.setAttachedItem(data);
        currentItem.setIsChecked(true);

        view().updateItem(currentItem);
    }

    @Override
    public void onCompleteButtonClick() {

        view().showScreenLoader(true);
        requestCompleteDelivery();
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

                view().showMessage(message);
                handleFailedUpdate();
                break;
        }
    }


    private void requestCompleteDelivery(){

        Request request = JobOrderAPI.updateStatus(REQUEST_UPDATE, model.getJobOrderNo(), JobOrderConstant.JO_COMPLETE, this);
        request.setTag(REQUEST_TAG);

        view().addRequest(request);
    }

    private void handleCompleteRequest(){

        DeliveryPackage deliveryPackage = createDeliveryPackage(true);

        view().startDeliveryService(deliveryPackage);
        view().goToCompleteScreen(model);
    }

    private void handleFailedUpdate(){

        DeliveryPackage deliveryPackage = createDeliveryPackage(false);

        view().startDeliveryService(deliveryPackage);
        view().goToMainScreen();

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

    private void updatePhoto(String newUri){

        photoUri = Uri.parse(newUri);

        currentItem.setAttachedItem(newUri);

        view().updateItem(currentItem);

    }

    private void setPhotoUri(){

        String image = photoUri.toString();

        currentItem.setIsChecked(true);
        currentItem.setAttachedItem(image);

        view().updateItem(currentItem);
    }

    private void handleImageItemClick(ChecklistItem item, int type, String pageName){

        currentItem = item;

        if(item.getAttachedItem() == null){

            createImageFile();
            view().launchCamera(photoUri, type);

        }
        else {

            List<String> images = new ArrayList<>();
            images.add(item.getAttachedItem());

            view().showCaptureImageScreen(images, type, pageName);
        }
    }

    private DeliveryPackage createDeliveryPackage(boolean isUpdated){

        DeliveryPackage deliveryPackage = new DeliveryPackage();

        deliveryPackage.setJobOrderNo(model.getJobOrderNo());
        deliveryPackage.setWaybillNo(model.getWaybillNo());
        deliveryPackage.setIsUpdated(isUpdated);
        deliveryPackage.setSignature(signature);

        List<String> images = new ArrayList<>();
        images.add(compressImage(validId));
        images.add(compressImage(recipientPicture));
        deliveryPackage.setImages(images);

        return deliveryPackage;
    }

    private String compressImage(String imagePath){

        Uri uri = Uri.parse(imagePath);

        String compressedImage = view().compressImage(uri.getEncodedPath());

        return compressedImage;
    }
}

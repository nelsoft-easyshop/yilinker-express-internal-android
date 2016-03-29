package com.yilinker.expressinternal.mvp.presenter.checklist;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.*;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistPickupView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class ChecklistPickupPresenter extends ChecklistBasePresenter<IChecklistPickupView> {

    private static final int REQUEST_UPDATE = 1003;

    private Package selectedPackage;

    public void onConfirmPackageClick(ChecklistItem item){

        currentItem = item;

        view().showConfirmPackageScreen(getPackage());
    }

    public void onConfirmPackageResult(Package selectedPackage){

        this.selectedPackage = selectedPackage;

        currentItem.setIsChecked(true);

        view().updateItem(currentItem);

    }

    public void onSelectStatus(String newStatus){

        view().showScreenLoader(true);
        selectedPackage.setNewStatus(newStatus);
        requestCompletePickup(newStatus);
    }

    private Package getPackage(){

        if(selectedPackage == null){

            selectedPackage = new Package();
            selectedPackage.setJobOrderNo(model.getJobOrderNo());

        }

        return this.selectedPackage;
    }

    @Override
    public void onCompleteButtonClick() {

        String riderCode = view().getRiderAreaCode();

        if(riderCode.equalsIgnoreCase(model.getAreaCode())){

            view().showScreenLoader(true);
            requestCompletePickup(JobOrderConstant.JO_CURRENT_DROPOFF);
        }
        else {

            view().showStatusOptionDialog();
        }

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
        request.setTag(REQUEST_TAG);

        view().addRequest(request);
    }

    private void handleCompleteRequest(){

        selectedPackage.setIsUpdated(true);
        view().startPickupService(selectedPackage);
        view().goToMainScreen();
    }

    private void handleFailedUpdate(){

        selectedPackage.setIsUpdated(false);
        view().startPickupService(selectedPackage);

    }
}

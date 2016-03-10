package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.android.volley.Request;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.IOpenJobDetailsView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class OpenJobDetailsPresenter extends RequestPresenter<JobOrder, IOpenJobDetailsView> {

    private static final String TAG_REQUEST = "request";
    private static final int REQUEST_ACCEPT_JOB = 1000;

    @Override
    protected void updateView() {

        view().setConsigneeContactNo(model.getContactNo());
        view().setConsigneeNameText(model.getRecipient());
        view().setDeliveryAddressText(model.getDeliveryAddress());
        view().setPickupAddressText(model.getPickupAddress());
        view().setShipperNameText("Shipper Name");
        view().setShipperContactNo("099999999");
        view().setDateCreatedText("Date Created");
        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));
        view().setStatusText(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
    }

    public void requestAcceptJob() {

        Request request = RiderAPI.acceptJobOrder(REQUEST_ACCEPT_JOB, model.getJobOrderNo(), this);
        request.setTag(TAG_REQUEST);

        view().addRequestToQueue(request);
    }

    public void handleNegativeButtonClick() {

        view().goBackToList();

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode) {

            case REQUEST_ACCEPT_JOB:
                view().showSuccessMessage();
                view().goBackToList();
                break;
        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        view().showErrorMessage(message);

    }
}

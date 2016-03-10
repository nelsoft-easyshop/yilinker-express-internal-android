package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.model.express.internal.ProblematicJobOrder;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICurrentPickupJobView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class CurrentPickupJobPresenter extends RequestPresenter<JobOrder, ICurrentPickupJobView> implements ICurrentPickupJobPresenter {

    private final static int OUT_OF_STOCK_REQUEST_CODE = 2000;
    private static final String CURRENT_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";
    private final static String REQUEST_TAG = "request-tag";

    @Override
    protected void updateView() {

        view().setEarningText(PriceFormatHelper.formatPrice(model.getEarning()));
        view().setWaybillNoText(model.getWaybillNo());
        view().setStatusText(model.getStatus());
        view().setDateCreatedText(DateUtility.convertDateToString(model.getDateCreated(), CURRENT_DATE_FORMAT));
        view().setAmountToCollectText(PriceFormatHelper.formatPrice(model.getEarning()));
        view().setConsigneeNameText(model.getRecipientName());
        view().setConsigneeNoText(model.getRecipientContactNo());
        view().setPickupAddressText(model.getPickupAddress());
        view().setItemText(model.getPackageDescription());
    }



    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case OUT_OF_STOCK_REQUEST_CODE:
                view().showLoader(false);
                view().handleOutOfStockResponse();
                break;

            default:
                break;
        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case OUT_OF_STOCK_REQUEST_CODE:
                view().showLoader(false);
                view().showErrorMessage(message);
                break;

            default:
                break;
        }
    }

    @Override
    public void reportOutOfStock(String problemType, String notes) {

        ProblematicJobOrder report = new ProblematicJobOrder();
        report.setProblemType(problemType);
        report.setJobOrderNo(model.getJobOrderNo());
        report.setNotes(notes);

        requestOutOfStock(report);

    }

    @Override
    public void onPause() {

        if(view() != null) {
            List<String> tags = new ArrayList<>();
            tags.add(REQUEST_TAG);
            view().cancelRequests(tags);
        }
    }

    private void requestOutOfStock(ProblematicJobOrder report){

        view().showLoader(true);
        Request request = JobOrderAPI.reportProblematic(OUT_OF_STOCK_REQUEST_CODE, report, this);
        request.setTag(REQUEST_TAG);
        view().addToRequestQueue(request);
    }
}

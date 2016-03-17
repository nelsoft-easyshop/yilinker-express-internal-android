package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ICompleteJODetailsView;

/**
 * Created by Patrick on 3/17/2016.
 */
public class CompleteJODetailsPresenter extends RequestPresenter<JobOrder, ICompleteJODetailsView> implements ICompleteJoDetailsPresenter {

    private final static int GET_JO_DETAILS_REQUEST_CODE = 2001;
    private final static String GET_JO_DETAILS_TAG = "jo-details";

    @Override
    protected void updateView() {
        super.updateView();

        /***TODO add methods here for setting values to views **/

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case GET_JO_DETAILS_REQUEST_CODE:
                handleJODetails(object);
                view().showLoader(false);
                break;

            default:
                break;
        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case GET_JO_DETAILS_REQUEST_CODE:
                view().showErrorMessage(message);
                view().showLoader(false);
                break;

            default:
                break;
        }
    }

    @Override
    public void requestJODetails(String jobOrderNumber) {

        Request request = JobOrderAPI.getJobOrderDetailsByJONumber(GET_JO_DETAILS_REQUEST_CODE, jobOrderNumber, this);
        request.setTag(GET_JO_DETAILS_TAG);
        view().addRequestToQueue(request);
        view().showLoader(true);
    }

    private void handleJODetails(Object object){
        model = new JobOrder((com.yilinker.core.model.express.internal.JobOrder) object);
        updateView();
    }
}

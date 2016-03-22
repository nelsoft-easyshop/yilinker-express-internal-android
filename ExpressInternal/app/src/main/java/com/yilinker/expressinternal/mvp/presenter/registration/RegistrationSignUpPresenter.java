package com.yilinker.expressinternal.mvp.presenter.registration;

import com.android.volley.Request;
import com.yilinker.core.api.express.RegistrationApi;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.presenter.registration.IRegistrationSignUpPresenter;
import com.yilinker.expressinternal.mvp.view.registration.IActivityRegistrationSignUpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public class RegistrationSignUpPresenter extends RequestPresenter<Object,IActivityRegistrationSignUpView>
        implements IRegistrationSignUpPresenter {

    private final static int GET_VERIFICATION_CODE_REQUEST_CODE = 2000;
    private final static String VERIFICATION_CODE_REQUEST_TAG = "verification-code";

    @Override
    protected void updateView() {
        view().onSignUpClick(true);
    }

    @Override
    public void validateInput(String mobileNumber, String savedMobileNumber) {
        if (mobileNumber.length()<10){
            view().showErrorMessage();
        }else{
//            updateView();
            if (savedMobileNumber!=null){

                if (savedMobileNumber.equals(mobileNumber)){
                    view().onSignUpClick(false);

                }else {
//                    requestVerificationCode(String.format("0%s",mobileNumber));
                    view().onSignUpClick(true);

                }

            }else {
//                requestVerificationCode(String.format("0%s",mobileNumber));
                view().onSignUpClick(true);
            }
        }
    }

    @Override
    public void onPause() {
        view().cancelRequests(getRequestTags());
    }

    private List<String> getRequestTags(){
        List<String> tags = new ArrayList<>();
        tags.add(VERIFICATION_CODE_REQUEST_TAG);

        return tags;
    }

    private void requestVerificationCode(String mobileNumber){
        view().showLoader(true);
        Request request = RegistrationApi.getVerificationCode(GET_VERIFICATION_CODE_REQUEST_CODE, mobileNumber, this, new ExpressErrorHandler(this,GET_VERIFICATION_CODE_REQUEST_CODE));
        request.setTag(VERIFICATION_CODE_REQUEST_TAG);
        view().addRequestToQueue(request);

    }


    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case GET_VERIFICATION_CODE_REQUEST_CODE:
                view().showLoader(false);
//                updateView();
                view().onSignUpClick(true);
                break;

            default:
                break;

        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case GET_VERIFICATION_CODE_REQUEST_CODE:
                view().showLoader(false);
                view().showValidationError(message);
                break;

            default:
                break;

        }

    }

}

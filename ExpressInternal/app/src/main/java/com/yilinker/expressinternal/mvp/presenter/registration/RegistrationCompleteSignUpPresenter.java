package com.yilinker.expressinternal.mvp.presenter.registration;

import android.content.res.TypedArray;

import com.android.volley.Request;
import com.yilinker.core.api.express.RegistrationApi;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.presenter.registration.IRegistrationCompleteSignUpPresenter;
import com.yilinker.expressinternal.mvp.view.registration.IActivityRegistrationCompleteSignUpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public class RegistrationCompleteSignUpPresenter extends RequestPresenter<Object, IActivityRegistrationCompleteSignUpView>
        implements IRegistrationCompleteSignUpPresenter, ResponseHandler{

    private final static int SIGN_UP_REQUEST_CODE = 2001;
    private final static String SIGN_UP_REQUEST_TAG = "sign-up";

    private String[] request_tags = {SIGN_UP_REQUEST_TAG};

    @Override
    protected void updateView() {
        super.updateView();
    }

    @Override
    public void validateInputs(String mobileNumber, String password, String confirmPassword,String accessToken) {

        if (password.isEmpty()){
            view().showValidationError(1);

        }else if (confirmPassword.isEmpty()){
            view().showValidationError(2);

        }else if (!password.equals(confirmPassword)){
            view().showValidationError(2);

        }else {
            requestSignUp(mobileNumber, password, accessToken);
        }
    }

    @Override
    public void handleSignUpResponse(String message) {
        view().handleSignUpResponse(message);
    }

    @Override
    public void onPause() {
        view().cancelRequests(getRequestTags());
    }


    private List<String> getRequestTags(){
        List<String> lists = new ArrayList<>();
        for (String item : request_tags){
            lists.add(item);
        }
        return lists;
    }

    private void requestSignUp(String mobileNumber, String password, String accessToken){
        //TODO call request here
        Request request = RegistrationApi.submitRegistration(SIGN_UP_REQUEST_CODE, mobileNumber, password,accessToken, this,new ExpressErrorHandler(this,SIGN_UP_REQUEST_CODE));
        request.setTag(SIGN_UP_REQUEST_TAG);
        view().addRequest(request);
        view().showLoader(true);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case SIGN_UP_REQUEST_CODE:
                handleSignUpResponse(object.toString());
                view().showLoader(false);
                break;

        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case SIGN_UP_REQUEST_CODE:
                view().showErrorMessage(message);
                view().showLoader(false);
                break;

        }
    }
}

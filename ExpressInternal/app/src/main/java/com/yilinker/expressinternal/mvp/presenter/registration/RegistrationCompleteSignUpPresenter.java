package com.yilinker.expressinternal.mvp.presenter.registration;

import com.android.volley.Request;
import com.yilinker.core.api.express.RegistrationApi;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.presenter.registration.IRegistrationCompleteSignUpPresenter;
import com.yilinker.expressinternal.mvp.view.registration.IActivityRegistrationCompleteSignUpView;

/**
 * Created by Patrick on 3/8/2016.
 */
public class RegistrationCompleteSignUpPresenter extends RequestPresenter<Object, IActivityRegistrationCompleteSignUpView>
        implements IRegistrationCompleteSignUpPresenter, ResponseHandler{

    private final static int SIGN_UP_REQUEST_CODE = 2001;

    @Override
    protected void updateView() {
        super.updateView();
    }

    @Override
    public void validateInputs(String password, String confirmPassword) {

        if (password.isEmpty()){
            view().showErrorMessage("password is empty");

        }else if (confirmPassword.isEmpty()){
            view().showErrorMessage("confirm password is empty");

        }else if (!password.equals(confirmPassword)){
            view().showErrorMessage("password is not equal");

        }else {
            requestSignUp();
        }
    }

    @Override
    public void handleSignUpResponse(String message) {
        view().handleSignUpResponse(message);
    }

    private void requestSignUp(){
        //TODO call request here
//        Request request = RegistrationApi.submitRegistration(SIGN_UP_REQUEST_CODE,this,new ExpressErrorHandler(this,SIGN_UP_REQUEST_CODE));
//        view().addRequest(request);
//        view().showLoader(true);
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

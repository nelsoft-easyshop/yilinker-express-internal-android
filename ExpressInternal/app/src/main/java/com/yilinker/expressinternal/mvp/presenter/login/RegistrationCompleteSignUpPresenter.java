package com.yilinker.expressinternal.mvp.presenter.login;

import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;

/**
 * Created by Patrick on 3/8/2016.
 */
public class RegistrationCompleteSignUpPresenter extends RequestPresenter<Object, IActivityRegistrationCompleteSignUpView>
        implements IRegistrationCompleteSignUpPresenter , ResponseHandler{

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
            view().showLoader(true);
            requestSignUp();
        }
    }

    @Override
    public void handleSignUpResponse(String message) {
        view().handleSignUpResponse(message);
    }

    private void requestSignUp(){
        //TODO call request here
//        view().addRequest();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case SIGN_UP_REQUEST_CODE:
                handleSignUpResponse(object.toString());
                break;

        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case SIGN_UP_REQUEST_CODE:
                handleSignUpResponse(message);
                break;

        }
    }
}

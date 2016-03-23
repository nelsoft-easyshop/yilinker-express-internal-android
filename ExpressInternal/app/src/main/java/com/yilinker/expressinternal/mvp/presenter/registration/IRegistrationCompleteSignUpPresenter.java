package com.yilinker.expressinternal.mvp.presenter.registration;

/**
 * Created by Patrick on 3/8/2016.
 */
public interface IRegistrationCompleteSignUpPresenter {

    public void validateInputs(String mobileNumber, String password, String confirmPassword);
    public void handleSignUpResponse(String message);
    public void onPause();

}

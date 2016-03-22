package com.yilinker.expressinternal.mvp.view.registration;

import com.yilinker.expressinternal.mvp.view.RequestBaseView;

/**
 * Created by Patrick on 3/8/2016.
 */
public interface IActivityRegistrationSignUpView extends RequestBaseView {

    public void onSignUpClick(boolean isNewNumber);
    public void onSignInClick();
    public void showErrorMessage();
    public void showValidationError(String errorMessage);
    public void saveMobileNumber(String mobileNumber);
    public void setAccessToken(String accessToken);
}

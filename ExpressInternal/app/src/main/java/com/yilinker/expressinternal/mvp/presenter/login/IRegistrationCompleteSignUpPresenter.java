package com.yilinker.expressinternal.mvp.presenter.login;

/**
 * Created by Patrick on 3/8/2016.
 */
public interface IRegistrationCompleteSignUpPresenter {

    public void validateInputs(String password, String confirmPassword);
    public void handleSignUpResponse(String message);

}

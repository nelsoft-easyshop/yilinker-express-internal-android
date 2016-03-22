package com.yilinker.expressinternal.mvp.presenter.registration;


/**
 * Created by Patrick on 3/8/2016.
 */
public interface IRegistrationVerificationCodePresenter {

    public void getVerificationCode(String mobileNumber, String accessToken);
    public void validateInput(String code, String mobileNumber, String accessToken);
    public void onPause();
    public void getRemainingTime(String remainingTime);

}

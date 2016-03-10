package com.yilinker.expressinternal.mvp.presenter.registration;


/**
 * Created by Patrick on 3/8/2016.
 */
public interface IRegistrationVerificationCodePresenter {

    public void getVerificationCode();
    public void validateInput(String inputCode);
    public void onPause();


}

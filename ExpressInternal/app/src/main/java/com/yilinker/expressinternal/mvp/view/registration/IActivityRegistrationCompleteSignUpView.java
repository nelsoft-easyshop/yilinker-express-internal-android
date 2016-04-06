package com.yilinker.expressinternal.mvp.view.registration;

import com.android.volley.Request;
import com.yilinker.expressinternal.mvp.view.base.RequestBaseView;

/**
 * Created by Patrick on 3/8/2016.
 */
public interface IActivityRegistrationCompleteSignUpView extends RequestBaseView {

    public void addRequest(Request request);
    public void showLoader(boolean isToShow);
    public void showErrorMessage(String errorMessage);
    public void showValidationError(int Code);
    public void handleSignUpResponse(String message);

}

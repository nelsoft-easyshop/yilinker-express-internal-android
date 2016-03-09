package com.yilinker.expressinternal.mvp.presenter.login;

import com.android.volley.Request;

import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public interface IActivityRegistrationCompleteSignUpView {

    public void addRequest(Request request);
    public void cancelRequest(List<String> requestTags);
    public void showLoader(boolean isToShow);
    public void showErrorMessage(String errorMessage);
    public void handleSignUpResponse(String message);

}

package com.yilinker.expressinternal.mvp.view.registration;

import com.android.volley.Request;

import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public interface IActivityRegistrationVerificationCodeView {

    public void handleGetVerificationCodeResponse(String message);
    public void showVerifyLoader(boolean isToShow);
    public void showGetVerificationLoader(boolean isToShow);
    public void showErrorMessage(boolean isToShow, String errorMessage);
    public void handleVerifyResponse(String message);
    public void addRequest(Request request);
    public void cancelRequest(List<String> requestTags);
    public void saveCurrentTime(String currentTime);
    public void setRemainingTime(String remainingTime);
}

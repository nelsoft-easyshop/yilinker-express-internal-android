package com.yilinker.expressinternal.mvp.presenter.reportproblematic;

import android.net.Uri;

import com.yilinker.expressinternal.mvp.model.ProblematicType;

import java.security.PublicKey;

/**
 * Created by patrick-villanueva on 3/29/2016.
 */
public interface IReportProblematicFormPresenter {

    public void onCreate();
    public void goToImages();
    public void addImageGallery(Uri photoUri);
    public void setJobOrderNo(String jobOrderNo);
    public void setSelectedProblematicType(ProblematicType type);
    public void launchCamera();
    public void submitReport(String remarks);
    public void onPause();
}

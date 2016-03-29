package com.yilinker.expressinternal.mvp.view.reportproblematic;

import android.app.Fragment;

import com.yilinker.expressinternal.mvp.model.ProblematicType;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public interface IReportProblematicView {

    public void replaceFragment(Fragment fragment);
    public void goToConfirmProblematic(ProblematicType problematicType, String jobOrderNo);
}

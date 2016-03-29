package com.yilinker.expressinternal.mvp.presenter.reportproblematic;

import com.yilinker.expressinternal.mvp.model.ProblematicType;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public interface IReportProblematicPresenter {

    public void goToConfirmProblematic(ProblematicType type);
    public void setJobOrderNumber(String jobOrderNo);
}

package com.yilinker.expressinternal.mvp.presenter.reportproblematic;

import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.reportproblematic.IReportProblematicView;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public class ReportProblematicPresenter extends BasePresenter<ProblematicType,IReportProblematicView> implements  IReportProblematicPresenter{

    private String jobOrderNo;

    @Override
    protected void updateView() {

    }


    @Override
    public void goToConfirmProblematic(ProblematicType type) {
        view().goToConfirmProblematic(type, jobOrderNo);
    }

    @Override
    public void setJobOrderNumber(String jobOrderNo) {
        this.jobOrderNo = jobOrderNo;
    }
}

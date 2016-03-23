package com.yilinker.expressinternal.mvp.presenter.checklist;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.RequestBaseView;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistMainView;
import com.yilinker.expressinternal.mvp.view.confirmpackage.IConfirmPackageView;

/**
 * Created by J.Bauti on 3/22/16.
 */
public class ChecklistMainPresenter extends BasePresenter<JobOrder, IChecklistMainView> {


    @Override
    protected void updateView() {

        view().setItemText(model.getPackageDescription());
        view().setStatusTitle(model.getStatus());
        view().setWaybillNoText(model.getWaybillNo());
        view().setActionBarColor(model.getStatus());
    }

    public void onCreate(JobOrder jobOrder){

        setModel(jobOrder);
    }
}

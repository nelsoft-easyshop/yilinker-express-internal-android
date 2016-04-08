package com.yilinker.expressinternal.mvp.presenter.checklist;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistMainView;

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

        view().showChecklistView(jobOrder.getStatus(), jobOrder);
    }
}

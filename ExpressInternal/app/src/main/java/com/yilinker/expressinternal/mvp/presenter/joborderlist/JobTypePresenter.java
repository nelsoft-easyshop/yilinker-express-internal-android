package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.JobType;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.JobTypeViewHolder;

/**
 * Created by J.Bautista on 3/4/16.
 */
public class JobTypePresenter extends BasePresenter<JobType, JobTypeViewHolder> {


    @Override
    protected void updateView() {

        view().setLabel(model.getLabel());
        view().setIsChecked(model.isChecked());
    }

    public void onClick(){

        model.setIsChecked(!model.isChecked());
        view().resetViewModel(model);
    }
}

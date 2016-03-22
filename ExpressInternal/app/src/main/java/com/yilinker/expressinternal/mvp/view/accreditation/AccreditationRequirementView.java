package com.yilinker.expressinternal.mvp.view.accreditation;

import android.view.View;

import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.presenter.accreditation.AccreditationRequirementPresenter;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobItemPresenter;

/**
 * Created by wagnavu on 3/16/16.
 */
public abstract class AccreditationRequirementView<P extends AccreditationRequirementPresenter>  extends BaseViewHolder<P> {

    private OnDataUpdateListener listener;

    public AccreditationRequirementView(View itemView, OnDataUpdateListener listener) {
        super(itemView);

        this.listener = listener;
    }

    public abstract void setLabelText(String label);

    public void notifyDataChanged(AccreditationRequirement item){

        listener.onDataUpdate(item);
    }
}

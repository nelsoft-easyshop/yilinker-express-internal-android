package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import android.view.View;

import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.CurrentJobItemPresenter;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.OpenJobItemPresenter;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class CurrentJobsViewHolder extends JobsViewHolder<CurrentJobItemPresenter> implements ICurrentJobView{


    public CurrentJobsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setStatus(String status) {

    }

    @Override
    public void setWaybillNo(String waybillNo) {

    }

    @Override
    public void setEarning(String earning) {

    }

    @Override
    public void setSize(String size) {

    }
}

package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.map.IJobListMapView;

import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class JobListMapPresenter extends BasePresenter<List<JobOrder>, IJobListMapView> {

    @Override
    protected void updateView() {

        view().loadJobOrderList(model);

    }


}

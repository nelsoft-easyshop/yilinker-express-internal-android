package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.IJobListView;

import java.util.List;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class JobListPresenter extends BasePresenter<List<JobOrder>, IJobListView> {

    @Override
    protected void updateView() {

        view().loadJobOrderList(model);
    }


}

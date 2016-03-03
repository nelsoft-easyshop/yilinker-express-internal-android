package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.IJobListMainView;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class JobListMainPresenter extends RequestPresenter<List<JobOrder>, IJobListMainView> {

    @Override
    protected void updateView() {


    }

    public void onTabItemClicked(String jobType){


    }

    public void onFilterChanged(String[] filters){


    }

    public void onSearchWaybill(String waybillNo){


    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);


    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);


    }

}

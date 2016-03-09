package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import com.yilinker.expressinternal.model.JobOrder;

import java.util.List;

/**
 * Created by J.Bautista on 3/1/16.
 */
public interface IJobListView {

    public void loadJobOrderList(List<JobOrder> jobOrders);
    public void showJobOrderDetails(JobOrder joborder);

}

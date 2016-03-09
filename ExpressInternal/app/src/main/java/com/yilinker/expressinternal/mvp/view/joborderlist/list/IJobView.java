package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import com.yilinker.expressinternal.model.JobOrder;

/**
 * Created by J.Bautista on 3/3/16.
 */
public interface IJobView {

    public void setStatus(String status);
    public void setWaybillNo(String waybillNo);
    public void setEarning(String earning);
    public void setSize(String size);
    public void showDetails(JobOrder jobOrder);
}

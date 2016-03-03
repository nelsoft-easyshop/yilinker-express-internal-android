package com.yilinker.expressinternal.mvp.view.joborderlist;

import com.yilinker.expressinternal.model.JobOrder;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IJobListMainView {

    public void reloadList(List<JobOrder> jobOrders);
    public void setResultCountText(String text);
    public void showLoader(boolean isVisible);

}

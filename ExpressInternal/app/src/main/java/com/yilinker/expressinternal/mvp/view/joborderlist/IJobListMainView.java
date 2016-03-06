package com.yilinker.expressinternal.mvp.view.joborderlist;

import com.android.volley.Request;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.TabItem;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IJobListMainView {

    public void reloadList(List<JobOrder> jobOrders);
    public void setResultCountText(String text);
    public void showLoader(boolean isVisible);
    public void showListView(List<JobOrder> jobOrders);
    public void showMapView(List<JobOrder> jobOrders);
    public void loadTabs(List<TabItem> tabs);
    public void changeSelectedTab(TabItem previousTab, TabItem currentTab);
    public void switchView(List<JobOrder> jobOrders);
    public void onFilterClicked(int position, boolean isChecked);
    public void addRequest(Request request);

}

package com.yilinker.expressinternal.mvp.view.joborderlist;

import com.android.volley.Request;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.JobType;
import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.view.RequestBaseView;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IJobListMainView extends RequestBaseView {

    public void reloadList(List<JobOrder> jobOrders);
    public void setResultCountText(String text);
    public void showLoader(boolean isVisible);
    public void showListView(List<JobOrder> jobOrders);
    public void showMapView(List<JobOrder> jobOrders);
    public void loadJobTabs(List<TabItem> tabs);
    public void loadJobTypes(List<JobType> jobTypes);
    public void changeSelectedTab(TabItem previousTab, TabItem currentTab);
    public void switchView(List<JobOrder> jobOrders);
    public void addRequest(Request request);
    public void showErrorMessage(String message);
    public String getSyncDictionary();
    public boolean isFilterByArea();
    public void setMoreJobTypesCount(int moreJobTypesCount);

}

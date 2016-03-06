package com.yilinker.expressinternal.mvp.view.mainScreen;


import com.android.volley.Request;
import com.yilinker.expressinternal.mvp.model.TabItem;

import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 *
 * Modified by J.Bautista on 3/2/2016
 */
public interface IMainView {

    public void loadTabs(List<TabItem> tabs);
    public void changeSelectedTab(TabItem previousTab, TabItem currentTab);
    public void replaceFragment(int selectedTab);

}

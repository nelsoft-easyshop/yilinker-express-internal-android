package com.yilinker.expressinternal.mvp.view.mainScreen;


import android.app.Fragment;
import android.os.Bundle;

import com.yilinker.expressinternal.mvp.model.MainTab;

import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 *
 * Modified by J.Bautista on 3/2/2016
 */
public interface IMainView {

    public void loadTabs(List<MainTab> tabs);
    public void changeSelectedTab(MainTab previousTab, MainTab currentTab);
    public void replaceFragment(int selectedTab);

}

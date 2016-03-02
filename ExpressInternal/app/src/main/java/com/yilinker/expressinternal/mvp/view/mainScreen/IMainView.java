package com.yilinker.expressinternal.mvp.view.mainScreen;


import android.app.Fragment;

import com.yilinker.expressinternal.mvp.model.MainTab;

import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public interface IMainView {

    public void initViews();
    public void replaceFragment(Fragment fragment);
    public void setUpTabs();
    public void loadTabs(List<MainTab> tabs);
    public void updateTabs();

}

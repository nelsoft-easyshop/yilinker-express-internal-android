package com.yilinker.expressinternal.mvp.presenter.mainScreen;

import android.content.res.TypedArray;

/**
 * Created by Patrick on 3/1/2016.
 *
 * Modified by J.Bautista on 3/2/2016
 */
public interface IMainScreenPresenter {

    public void onInitializeTabs(String[] tabTitles, int[] tabIcons, int[] selectedTabIcons);
    public void onTabSelected(int position);
    public void setAvailableIndicators();

}

package com.yilinker.expressinternal.mvp.presenter.mainScreen;

import android.content.res.TypedArray;

/**
 * Created by Patrick on 3/1/2016.
 */
public interface IMainScreenPresenter {

    public void setUpMainTabs(String[] tabItems, TypedArray icons);
    public void setSelectedTab(int position);
}

package com.yilinker.expressinternal.mvp.presenter.mainScreen;

import android.content.res.TypedArray;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.model.MainTab;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;
import com.yilinker.expressinternal.mvp.view.mainScreen.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public class MainScreenPresenter extends BasePresenter<Object, ActivityMain> implements IMainScreenPresenter{

    private IMainView view;
    private List<MainTab> tabs;

    @Override
    protected void updateView() {

    }


    @Override
    public void setUpMainTabs(String[] tabItems, TypedArray icons) {
        //TODO add the tab items and icons to model
        tabs = new ArrayList<>();
        view.loadTabs(tabs);
    }

    @Override
    public void setSelectedTab(int position) {
        //TODO update the value of previous and current tab
        view.loadTabs(tabs);
    }
}

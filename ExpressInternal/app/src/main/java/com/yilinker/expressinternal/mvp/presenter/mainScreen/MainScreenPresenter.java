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
public class MainScreenPresenter extends BasePresenter<List<MainTab>, IMainView> implements IMainScreenPresenter{

    private List<MainTab> tabs;

    @Override
    protected void updateView() {


    }

    @Override
    public void onInitializeTabs(String[] tabTitles, int[] tabIcons, int[] selectedTabIcons) {

        tabs = new ArrayList<>();
        MainTab tab = null;

        for(int i =0; i < tabTitles.length; i ++){

            tab = new MainTab();
            tab.setId(i);
            tab.setResourceIcon(tabIcons[i]);
            tab.setSelected(i == 0);
            tab.setTitle(tabTitles[i]);
            tab.setSelectedIcon(selectedTabIcons[i]);

            tabs.add(tab);
        }

        view().loadTabs(tabs);

    }

    @Override
    public void onTabSelected(int position) {


    }
}

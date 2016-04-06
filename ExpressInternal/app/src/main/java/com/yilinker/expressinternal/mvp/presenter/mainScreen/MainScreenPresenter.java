package com.yilinker.expressinternal.mvp.presenter.mainScreen;

import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.mainScreen.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public class MainScreenPresenter extends BasePresenter<List<TabItem>, IMainView> implements IMainScreenPresenter{

    private List<TabItem> tabs;
    private int previousTab = 0;

    @Override
    protected void updateView() {

        view().replaceFragment(previousTab);

    }

    @Override
    public void onInitializeTabs(String[] tabTitles, int[] tabIcons, int[] selectedTabIcons) {

        tabs = new ArrayList<>();
        TabItem tab = null;

        for(int i =0; i < tabTitles.length; i ++){

            tab = new TabItem();
            tab.setId(i);
            tab.setResourceIcon(tabIcons[i]);
            tab.setSelected(i == 0);
            tab.setTitle(tabTitles[i]);
            tab.setSelectedIcon(selectedTabIcons[i]);

            tabs.add(tab);
        }

        view().loadTabs(tabs);
        updateView();

    }

    @Override
    public void onTabSelected(int position) {

        //Unselect previous tab
        TabItem previous = tabs.get(previousTab);
        previous.setSelected(false);

        //Select current tab
        TabItem current = tabs.get(position);
        current.setSelected(true);

        previousTab = position;

        view().changeSelectedTab(previous, current);

        updateView();
    }
}

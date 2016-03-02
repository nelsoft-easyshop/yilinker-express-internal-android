package com.yilinker.expressinternal.mvp.presenter.mainScreen;

import com.yilinker.expressinternal.mvp.model.MainTab;
import com.yilinker.expressinternal.mvp.view.mainScreen.IMainView;

import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */

//TODO add parameter for the object and view
public class MainTabPresenter extends BasePresenter<> implements IMainTabPresenter {

    IMainView view;


    @Override
    protected void updateView() {

        //TODO notify the adapter
        view.updateTabs();
    }

    @Override
    public void updateTabs(List<MainTab> tabs) {
        //TODO set/update the content/status of the tabs here
        updateView();
    }

}

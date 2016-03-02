package com.yilinker.expressinternal.mvp.presenter.mainScreen;

import android.view.View;

import com.yilinker.expressinternal.mvp.model.MainTab;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.mainScreen.MainTabViewHolder;

/**
 * Created by Patrick on 3/1/2016.
 */
public class MainTabPresenter extends BasePresenter<MainTab, MainTabViewHolder> implements View.OnClickListener {

    @Override
    protected void updateView() {

        view().setIcon(model.getResourceIcon());
        view().setTitle(model.getTitle());
        view().setSelected(model.isSelected());
    }

    @Override
    public void onClick(View v) {


    }
}

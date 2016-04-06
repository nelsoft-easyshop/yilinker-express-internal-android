package com.yilinker.expressinternal.mvp.presenter.mainScreen;

import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.mainScreen.MainTabViewHolder;

/**
 * Created by Patrick on 3/1/2016.
 */
public class MainTabPresenter extends BasePresenter<TabItem, MainTabViewHolder> {



    @Override
    protected void updateView() {

        view().setIcon(model.isSelected() ? model.getSelectedIcon() : model.getResourceIcon());
        view().setTitle(model.getTitle());
        view().setSelected(model.isSelected());
    }

    public void onClick(){

        view().showSelected(model);
    }

}

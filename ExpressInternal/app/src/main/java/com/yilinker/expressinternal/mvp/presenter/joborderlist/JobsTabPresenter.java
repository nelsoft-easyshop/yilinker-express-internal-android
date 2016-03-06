package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.JobsTabViewHolder;
import com.yilinker.expressinternal.mvp.view.mainScreen.MainTabViewHolder;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class JobsTabPresenter extends BasePresenter<TabItem, JobsTabViewHolder> {


    @Override
    protected void updateView() {

        view().setTitle(model.getTitle());
        view().setSelected(model.isSelected());
    }

    public void onClick(){

        view().showSelected(model);
    }
}

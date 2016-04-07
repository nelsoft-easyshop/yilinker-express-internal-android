package com.yilinker.expressinternal.mvp.presenter.tools;

import com.yilinker.expressinternal.mvp.model.Tools;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.tools.ToolsViewHolder;

/**
 * Created by rlcoronado on 01/03/2016.
 */
public class ToolsPresenter extends BasePresenter<Tools, ToolsViewHolder> {

    @Override
    protected void updateView() {

        view().setIcon(model.getResourceIcon());
        view().setTitle(model.getTitle());
        view().setWarningIcon(model.getWarningResourceId());

    }

    public void onClick(){

        view().showSelected(model);

    }


}

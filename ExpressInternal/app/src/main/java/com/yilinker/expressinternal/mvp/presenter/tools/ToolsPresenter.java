package com.yilinker.expressinternal.mvp.presenter.tools;

import com.yilinker.expressinternal.mvp.model.Tools;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.tools.FragmentTools;
import com.yilinker.expressinternal.mvp.view.tools.ToolsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rlcoronado on 01/03/2016.
 */
public class ToolsPresenter extends BasePresenter<Tools, ToolsViewHolder> {

    @Override
    protected void updateView() {

        view().setIcon(model.getResourceIcon());
        view().setTitle(model.getTitle());
    }

    public void onClick(){

        view().showSelected(model);
    }


}

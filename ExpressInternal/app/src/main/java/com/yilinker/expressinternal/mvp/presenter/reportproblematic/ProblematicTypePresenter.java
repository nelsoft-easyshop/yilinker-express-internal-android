package com.yilinker.expressinternal.mvp.presenter.reportproblematic;

import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.reportproblematic.ProblematicTypeViewHolder;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public class ProblematicTypePresenter extends BasePresenter<ProblematicType, ProblematicTypeViewHolder> implements IProblematicTypePresenter{

    @Override
    protected void updateView() {
        view().setProblematicType(model.getType());
    }

    @Override
    public void onProblematicItemClick() {
        view().onProblematicItemClick(model);
    }
}

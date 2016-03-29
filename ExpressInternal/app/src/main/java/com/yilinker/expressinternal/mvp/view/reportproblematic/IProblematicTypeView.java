package com.yilinker.expressinternal.mvp.view.reportproblematic;

import com.yilinker.expressinternal.mvp.model.ProblematicType;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public interface IProblematicTypeView {

    public void setProblematicType(String type);
    public void onProblematicItemClick(ProblematicType type);
}

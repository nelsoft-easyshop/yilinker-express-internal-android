package com.yilinker.expressinternal.mvp.view.tools;

import com.yilinker.expressinternal.mvp.model.Tools;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IMainToolsView {

    void loadTabs(List<Tools> tools);
    void openActivity(int selectedActivity);

}

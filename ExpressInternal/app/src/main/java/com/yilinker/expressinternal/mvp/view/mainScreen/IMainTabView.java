package com.yilinker.expressinternal.mvp.view.mainScreen;

import com.yilinker.expressinternal.mvp.model.MainTab;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IMainTabView {

    public void setTitle(String title);
    public void setIcon(int resourceIcon);
    public void setSelected(boolean isSelected);
    public void showSelected(MainTab tab);

}

package com.yilinker.expressinternal.mvp.view.base;

import com.yilinker.expressinternal.mvp.model.TabItem;

/**
 * Created by J.Bautista on 3/3/16.
 */
public interface ITabView {

    public void setTitle(String title);
    public void setIcon(int resourceIcon);
    public void setSelected(boolean isSelected);
    public void showSelected(TabItem tab);
}

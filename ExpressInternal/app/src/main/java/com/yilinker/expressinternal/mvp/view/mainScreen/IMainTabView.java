package com.yilinker.expressinternal.mvp.view.mainScreen;

import com.yilinker.expressinternal.mvp.model.TabItem;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IMainTabView {

    public void setTitle(String title);
    public void setIcon(int resourceIcon);
    public void setSelected(boolean isSelected);
    public void showSelected(TabItem tab);
    public void showIndicator(boolean show);
    public void setIndicator(int resource);

}

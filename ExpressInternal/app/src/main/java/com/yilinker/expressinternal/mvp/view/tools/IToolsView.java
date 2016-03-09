package com.yilinker.expressinternal.mvp.view.tools;

import com.yilinker.expressinternal.mvp.model.Tools;

import java.util.List;

/**
 * Created by rlcoronado on 01/03/2016.
 */
public interface IToolsView {

    void setTitle(String title);
    void setIcon(int resourceIcon);
    void showSelected(Tools tool);
}

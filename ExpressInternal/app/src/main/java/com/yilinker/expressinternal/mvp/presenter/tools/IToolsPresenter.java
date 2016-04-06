package com.yilinker.expressinternal.mvp.presenter.tools;

import com.yilinker.expressinternal.mvp.model.Tools;

import java.util.List;

/**
 * Created by rlcoronado on 01/03/2016.
 */
public interface IToolsPresenter {

    List<Tools> createTools(String[] toolTitles, int[] toolIcons);
    void onToolSelected(int position);
//    void hasItemsForSyncing(boolean hasForSyncing);
//    void openActivitySync(boolean hasForSyncing);
    void setIndicator(int position, int resource);
    void changeItemTitle(int position, String title);

}

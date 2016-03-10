package com.yilinker.expressinternal.mvp.presenter.tools;

/**
 * Created by rlcoronado on 01/03/2016.
 */
public interface IToolsPresenter {

    void initializeToolsModel(String[] toolTitles, int[] toolIcons);
    void onToolSelected(int position);
    void hasItemsForSyncing(boolean hasForSyncing);
    void openActivitySync(boolean hasForSyncing);
}

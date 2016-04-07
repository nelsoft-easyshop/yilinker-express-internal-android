package com.yilinker.expressinternal.mvp.view.tools;

import com.yilinker.expressinternal.mvp.model.Tools;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IMainToolsView {

    void loadTabs(List<Tools> tools);
    void openActivity(int selectedActivity);
//    void enableSyncButton(boolean b);
//    void openActivitySyncing();
    void initializeReceivers();
    void showNoItemsForSyncingMessage();
    void setItemIndicatorResourceId(Tools tools);
    void setItemTitle(Tools tools);
    void startSyncing();
    boolean syncable();
    void updateItem(Tools tools);
    void showNoInternetConnection();

}

package com.yilinker.expressinternal.mvp.presenter.tools;

import com.yilinker.expressinternal.mvp.model.Tools;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.tools.IMainToolsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public class MainToolsPresenter extends BasePresenter<List<Tools>, IMainToolsView> implements IToolsPresenter {

//    private int activity;
//    private List<Tools> tools;

    @Override
    protected void updateView() {

//        view().openActivity(activity);
        view().loadTabs(model);
        view().updateSyncItem();

    }

    @Override
    public List<Tools> createTools(String[] toolTitles, int[] toolIcons) {

        List<Tools> tools = new ArrayList<>();
        Tools tool;

        for(int i =0; i < toolTitles.length; i ++){

            tool = new Tools();
            tool.setId(i);
            tool.setResourceIcon(toolIcons[i]);
            tool.setSelected(i == 0);
            tool.setTitle(toolTitles[i]);
            tool.setWarningResourceId(0);

            tools.add(tool);
        }


        return tools;
//        view().loadTabs(tools);

    }

    @Override
    public void onToolSelected(int position) {

//        activity = position;
//        updateView();
        view().openActivity(position);

    }

//    @Override
//    public void hasItemsForSyncing(boolean hasForSyncing) {
//
//        view().enableSyncButton(hasForSyncing);
//
//    }

//    @Override
//    public void openActivitySync(boolean hasForSyncing) {
//
//        if(hasForSyncing) {
//            view().openActivitySyncing();
//        } else {
//            view().showNoItemsForSyncingMessage();
//        }
//    }

    @Override
    public void setIndicator(int position, int resource) {

        model.get(position).setWarningResourceId(resource);

    }

    @Override
    public void changeItemTitle(int position, String title) {

        model.get(position).setTitle(title);

    }

}

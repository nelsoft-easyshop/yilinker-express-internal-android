package com.yilinker.expressinternal.mvp.presenter.tools;

import com.yilinker.expressinternal.mvp.model.MainTab;
import com.yilinker.expressinternal.mvp.model.Tools;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.mainScreen.MainTabViewHolder;
import com.yilinker.expressinternal.mvp.view.tools.IMainToolsView;
import com.yilinker.expressinternal.mvp.view.tools.IToolsView;
import com.yilinker.expressinternal.mvp.view.tools.ToolsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public class MainToolsPresenter extends BasePresenter<List<Tools>, IMainToolsView> implements IToolsPresenter {

    private int activity;

    @Override
    protected void updateView() {

        view().openActivity(activity);

    }

    private List<Tools> tools;

    @Override
    public void initializeToolsModel(String[] toolTitles, int[] toolIcons) {

        tools = new ArrayList<>();
        Tools tool = null;

        for(int i =0; i < toolTitles.length; i ++){

            tool = new Tools();
            tool.setId(i);
            tool.setResourceIcon(toolIcons[i]);
            tool.setSelected(i == 0);
            tool.setTitle(toolTitles[i]);

            tools.add(tool);
        }

        view().loadTabs(tools);

    }

    @Override
    public void onToolSelected(int position) {

        activity = position;
        updateView();
    }

}

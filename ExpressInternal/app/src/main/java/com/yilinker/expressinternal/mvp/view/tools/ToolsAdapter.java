package com.yilinker.expressinternal.mvp.view.tools;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.Tools;
import com.yilinker.expressinternal.mvp.presenter.tools.ToolsPresenter;

/**
 * Created by rlcoronado on 03/03/2016.
 */
public class ToolsAdapter extends ListRecyclerViewAdapter<Tools, ToolsPresenter, ToolsViewHolder> {

    private TabItemClickListener clickListener;

    public ToolsAdapter(TabItemClickListener clickListener){

        this.clickListener = clickListener;

    }




    @NonNull
    @Override
    protected ToolsPresenter createPresenter(@NonNull Tools model) {

        ToolsPresenter presenter = new ToolsPresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull Tools model) {

        return model.getId();

    }

    @Override
    public ToolsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ToolsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_tools, parent, false), clickListener);
    }
}

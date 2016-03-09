package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.adapter.TabRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobsTabPresenter;
import com.yilinker.expressinternal.mvp.presenter.mainScreen.MainTabPresenter;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class JobsTabAdapter extends TabRecyclerViewAdapter<TabItem, JobsTabPresenter, JobsTabViewHolder> {

    private TabItemClickListener clickListener;

    public JobsTabAdapter(int resourceId, TabItemClickListener clickListener) {
        super(resourceId);

        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    protected JobsTabPresenter createPresenter(@NonNull TabItem model) {

        JobsTabPresenter presenter = new JobsTabPresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull TabItem model) {

        return model.getId();
    }

    @Override
    public JobsTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(getResourceId(), parent, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(getWidth(), ViewGroup.LayoutParams.MATCH_PARENT, 1));

        return new JobsTabViewHolder(view, clickListener);
    }
}

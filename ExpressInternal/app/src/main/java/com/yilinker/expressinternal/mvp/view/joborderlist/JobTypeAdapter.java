package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.JobType;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobTypePresenter;

/**
 * Created by J.Bautista on 3/4/16.
 */
public class JobTypeAdapter extends ListRecyclerViewAdapter<JobType, JobTypePresenter, JobTypeViewHolder>{

    private RecyclerViewClickListener listener;

    public JobTypeAdapter(RecyclerViewClickListener listener){

        this.listener = listener;
    }

    @NonNull
    @Override
    protected JobTypePresenter createPresenter(@NonNull JobType model) {

        JobTypePresenter presenter = new JobTypePresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull JobType model) {

        return model.getId();
    }

    @Override
    public JobTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_jobtype_item, parent, false);

        return new JobTypeViewHolder(view, listener);
    }
}

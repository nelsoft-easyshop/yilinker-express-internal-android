package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.CurrentJobItemPresenter;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobItemPresenter;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.OpenJobItemPresenter;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class JobsAdapter extends ListRecyclerViewAdapter<JobOrder, JobItemPresenter, JobsViewHolder<JobItemPresenter>>{

    private RecyclerViewClickListener listener;
    private int resourceId;

    public JobsAdapter(int resourceId , RecyclerViewClickListener listener){

        this.resourceId = resourceId;
        this.listener = listener;

    }

    @NonNull
    @Override
    protected JobItemPresenter createPresenter(@NonNull JobOrder model) {

        JobItemPresenter presenter = null;

        if(model.isOpen())
        {
            presenter = new OpenJobItemPresenter();
        }
        else if(!model.isOpen() && model.getStatus().equals("Problematic")) //TODO Change this
        {
            presenter = new CurrentJobItemPresenter();
        }
        else {

            presenter = new CurrentJobItemPresenter();
        }


        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull JobOrder model) {

        return model.getId();
    }

    @Override
    public JobsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        switch (viewType){

            case R.layout.layout_job_order_list_open_2:

                return new OpenJobsViewHolder(view, listener);

            case R.layout.layout_job_order_list_current_2:

                return new CurrentJobsViewHolder(view, listener);

            case R.layout.layout_job_order_list_problematic:

                return new CurrentJobsViewHolder(view, listener);

            default:

                return new OpenJobsViewHolder(view, listener);
        }


    }

    @Override
    public int getItemViewType(int position) {

        JobOrder model = getItem(position);
        String status = model.getStatus();
        int type = 0;

        if(model.isOpen()){

            type = R.layout.layout_job_order_list_open_2;

        }
        else if (!model.isOpen() && model.getStatus().equals("Problematic")){

            type = R.layout.layout_job_order_list_problematic;
        }
        else {

            type = R.layout.layout_job_order_list_current_2;
        }

        return type;
    }
}

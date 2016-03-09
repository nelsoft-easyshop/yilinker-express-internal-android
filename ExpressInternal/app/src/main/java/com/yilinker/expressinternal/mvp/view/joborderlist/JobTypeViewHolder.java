package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.JobType;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobTypePresenter;

/**
 * Created by J.Bautista on 3/4/16.
 */
public class JobTypeViewHolder extends BaseViewHolder<JobTypePresenter> implements View.OnClickListener {

    private TextView tvLabel;
    private RecyclerViewClickListener listener;

    public JobTypeViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        this.listener = listener;

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        presenter.onClick();
    }

    public void setLabel(String label){

        tvLabel.setText(label);
    }

    public void setIsChecked(boolean isChecked){

        tvLabel.setSelected(isChecked);
    }

    public void resetViewModel(JobType type){

        tvLabel.setSelected(type.isChecked());
        listener.onItemClick(type.getId(), type);
    }

}

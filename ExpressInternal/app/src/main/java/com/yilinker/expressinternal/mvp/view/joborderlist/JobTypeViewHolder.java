package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobTypePresenter;

/**
 * Created by J.Bautista on 3/4/16.
 */
public class JobTypeViewHolder extends BaseViewHolder<JobTypePresenter> implements View.OnClickListener {

    private TextView tvLabel;

    public JobTypeViewHolder(View itemView) {
        super(itemView);

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);

        tvLabel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


    }

    public void setLabel(String label){

        tvLabel.setText(label);
    }

    public void setIsChecked(boolean isChecked){

        tvLabel.setSelected(isChecked);
    }

}

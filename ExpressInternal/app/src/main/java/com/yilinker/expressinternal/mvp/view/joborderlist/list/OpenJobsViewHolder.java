package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.OpenJobItemPresenter;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class OpenJobsViewHolder extends JobsViewHolder<OpenJobItemPresenter> implements IOpenJobView {

    private TextView tvStatus;
    private TextView tvWaybillNo;
    private TextView tvDateCreated;
    private TextView tvEarning;
    private TextView tvFromAddress;
    private TextView tvToAddress;
    private TextView tvDistance;
    private TextView tvSize;


    public OpenJobsViewHolder(View itemView) {
        super(itemView);

        tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        tvWaybillNo = (TextView) itemView.findViewById(R.id.tvWaybillNo);
        tvDateCreated = (TextView) itemView.findViewById(R.id.tvDateCreated);
        tvEarning = (TextView) itemView.findViewById(R.id.tvEarning);
        tvFromAddress = (TextView) itemView.findViewById(R.id.tvFromAddress);
        tvToAddress = (TextView) itemView.findViewById(R.id.tvToAddress);
        tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
        tvSize = (TextView) itemView.findViewById(R.id.tvSize);

    }

    @Override
    public void setDateCreated(String dateCreated) {

        tvDateCreated.setText(dateCreated);
    }

    @Override
    public void setFromAddress(String fromAddress) {

    }

    @Override
    public void setToAddress(String toAddress) {

    }

    @Override
    public void setDistance(String distance) {

    }

    @Override
    public void setStatus(String status) {

        tvStatus.setText(status);
    }

    @Override
    public void setWaybillNo(String waybillNo) {

        tvWaybillNo.setText(waybillNo);
    }

    @Override
    public void setEarning(String earning) {

        tvEarning.setText(earning);
    }

    @Override
    public void setSize(String size) {

        tvSize.setText(size);
    }
}

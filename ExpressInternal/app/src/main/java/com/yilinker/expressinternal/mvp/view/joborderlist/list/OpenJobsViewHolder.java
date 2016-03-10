package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.OpenJobItemPresenter;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class OpenJobsViewHolder extends JobsViewHolder<OpenJobItemPresenter> implements IOpenJobView, View.OnClickListener {

    private RecyclerViewClickListener listener;
    private TextView tvStatus;
    private TextView tvWaybillNo;
    private TextView tvDateCreated;
    private TextView tvEarning;
    private TextView tvFromAddress;
    private TextView tvToAddress;
    private TextView tvDistance;
    private TextView tvSize;


    public OpenJobsViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        tvWaybillNo = (TextView) itemView.findViewById(R.id.tvWaybillNo);
        tvDateCreated = (TextView) itemView.findViewById(R.id.tvDateCreated);
        tvEarning = (TextView) itemView.findViewById(R.id.tvEarning);
        tvFromAddress = (TextView) itemView.findViewById(R.id.tvFromAddress);
        tvToAddress = (TextView) itemView.findViewById(R.id.tvToAddress);
        tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
        tvSize = (TextView) itemView.findViewById(R.id.tvSize);

        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void setDateCreated(String dateCreated) {

        tvDateCreated.setText(dateCreated);
    }

    @Override
    public void setFromAddress(String fromAddress) {
        tvFromAddress.setText(fromAddress);
    }

    @Override
    public void setToAddress(String toAddress) {
        tvToAddress.setText(toAddress);
    }

    @Override
    public void setDistance(String distance) {

        tvDistance.setText(distance);
    }

    @Override
    public void setStatus(String status) {

        tvStatus.setText(status);
        setStatusBackground(status);
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

    @Override
    public void onClick(View v) {

        presenter.onClick();

    }

    @Override
    public void showDetails(JobOrder jobOrder) {

        listener.onItemClick(jobOrder.getId(), jobOrder);
    }

    private void setStatusBackground(String status){

        int background = 0;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            background = R.drawable.bg_image_orangeyellow;
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            background = R.drawable.bg_image_bluegreen;
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)){

            background = R.drawable.bg_image_orangered;
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)){

            background = R.drawable.bg_image_marigold;
        }

        tvStatus.setBackgroundResource(background);

    }
}

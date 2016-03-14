package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.CurrentJobItemPresenter;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.OpenJobItemPresenter;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class CurrentJobsViewHolder extends JobsViewHolder<CurrentJobItemPresenter> implements ICurrentJobView, View.OnClickListener{

    private RecyclerViewClickListener listener;

    private TextView tvStatus;
    private TextView tvWaybillNo;
    private TextView tvTimeElapsed;
    private TextView tvAddress;
    private TextView tvDistance;
    private TextView tvEarning;
    private TextView tvSize;
    private TextView tvAddressLabel;
    private TextView tvProblemType;
    private RelativeLayout rlSyncLabel;

    public CurrentJobsViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        this.listener = listener;

        tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        tvWaybillNo = (TextView) itemView.findViewById(R.id.tvWaybillNo);
        tvTimeElapsed = (TextView) itemView.findViewById(R.id.tvTimeElapsed);
        tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
        tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
        tvEarning = (TextView) itemView.findViewById(R.id.tvEarning);
        tvSize = (TextView) itemView.findViewById(R.id.tvSize);
        tvAddressLabel = (TextView) itemView.findViewById(R.id.tvAddressLabel);
        tvProblemType = (TextView) itemView.findViewById(R.id.tvProblemType);
        rlSyncLabel = (RelativeLayout) itemView.findViewById(R.id.rlSyncContainer);

        itemView.setOnClickListener(this);
    }

    @Override
    public void setStatus(String status) {

        tvStatus.setText(status);
        setstatusBackground(status);
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
    public void showDetails(JobOrder jobOrder) {

        listener.onItemClick(jobOrder.getId(), jobOrder);
    }


    @Override
    public void onClick(View v) {

        presenter.onClick();
    }

    @Override
    public void setTimeElapsed(String timeElapsed) {

        tvTimeElapsed.setText(timeElapsed);
    }

    @Override
    public void setAddressText(String address) {

        tvAddress.setText(address);
    }

    @Override
    public void setDistanceText(String distance) {

        tvDistance.setText(distance);
    }

    @Override
    public void setAddressLabelText(String status) {

        tvAddressLabel.setText(getAddressLabel(status));
    }

    @Override
    public void setProblematicTypeText(String type) {

        tvProblemType.setText(type);
    }

    @Override
    public void onTick() {

        if(presenter != null) {
            presenter.onTimerTick();
        }
    }

    @Override
    public void showForSyncLabel(boolean isForSync) {

        int visibility = 0;

        if(isForSync){

            visibility = View.VISIBLE;
        }
        else{

            visibility = View.GONE;
        }

        rlSyncLabel.setVisibility(visibility);
    }

    private void setstatusBackground(String status){

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

    private String getAddressLabel(String status){

        int resourceString = 0;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            resourceString = R.string.job_order_list_pick_up_at;
        }else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            resourceString = R.string.job_order_list_deliver_to;

        }else{

            resourceString = R.string.job_order_list_drop_off_to;

        }

        return ApplicationClass.getInstance().getString(resourceString);
    }

}

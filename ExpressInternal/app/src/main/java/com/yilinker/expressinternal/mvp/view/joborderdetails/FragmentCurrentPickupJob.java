package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentPickupJobPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class FragmentCurrentPickupJob extends BaseFragment implements ICurrentPickupJobView{

    private static final String ARG_JOB = "job";

    private CurrentPickupJobPresenter presenter;

    private TextView tvStatus;
    private TextView tvWaybillNo;
    private TextView tvDateCreated;
    private TextView tvDateAccepted;
    private TextView tvAddress;
    private TextView tvName;
    private TextView tvContactNo;
    private TextView tvEarning;
    private TextView tvItem;
    private TextView tvAmountToCollect;
    private TextView tvTimeElapsed;

    public static FragmentCurrentPickupJob createInstance(JobOrder jobOrder){

        FragmentCurrentPickupJob fragment = new FragmentCurrentPickupJob();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_JOB, jobOrder);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobdetails_current, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new CurrentPickupJobPresenter();
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        initializeViews(view);

        presenter.bindView(this);

        getData();

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();
    }


    @Override
    public void initializeViews(View parent) {

        tvStatus = (TextView) parent.findViewById(R.id.tvStatus);
        tvContactNo = (TextView) parent.findViewById(R.id.tvContactNo);
        tvDateAccepted = (TextView) parent.findViewById(R.id.tvDateAccepted);
        tvDateCreated = (TextView) parent.findViewById(R.id.tvDateCreated);
        tvEarning = (TextView) parent.findViewById(R.id.tvEarning);
        tvItem = (TextView) parent.findViewById(R.id.tvItem);
        tvAddress = (TextView) parent.findViewById(R.id.tvAddress);
        tvName = (TextView) parent.findViewById(R.id.tvRecipient);
        tvWaybillNo = (TextView) parent.findViewById(R.id.tvWaybillNo);
        tvAmountToCollect = (TextView) parent.findViewById(R.id.tvAmountToCollect);
        tvTimeElapsed = (TextView) parent.findViewById(R.id.tvTimeElapsed);

    }

    @Override
    public void setTimeElapsedText(String time) {

        tvTimeElapsed.setText(time);
    }

    @Override
    public void setPickupAddressText(String pickupAddress) {

        tvAddress.setText(pickupAddress);
    }

    @Override
    public void setConsigneeNameText(String consigneeName) {

        tvName.setText(consigneeName);
    }

    @Override
    public void setConsigneeNoText(String consigneeNo) {

        tvContactNo.setText(consigneeNo);
    }

    @Override
    public void setItemText(String items) {

        tvItem.setText(items);
    }

    @Override
    public void setAmountToCollectText(String amountToCollect) {

        tvAmountToCollect.setText(amountToCollect);
    }

    @Override
    public void showProblematicForm(JobOrder jobOrder) {

    }

    @Override
    public void setWaybillNoText(String waybillNo) {

        tvWaybillNo.setText(waybillNo);
    }

    @Override
    public void setDateCreatedText(String dateCreated) {

        tvDateCreated.setText(dateCreated);
    }

    @Override
    public void setDateAccepted(String dateAccepted) {

        tvDateAccepted.setText(dateAccepted);
    }

    @Override
    public void setEarningText(String earning) {

        tvEarning.setText(earning);
    }

    @Override
    public void setStatusText(String status) {

        tvStatus.setText(status);
        setStatusBackground(status);
    }

    @Override
    public void showLoader(boolean isShown) {

    }

    private void getData(){

        Bundle arguments = getArguments();

        JobOrder jobOrder = arguments.getParcelable(ARG_JOB);

        presenter.setModel(jobOrder);
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

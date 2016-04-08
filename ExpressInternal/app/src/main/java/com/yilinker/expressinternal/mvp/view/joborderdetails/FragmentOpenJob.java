package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.OpenJobDetailsPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragment;

import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class FragmentOpenJob extends BaseFragment implements IOpenJobDetailsView, View.OnClickListener{

    private static final String ARG_JOB = "job";

    private OpenJobDetailsPresenter presenter;

    private TextView tvStatus;
    private TextView tvWaybillNo;
    private TextView tvDateCreated;
    private TextView tvPickupAddress;
    private TextView tvShipperName;
    private TextView tvShipperContactNo;
    private TextView tvDeliveryAddress;
    private TextView tvConsigneeName;
    private TextView tvConsigneeContactNo;
    private TextView tvEarning;
    private LinearLayout btnPositive;
    private LinearLayout btnNegative;
    private RelativeLayout rlProgressBar;


    public static FragmentOpenJob createInstance(JobOrder jobOrder){

        FragmentOpenJob fragment = new FragmentOpenJob();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_JOB, jobOrder);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobdetails_open, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new OpenJobDetailsPresenter();
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

        presenter.onPause();

        presenter.unbindView();
        super.onPause();

    }

    @Override
    public void setPickupAddressText(String pickupAddress) {

        tvPickupAddress.setText(pickupAddress);

    }

    @Override
    public void setShipperNameText(String shipperName) {

        tvShipperName.setText(shipperName);
    }

    @Override
    public void setShipperContactNo(String contactNo) {

        tvShipperContactNo.setText(contactNo);
    }

    @Override
    public void setDeliveryAddressText(String deliveryAddress) {

        tvDeliveryAddress.setText(deliveryAddress);
    }

    @Override
    public void setConsigneeNameText(String consigneeName) {

        tvConsigneeName.setText(consigneeName);
    }

    @Override
    public void setConsigneeContactNo(String contactNo) {

        tvConsigneeContactNo.setText(contactNo);
    }

    @Override
    public void goBackToList() {

        getActivity().onBackPressed();

    }

    @Override
    public void showErrorMessage(String errorMessage) {

        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessMessage() {

        Toast.makeText(getActivity(), getString(R.string.joborderdetail_job_order_accepted),
                Toast.LENGTH_LONG).show();
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
    public void setEarningText(String earning) {

        tvEarning.setText(earning);
    }

    @Override
    public void setStatusText(String status) {

        tvStatus.setText(status);
    }

    @Override
    public void initializeViews(View parent) {

        tvStatus = (TextView) parent.findViewById(R.id.tvStatus);
        tvEarning = (TextView) parent.findViewById(R.id.tvEarning);
        tvWaybillNo = (TextView) parent.findViewById(R.id.tvWaybillNo);
        tvDateCreated = (TextView) parent.findViewById(R.id.tvDateCreated);
        tvPickupAddress = (TextView) parent.findViewById(R.id.tvPickupAddress);
        tvShipperName = (TextView) parent.findViewById(R.id.tvShipperName);
        tvShipperContactNo = (TextView) parent.findViewById(R.id.tvShipperContactNo);
        tvDeliveryAddress = (TextView) parent.findViewById(R.id.tvDeliveryAddress);
        tvConsigneeName = (TextView) parent.findViewById(R.id.tvConsigneeName);
        tvConsigneeContactNo = (TextView) parent.findViewById(R.id.tvConsigneeContactNo);
        btnPositive = (LinearLayout) parent.findViewById(R.id.btnPositive);
        btnNegative = (LinearLayout) parent.findViewById(R.id.btnNegative);
        rlProgressBar = (RelativeLayout) parent.findViewById(R.id.rlProgress);

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);

    }

    @Override
    public void showLoader(boolean isShown) {
        if(isShown) {
            rlProgressBar.setVisibility(View.VISIBLE);
        } else {
            rlProgressBar.setVisibility(View.GONE);
        }
    }

    private void getData(){

        Bundle arguments = getArguments();

        JobOrder jobOrder = arguments.getParcelable(ARG_JOB);

        presenter.setModel(jobOrder);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnPositive:

                presenter.requestAcceptJob();

                break;

            case R.id.btnNegative:

                presenter.handleNegativeButtonClick();

                break;
        }

    }

    @Override
    public void cancelRequests(List<String> tags) {
        super.cancelRequests(tags);
    }
}

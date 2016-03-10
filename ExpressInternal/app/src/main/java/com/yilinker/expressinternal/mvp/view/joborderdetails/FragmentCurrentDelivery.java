package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityJobOderDetail;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityProblematic;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentDeliveryJobPresenter;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentDropoffJobPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;

import org.w3c.dom.Text;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class FragmentCurrentDelivery extends BaseFragment implements ICurrentDeliveryJobView, View.OnClickListener {

    private static final String ARG_JOB = "job";

    private CurrentDeliveryJobPresenter presenter;

    private TextView tvStatus;
    private TextView tvWaybillNo;
    private TextView tvDateCreated;
    private TextView tvDateAccepted;
    private TextView tvDeliveryAddress;
    private TextView tvTimeElapsed;
    private TextView tvEarning;
    private TextView tvItem;
    private TextView tvShipperName;
    private TextView tvContactNo;
    private TextView tvAmountToCollect;
    private Button btnPositive;
    private Button btnNegative;

    public static FragmentCurrentDelivery createInstance(JobOrder jobOrder){

        FragmentCurrentDelivery fragment = new FragmentCurrentDelivery();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_JOB, jobOrder);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobdetails_delivery, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new CurrentDeliveryJobPresenter();
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
        tvDateAccepted = (TextView) parent.findViewById(R.id.tvDateAccepted);
        tvDateCreated = (TextView) parent.findViewById(R.id.tvDateCreated);
        tvEarning = (TextView) parent.findViewById(R.id.tvEarning);
        tvItem = (TextView) parent.findViewById(R.id.tvItem);
        tvDeliveryAddress = (TextView) parent.findViewById(R.id.tvDeliveryAddress);
        tvTimeElapsed = (TextView) parent.findViewById(R.id.tvTimeElapsed);
        tvWaybillNo = (TextView) parent.findViewById(R.id.tvWaybillNo);
        tvShipperName = (TextView) parent.findViewById(R.id.tvShipperName);
        tvContactNo = (TextView) parent.findViewById(R.id.tvContactNo);
        tvAmountToCollect = (TextView) parent.findViewById(R.id.tvAmountToCollect);
        btnPositive = (Button) parent.findViewById(R.id.btnPositive);
        btnNegative = (Button) parent.findViewById(R.id.btnNegative);

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);
    }

    @Override
    public void setTimeElapsedText(String timeElapsed) {

        tvTimeElapsed.setText(timeElapsed);
    }

    @Override
    public void setDeliveryAddressText(String deliveryAddress) {

        tvDeliveryAddress.setText(deliveryAddress);

    }

    @Override
    public void setShipperName(String deliveryName) {

        tvShipperName.setText(deliveryName);

    }

    @Override
    public void setContactNumber(String shipperNo) {

        tvContactNo.setText(shipperNo);

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
    public void openChecklistDelivery(JobOrder jobOrder) {

        Intent intent = new Intent(getActivity(), ActivityChecklist.class);
        intent.putExtra(ActivityChecklist.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);
    }

    @Override
    public void openProblematicOptions(String jobOrderNo) {

        Intent intent = new Intent(getActivity(), ActivityProblematic.class);
        intent.putExtra(ActivityProblematic.ARG_JOB_ORDER, jobOrderNo);
        startActivity(intent);

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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.btnPositive:
                presenter.openDeliveryChecklist();
                break;

            case R.id.btnNegative:
                presenter.openProblematicOptions();
                break;

        }
    }
}

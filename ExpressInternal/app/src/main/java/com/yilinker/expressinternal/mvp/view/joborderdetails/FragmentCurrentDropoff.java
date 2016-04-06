package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentDropoffJobPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragment;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class FragmentCurrentDropoff extends BaseFragment implements ICurrentDropoffJobView, View.OnClickListener {

    private static final String ARG_JOB = "job";

    private CurrentDropoffJobPresenter presenter;

    private TextView tvStatus;
    private TextView tvWaybillNo;
    private TextView tvDateCreated;
    private TextView tvDateAccepted;
    private TextView tvAddress;
    private TextView tvTimeElapsed;
    private TextView tvEarning;
    private TextView tvItem;
    private Button btnPositive;

    public static FragmentCurrentDropoff createInstance(JobOrder jobOrder){

        FragmentCurrentDropoff fragment = new FragmentCurrentDropoff();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_JOB, jobOrder);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobdetails_dropoff, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new CurrentDropoffJobPresenter();
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        initializeViews(view);

        presenter.bindView(this);

        getData();

        presenter.startTimer();

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.onPause();
        presenter.unbindView();
    }

    @Override
    public void initializeViews(View parent) {

        tvStatus = (TextView) parent.findViewById(R.id.tvStatus);
        tvDateAccepted = (TextView) parent.findViewById(R.id.tvDateAccepted);
        tvDateCreated = (TextView) parent.findViewById(R.id.tvDateCreated);
        tvEarning = (TextView) parent.findViewById(R.id.tvEarning);
        tvItem = (TextView) parent.findViewById(R.id.tvItem);
        tvAddress = (TextView) parent.findViewById(R.id.tvAddress);
        tvTimeElapsed = (TextView) parent.findViewById(R.id.tvTimeElapsed);
        tvWaybillNo = (TextView) parent.findViewById(R.id.tvWaybillNo);
        btnPositive = (Button) parent.findViewById(R.id.btnPositive);

        btnPositive.setOnClickListener(this);

    }

    @Override
    public void setDateAcceptedText(String dateAccepted) {


        tvDateAccepted.setText(dateAccepted);
    }

    @Override
    public void setTimeElapsedText(String timeElapsed) {

        tvTimeElapsed.setText(timeElapsed);
    }

    @Override
    public void setDropoffAddress(String dropoffAddress) {

        tvAddress.setText(dropoffAddress);
    }

    @Override
    public void setItemText(String items) {

        tvItem.setText(items);
    }

    @Override
    public void goBackToList() {

        getActivity().onBackPressed();

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
                presenter.buttonOkPressed();
                break;
        }
    }
}

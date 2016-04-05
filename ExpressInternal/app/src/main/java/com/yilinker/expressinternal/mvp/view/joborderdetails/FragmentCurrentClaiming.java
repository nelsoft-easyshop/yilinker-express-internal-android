package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentClaimingJobPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;

/**
 * Created by jaybryantc on 4/4/16.
 */
public class FragmentCurrentClaiming extends BaseFragment implements ICurrentClaimingJobView, View.OnClickListener {

    public static final String ARG_JOB = "job";

    private RelativeLayout rlProgress;

    private TextView tvStatus;
    private TextView tvEarning;
    private TextView tvWaybillNo;
    private TextView tvDateCreated;
    private TextView tvDateAccepted;
    private TextView tvTimeElapsed;
    private TextView tvAddress;
    private TextView tvContactNo;
    private TextView tvItem;

    private Button btnPositive;
    private Button btnNegative;

    private CurrentClaimingJobPresenter presenter;

    private JobOrder jobOrder;

    public static FragmentCurrentClaiming createInstance(JobOrder jobOrder) {

        FragmentCurrentClaiming fragment = new FragmentCurrentClaiming();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_JOB, jobOrder);

        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            presenter = new CurrentClaimingJobPresenter();

        } else {

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        jobOrder = getArguments().getParcelable(ARG_JOB);

        if (jobOrder == null)
            throw  new IllegalStateException("Job Order not found.");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_jobdetails_claiming, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

        presenter.setModel(jobOrder);
        presenter.bindView(this);

    }

    @Override
    public void initializeViews(View parent) {

        rlProgress = (RelativeLayout) parent.findViewById(R.id.rlProgress);

        tvStatus = (TextView) parent.findViewById(R.id.tvStatus);
        tvEarning = (TextView) parent.findViewById(R.id.tvEarning);
        tvWaybillNo = (TextView) parent.findViewById(R.id.tvWaybillNo);
        tvDateCreated = (TextView) parent.findViewById(R.id.tvDateCreated);
        tvDateAccepted = (TextView) parent.findViewById(R.id.tvDateAccepted);
        tvTimeElapsed = (TextView) parent.findViewById(R.id.tvTimeElapsed);
        tvAddress = (TextView) parent.findViewById(R.id.tvAddress);
        tvContactNo = (TextView) parent.findViewById(R.id.tvContactNo);
        tvItem = (TextView) parent.findViewById(R.id.tvItem);

        btnPositive = (Button) parent.findViewById(R.id.btnPositive);
        btnNegative = (Button) parent.findViewById(R.id.btnNegative);

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);

    }

    @Override
    public void showLoader(boolean isShown) {

        rlProgress.setVisibility(isShown ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.startTimer();

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.stopTimer();
        presenter.unbindView();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnPositive:

                presenter.claimJobOrder();

                break;

            case R.id.btnNegative:

                presenter.reportOutOfStock();

                break;

        }

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
    public void setDateAcceptedLabel(String dateAccepted) {

        tvDateAccepted.setText(dateAccepted);

    }

    @Override
    public void setTimeElapsedLabel(String timeElapsed) {

        tvTimeElapsed.setText(timeElapsed);

    }

    @Override
    public void setClaimingAddressLabel(String address) {

        tvAddress.setText(address);

    }

    @Override
    public void setContactNumberLabel(String contactNumber) {

        tvContactNo.setText(contactNumber);

    }

    @Override
    public void setItemLabel(String item) {

        tvItem.setText(item);

    }

}

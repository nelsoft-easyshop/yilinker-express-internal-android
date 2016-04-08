package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.core.helper.DeviceHelper;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentClaimingJobPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragment;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;
import com.yilinker.expressinternal.mvp.view.sync.ServiceSync;
import com.yilinker.expressinternal.service.ServicePickupChecklist;
import com.yilinker.expressinternal.mvp.model.Package;

import java.util.List;

/**
 * Created by jaybryantc on 4/4/16.
 */
public class FragmentCurrentClaiming extends BaseFragment implements ICurrentClaimingJobView, View.OnClickListener {

    public static final String ARG_JOB = "job";

    private ApplicationClass appClass;

    private RelativeLayout rlProgress;

    private LinearLayout llButtons;

    private TextView tvSync;
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

    private BroadcastReceiver syncReceiver;

    private boolean toRestartMain = false;

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

        appClass = (ApplicationClass) getActivity().getApplicationContext();

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

    }

    @Override
    public void initializeViews(View parent) {

        rlProgress = (RelativeLayout) parent.findViewById(R.id.rlProgress);

        llButtons = (LinearLayout) parent.findViewById(R.id.llButtons);

        tvSync = (TextView) parent.findViewById(R.id.tvSync);
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
        tvSync.setOnClickListener(this);

    }

    @Override
    public void showLoader(boolean isShown) {

        rlProgress.setVisibility(isShown ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.bindView(this);
        presenter.startTimer();
        presenter.onSync(toRestartMain);

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.stopTimer();
        if (syncReceiver != null) {
            getActivity().unregisterReceiver(syncReceiver);
            syncReceiver = null;
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnPositive:

                if (llButtons.getAlpha() == 1.0f) {

                    presenter.claimJobOrder();

                }

                break;

            case R.id.btnNegative:

                if (llButtons.getAlpha() == 1.0f) {

                    showReportOutOfStockDialog();

                }

                break;

            case R.id.tvSync:

                presenter.sync(appClass.hasItemsForSyncing() && !appClass.isSyncing(getActivity()),
                        DeviceHelper.isDeviceConnected(getActivity()));

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

    @Override
    public void showOutOfStock() {

        showToast(getString(R.string.joborderdetail_job_order_successfully_problem_reporting));
        getActivity().finish();

    }

    @Override
    public void showToast(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void goToCompleteScreen(JobOrder jobOrder, boolean offline) {

        Intent intent = new Intent(getActivity(), ActivityCompleteJODetails.class);
        intent.putExtra(ActivityCompleteJODetails.KEY_JOB_ORDER, jobOrder);
        intent.putExtra(ActivityCompleteJODetails.KEY_IS_OFFLINE, offline);

        startActivity(intent);

    }

    @Override
    public void startClaimService(Package pack) {

        Intent service = new Intent(getActivity(), ServicePickupChecklist.class);
        service.putExtra(ServicePickupChecklist.ARG_PACKAGE, pack);
        getActivity().startService(service);

    }

    @Override
    public boolean ifUpdated(String jobOrderNo) {

        SyncDBTransaction<SyncDBObject> dbTransaction = new SyncDBTransaction<>(getActivity());
        List<SyncDBObject> dbObjects =  dbTransaction.getAll(SyncDBObject.class);

        for (SyncDBObject object : dbObjects) {

            if (object.getId().equals(jobOrderNo) && !object.isSync()) {
                return false;
            }

        }

        return true;
    }

    @Override
    public void showSyncStatus(boolean updated) {

        if (updated) {

            tvSync.setVisibility(View.GONE);
            llButtons.setAlpha(1f);
            btnPositive.setEnabled(true);
            btnNegative.setEnabled(true);

        } else {

            tvSync.setVisibility(View.VISIBLE);
            llButtons.setAlpha(0.5f);
            btnPositive.setEnabled(false);
            btnNegative.setEnabled(false);
            tvSync.setText(getString(appClass.isSyncing(getActivity()) ?
                    R.string.tools_syncing : R.string.joborderdetail_outdated));

        }

    }

    @Override
    public void startSyncService() {

        getActivity().startService(new Intent(getActivity(), ServiceSync.class));

    }

    @Override
    public void initializeReceivers() {

        syncReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                presenter.onSync(toRestartMain = !appClass.isSyncing(getActivity()));

            }
        };

        getActivity().registerReceiver(syncReceiver, new IntentFilter("sync"));

    }

    @Override
    public void showNoInternetConnection() {

        Toast.makeText(getActivity(), getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void restartMain() {

        Intent intent = new Intent(getActivity(), ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void showReportOutOfStockDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.joborderdetail_report_out_of_stock));
        builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                presenter.reportOutOfStock();

            }

        });
        builder.setNegativeButton(getString(R.string.dialog_no), null);
        builder.create();
        builder.show();

    }

}

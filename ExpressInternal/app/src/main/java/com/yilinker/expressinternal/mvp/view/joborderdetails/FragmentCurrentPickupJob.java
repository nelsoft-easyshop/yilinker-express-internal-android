package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentPickupJobPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragment;
import com.yilinker.expressinternal.mvp.view.checklist.ActivityChecklist;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;
import com.yilinker.expressinternal.mvp.view.sync.ServiceSync;

import java.util.List;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class FragmentCurrentPickupJob extends BaseFragment implements ICurrentPickupJobView, View.OnClickListener{

    private static final String ARG_JOB = "job";

    private ApplicationClass appClass;

    private BroadcastReceiver syncReceiver;

    private CurrentPickupJobPresenter presenter;

    private TextView tvSync;
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
    private LinearLayout llButtons;
    private RelativeLayout rlProgress;
    private Button btnNegative;
    private Button btnPositive;

    private JobOrder jobOrder;

    private boolean toRestartMain = false;

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

        appClass = (ApplicationClass) getActivity().getApplicationContext();

        if(savedInstanceState == null){

            presenter = new CurrentPickupJobPresenter();
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
    public void onResume() {
        super.onResume();

        presenter.onSync(toRestartMain);
    }

    @Override
    public void onPause() {

        presenter.onPause();
        presenter.unbindView();
        if (syncReceiver != null) {
            getActivity().unregisterReceiver(syncReceiver);
            syncReceiver = null;
        }

        super.onPause();

    }


    @Override
    public void initializeViews(View parent) {

        tvSync = (TextView) parent.findViewById(R.id.tvSync);
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
        rlProgress = (RelativeLayout) parent.findViewById(R.id.rlProgress);
        llButtons = (LinearLayout) parent.findViewById(R.id.llButtons);

        btnPositive = (Button) parent.findViewById(R.id.btnPositive);
        btnNegative = (Button) parent.findViewById(R.id.btnNegative);

        rlProgress.setVisibility(View.GONE);

        btnNegative.setOnClickListener(this);
        btnPositive.setOnClickListener(this);

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
    public void showOutOfStock() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog);
        builder.setMessage(getString(R.string.joborderdetail_report_out_of_stock));

        builder.setPositiveButton(getString(R.string.dialog_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        presenter.reportOutOfStock(getString(R.string.problematic_out_of_stock),
                                getString(R.string.problematic_notes));
                    }
                });

        builder.setNegativeButton(getString(R.string.dialog_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

        builder.show();

    }

    @Override
    public void goToChecklist() {

        Intent intent = new Intent(getActivity(), ActivityChecklist.class);
        intent.putExtra(ActivityChecklist.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);
    }

    @Override
    public void addToRequestQueue(Request request) {
        addRequestToQueue(request);
    }

    @Override
    public void showErrorMessage(String errorMessage) {

        Toast.makeText(getActivity(),errorMessage,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleOutOfStockResponse() {

        Toast.makeText(getActivity(),
                getString(R.string.joborderdetail_job_order_successfully_problem_reporting),
                Toast.LENGTH_LONG).show();
        getActivity().finish();
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
        rlProgress.setVisibility(isShown? View.VISIBLE: View.GONE);
    }

    private void getData(){

        Bundle arguments = getArguments();

        jobOrder = arguments.getParcelable(ARG_JOB);

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


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnNegative:

                if (llButtons.getAlpha() == 1.0f) {

                    showOutOfStock();

                }

                break;

            case R.id.btnPositive:

                if (llButtons.getAlpha() == 1.0f) {

                    goToChecklist();

                }

                break;

            default:
                break;

        }
    }
}

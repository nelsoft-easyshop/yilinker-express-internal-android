package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.core.helper.DeviceHelper;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentDeliveryJobPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragment;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;
import com.yilinker.expressinternal.mvp.view.reportproblematic.ActivityReportProblematic;
import com.yilinker.expressinternal.mvp.view.checklist.ActivityChecklist;
import com.yilinker.expressinternal.mvp.view.sync.ServiceSync;

import java.util.List;
/**
 * Created by J.Bautista on 3/8/16.
 */
public class FragmentCurrentDelivery extends BaseFragment implements ICurrentDeliveryJobView, View.OnClickListener {

    private static final String ARG_JOB = "job";

    private ApplicationClass appClass;

    private CurrentDeliveryJobPresenter presenter;

    private LinearLayout llButtons;

    private TextView tvSync;
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

    private BroadcastReceiver syncReceiver;

    private boolean toRestartMain = false;

    public static FragmentCurrentDelivery createInstance(JobOrder jobOrder){

        FragmentCurrentDelivery fragment = new FragmentCurrentDelivery();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_JOB, jobOrder);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appClass = (ApplicationClass) getActivity().getApplicationContext();

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

        presenter.startTimer();

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.onPause();
//        presenter.unbindView();
        if (syncReceiver != null) {
            getActivity().unregisterReceiver(syncReceiver);
            syncReceiver = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.onSync(toRestartMain);

    }

    @Override
    public void initializeViews(View parent) {

        llButtons = (LinearLayout) parent.findViewById(R.id.llButtons);

        tvSync = (TextView) parent.findViewById(R.id.tvSync);
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
        tvSync.setOnClickListener(this);

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
    public void setDateAccepted(String dateAccepted) {

        tvDateAccepted.setText(dateAccepted);

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

//        Intent intent = new Intent(getActivity(), ActivityChecklist2.class);
//        intent.putExtra(ActivityChecklist.ARG_JOB_ORDER, jobOrder);
//        startActivity(intent);

        Intent intent = new Intent(getActivity(), ActivityChecklist.class);
        intent.putExtra(ActivityChecklist.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);
    }

    @Override
    public void openProblematicOptions(String jobOrderNo) {

//        Intent intent = new Intent(getActivity(), ActivityProblematic.class);
//        intent.putExtra(ActivityProblematic.ARG_JOB_ORDER, jobOrderNo);
//        startActivity(intent);


        Intent intent = new Intent(getActivity(), ActivityReportProblematic.class);
        intent.putExtra(ActivityReportProblematic.ARG_JOB_ORDER_NO, jobOrderNo);
        startActivity(intent);

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

                if (llButtons.getAlpha() == 1.0) {

                    presenter.openDeliveryChecklist();

                }

                break;

            case R.id.btnNegative:

                if (llButtons.getAlpha() == 1.0) {

                    presenter.openProblematicOptions();

                }

                break;

            case R.id.tvSync:

                presenter.sync(appClass.hasItemsForSyncing() && !appClass.isSyncing(getActivity()),
                        DeviceHelper.isDeviceConnected(getActivity()));

                break;

        }
    }
}

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
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CurrentProblematicJobPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragment;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;
import com.yilinker.expressinternal.mvp.view.sync.ServiceSync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/8/16.
 */
public class FragmentCurrentProblematic extends BaseFragment implements ICurrentProblematicJobView, View.OnClickListener {

    private static final String ARG_JOB = "job";

    private ApplicationClass appClass;

    private BroadcastReceiver syncReceiver;

    private CurrentProblematicJobPresenter presenter;

    private TextView tvSync;
    private TextView tvStatus;
    private TextView tvWaybillNo;
    private TextView tvDateCreated;
    private TextView tvDateAccepted;
    private TextView tvAddress;
    private TextView tvEarning;
    private TextView tvItem;
    private TextView tvProblemType;
    private TextView tvViewImages;

    private boolean toRestartMain = false;

    public static FragmentCurrentProblematic createInstance(JobOrder jobOrder){

        FragmentCurrentProblematic fragment = new FragmentCurrentProblematic();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_JOB, jobOrder);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobdetails_problematic, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appClass = (ApplicationClass) getActivity().getApplicationContext();

        if(savedInstanceState == null){

            presenter = new CurrentProblematicJobPresenter();
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        initializeViews(view);

        presenter.bindView(this);

        getData();

    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.onSync(toRestartMain);
    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();

        if (syncReceiver != null) {
            getActivity().unregisterReceiver(syncReceiver);
            syncReceiver = null;
        }
    }

    @Override
    public void initializeViews(View parent) {

        tvSync = (TextView) parent.findViewById(R.id.tvSync);
        tvStatus = (TextView) parent.findViewById(R.id.tvStatus);
        tvDateAccepted = (TextView) parent.findViewById(R.id.tvDateAccepted);
        tvDateCreated = (TextView) parent.findViewById(R.id.tvDateCreated);
        tvEarning = (TextView) parent.findViewById(R.id.tvEarning);
        tvItem = (TextView) parent.findViewById(R.id.tvItem);
        tvAddress = (TextView) parent.findViewById(R.id.tvAddress);
        tvWaybillNo = (TextView) parent.findViewById(R.id.tvWaybillNo);
        tvProblemType = (TextView) parent.findViewById(R.id.tvProblemType);
        tvViewImages = (TextView) parent.findViewById(R.id.tvViewImages);

        tvViewImages.setOnClickListener(this);

    }

    @Override
    public void setDateAcceptedText(String dateAccepted) {

        tvDateAccepted.setText(dateAccepted);
    }

    @Override
    public void setDeliveryAddress(String deliveryAddress) {

        tvAddress.setText(deliveryAddress);
    }

    @Override
    public void setItemText(String items) {

        tvItem.setText(items);
    }

    @Override
    public void setProblemType(String problemType) {
        tvProblemType.setText(problemType);
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
    public void showReportedImages(List<String> imageUrls) {

        Intent intent = new Intent(getActivity(), ActivityImageGallery.class);
        intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, (ArrayList) imageUrls);
        intent.putExtra(ActivityImageGallery.ARG_TYPE, ImagePagerAdapter.TYPE_URL);
        startActivity(intent);

    }

    @Override
    public void showNoImageError() {

        Toast.makeText(getActivity(), R.string.joborderdetail_error_no_image, Toast.LENGTH_LONG).show();
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

        } else {

            tvSync.setVisibility(View.VISIBLE);
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

        if(v.getId() == R.id.tvViewImages) {
            presenter.onViewImageClick();
        }
    }
}

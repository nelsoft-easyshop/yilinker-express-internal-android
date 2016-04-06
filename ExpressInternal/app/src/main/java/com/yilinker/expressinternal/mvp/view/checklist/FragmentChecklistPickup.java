package com.yilinker.expressinternal.mvp.view.checklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.core.helper.DeviceHelper;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.checklist.FragmentDialogUpdateStatus2;
import com.yilinker.expressinternal.interfaces.RequestOngoingListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.Rider;
import com.yilinker.expressinternal.mvp.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.checklist.ChecklistPickupPresenter;
import com.yilinker.expressinternal.mvp.view.confirmpackage.ActivityConfirmPackage;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ActivityCompleteJODetails;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;
import com.yilinker.expressinternal.service.ServicePickupChecklist;

import java.util.List;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class FragmentChecklistPickup extends ChecklistBaseFragment<ChecklistPickupPresenter> implements IChecklistPickupView, View.OnClickListener{

    private static final int REQUEST_CONFIRM_PACKAGE = 100;
    private static final int REQUEST_DIALOG_UPDATE = 102;

    private ChecklistItemAdapter adapter;

    private Button btnComplete;

    private RequestOngoingListener requestListener;

    public static FragmentChecklistPickup createInstance(JobOrder jobOrder){

        FragmentChecklistPickup fragment = new FragmentChecklistPickup();

        Bundle args = new Bundle();
        args.putParcelable(ARG_JOB_ORDER, jobOrder);

        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_joborder_checklist, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new ChecklistPickupPresenter();

            initializeViews(view);

            presenter.bindView(this);

            //For creating the checklist
            presenter.onViewCreated(getData(), getTitles(R.array.checklist_pickup), getTitleWithData(R.array.checklist_pickup_with_data), getString(R.string.checklist_pickup_payment));

        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        requestListener = (RequestOngoingListener)activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void onResume() {

        presenter.bindView(this);
        super.onResume();
    }

    @Override
    public void onPause() {

        presenter.onPause();

        presenter.unbindView();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        presenter.bindView(this);

        if(resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case  REQUEST_CONFIRM_PACKAGE:

                    handleConfirmPackageResult(data);
                    break;

                case REQUEST_DIALOG_UPDATE:

                    String status = data.getStringExtra(FragmentDialogUpdateStatus2.ARG_NEW_STATUS);

                    presenter.onSelectStatus(status);
                    break;
            }
        }

    }

    @Override
    public void initializeViews(View parent) {

        btnComplete = (Button) parent.findViewById(R.id.btnConfirm);

        RecyclerView rvChecklist = (RecyclerView) parent.findViewById(R.id.rvChecklist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvChecklist.setLayoutManager(layoutManager);

        adapter = new ChecklistItemAdapter(this);
        rvChecklist.setAdapter(adapter);

        btnComplete.setText(getString(R.string.checklist_pickup_complete));
        btnComplete.setOnClickListener(this);
    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void showConfirmPackageScreen(Package selectedPackage) {

        Intent intent = new Intent(getActivity(), ActivityConfirmPackage.class);
        intent.putExtra(ActivityConfirmPackage.ARG_SELECTED_PACKAGE, selectedPackage);

        startActivityForResult(intent, REQUEST_CONFIRM_PACKAGE);
    }

    @Override
    public void goToMainScreen() {

        Intent intent = new Intent(getActivity(), ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    public void goToCompleteScreen(JobOrder jobOrder, boolean offline) {

        Intent intent = new Intent(getActivity(), ActivityCompleteJODetails.class);
        intent.putExtra(ActivityCompleteJODetails.KEY_JOB_ORDER, jobOrder);
        intent.putExtra(ActivityCompleteJODetails.KEY_IS_OFFLINE, offline);

        startActivity(intent);

    }

    @Override
    public void startPickupService(Package selectedPackage) {

        Intent service = new Intent(getActivity(), ServicePickupChecklist.class);
        service.putExtra(ServicePickupChecklist.ARG_PACKAGE, selectedPackage);
        getActivity().startService(service);
    }

    @Override
    public void showStatusOptionDialog() {

        FragmentDialogUpdateStatus2 dialog = FragmentDialogUpdateStatus2.createInstance(REQUEST_DIALOG_UPDATE);
        dialog.setTargetFragment(this, REQUEST_DIALOG_UPDATE);
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public String getRiderAreaCode() {

        ApplicationClass appClass = (ApplicationClass) ApplicationClass.getInstance();

        Rider rider = appClass.getRider();

        return rider.getAreaCode();
    }

    @Override
    public void addRequest(Request request) {
        addRequestToQueue(request);
    }

    @Override
    public void cancelRequest(List<String> requests) {

        cancelRequests(requests);
    }

    @Override
    public void loadChecklistItems(List<ChecklistItem> items) {

        adapter.clearAndAddAll(items);
    }

    @Override
    public void updateItem(ChecklistItem item) {

        adapter.updateItem(item);
        presenter.onUpdateChecklistItem(item);
    }

    @Override
    public void enableCompleteButton(boolean isEnabled) {

        btnComplete.setEnabled(isEnabled);
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showScreenLoader(boolean showLoader) {

        String text = null;

        if(showLoader){

            text = getString(R.string.completing_delivery);

        }
        else{

            text = getString(R.string.checklist_delivery_complete);
        }

        requestListener.onRequestOngoing(showLoader);
        btnComplete.setText(text);

    }

    @Override
    public void onItemClick(int position, ChecklistItem object) {


        String confirmPackage = getString(R.string.checklist_delivery_package);

        if(object.getTitle().equalsIgnoreCase(confirmPackage)){

            presenter.onConfirmPackageClick(object);
        }
        else{

            updateItem(object);
        }

    }



    private void handleConfirmPackageResult(Intent data){

        Package selectedPackage = data.getParcelableExtra(ActivityConfirmPackage.ARG_SELECTED_PACKAGE);

        presenter.onConfirmPackageResult(selectedPackage);

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnConfirm:

                if (DeviceHelper.isDeviceConnected(getActivity())) {

                    presenter.onCompleteButtonClick();

                } else {

                    presenter.doOfflineCompletion();

                }

                break;
        }
    }



}
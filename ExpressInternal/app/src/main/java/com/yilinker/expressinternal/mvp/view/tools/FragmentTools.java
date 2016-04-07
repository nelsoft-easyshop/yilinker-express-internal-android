package com.yilinker.expressinternal.mvp.view.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yilinker.core.helper.DeviceHelper;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.qrscanner.ActivityAcknowledge2;
import com.yilinker.expressinternal.controllers.qrscanner.ActivityBulkCheckIn;
import com.yilinker.expressinternal.controllers.qrscanner.ActivityScanToDetails;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.model.Tools;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.tools.MainToolsPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;
import com.yilinker.expressinternal.mvp.view.cashManagement.ActivityCashManagement;
import com.yilinker.expressinternal.mvp.view.sync.ServiceSync;

import java.util.List;

/**
 * Created by rlcoronado on 01/03/2016.
 */
public class FragmentTools extends BaseFragment implements IMainToolsView, TabItemClickListener {

    private ApplicationClass appClass;
//    private boolean hasForSyncing = false;

    private MainToolsPresenter presenter;
    private ToolsAdapter adapter;

    private RecyclerView rvTools;

    private BroadcastReceiver syncReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        appClass = (ApplicationClass) getActivity().getApplicationContext();

        initializeViews(view);

        if (savedInstanceState == null) {

            presenter = new MainToolsPresenter();

//            presenter.bindView(this);


        } else {

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        initializeTools();
        initializeReceivers();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);

    }


    @Override
    public void onResume() {
        super.onResume();

        presenter.bindView(this);

        getActivity().registerReceiver(syncReceiver, new IntentFilter("sync"));

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();

        getActivity().unregisterReceiver(syncReceiver);

    }
    @Override
    public void initializeViews(View parent) {

        rvTools = (RecyclerView) parent.findViewById(R.id.rvTools);

        rvTools.setHasFixedSize(true);
        rvTools.setLayoutManager(new GridLayoutManager(getActivity(), 3));

//        adapter = new ToolsAdapter(this);
//        rvTools.setAdapter(adapter);

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    private void initializeTools() {

        Resources resources = getResources();
        String[] tabTitles = resources.getStringArray(R.array.array_tool_items);

        TypedArray toolsIconsArray = resources.obtainTypedArray(R.array.array_tool_images);

        int[] toolIcons = convertTypedArrayToIntArray(toolsIconsArray);

        toolsIconsArray.recycle();

//        presenter.initializeToolsModel(tabTitles, toolIcons)

        presenter.setModel(presenter.createTools(tabTitles, toolIcons));
//        presenter.hasItemsForSyncing(appClass.hasItemsForSyncing());

    }

    //TODO Move to other file
    private int[] convertTypedArrayToIntArray(TypedArray array){

        int[] intArray = new int[array.length()];

        for(int i =0; i < array.length(); i++){

            intArray[i] = array.getResourceId(i, 0);

        }

        return intArray;
    }

    @Override
    public void loadTabs(List<Tools> tools) {

        adapter = new ToolsAdapter(this);
        rvTools.setAdapter(adapter);
        adapter.clearAndAddAll(tools);

    }

    @Override
    public void openActivity(int selectedActivity) {

        Intent intent;

        switch (selectedActivity) {
            case 0:
//                intent = new Intent(getActivity(), ActivitySingleScanner.class);
                intent = new Intent(getActivity(), ActivityScanToDetails.class);
                startActivity(intent);

                break;

            case 1:
//                intent = new Intent(getActivity(), ActivityScanner.class);
                intent = new Intent(getActivity(), ActivityBulkCheckIn.class);
                startActivity(intent);
                break;

            case 2:
//                intent = new Intent(getActivity(), ActivityAcknowledge.class);
                intent = new Intent(getActivity(), ActivityAcknowledge2.class);
                startActivity(intent);
                break;

            case 3:

                intent = new Intent(getActivity(), ActivityCashManagement.class);
                startActivity(intent);
                break;

            case 4:

//                presenter.openActivitySync(hasForSyncing);
//                presenter.openActivitySync(appClass.hasItemsForSyncing());
                presenter.doIfSyncable(syncable(), appClass.isSyncing(getActivity()),
                        DeviceHelper.isDeviceConnected(getActivity()));

                break;

        }

    }

    @Override
    public void initializeReceivers() {

        syncReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                presenter.updateSyncItem();
            }
        };

    }

    @Override
    public void setItemIndicatorResourceId(Tools tools) {

        tools.setWarningResourceId(appClass.hasItemsForSyncing() && !appClass.isSyncing(getActivity()) ?
                R.drawable.ic_for_syncing : 0);

    }

    @Override
    public void setItemTitle(Tools tools) {

        tools.setTitle(appClass.isSyncing(getActivity()) ?
                getString(R.string.tools_syncing) : getString(R.string.tools_sync));

    }

    @Override
    public void startSyncing() {

        getActivity().startService(new Intent(getActivity(), ServiceSync.class));

    }

    @Override
    public boolean syncable() {
        return appClass.hasItemsForSyncing() && !appClass.isSyncing(getActivity());
    }

    @Override
    public void updateItem(Tools tools) {

        adapter.updateItem(tools);

    }

    @Override
    public void showNoInternetConnection() {

        Toast.makeText(getActivity(), getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showSyncingOnProgress() {

        Toast.makeText(getActivity(), "Syncing.", Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void enableSyncButton(boolean disableSyncing) {
//
//        hasForSyncing = disableSyncing;
//
//    }

//    @Override
//    public void openActivitySyncing() {
//
//        Intent intent = new Intent(getActivity(), ActivitySync.class);
//        startActivityForResult(intent, ActivitySync.REQUEST_SYNC);
//
//    }
//
    @Override
    public void showNoItemsForSyncingMessage() {

        Toast.makeText(getActivity(), getString(R.string.request_no_data_for_syncing), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTabItemClick(int position) {

        presenter.onToolSelected(position);

    }
}

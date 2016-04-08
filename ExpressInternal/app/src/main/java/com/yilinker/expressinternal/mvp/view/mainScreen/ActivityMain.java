package com.yilinker.expressinternal.mvp.view.mainScreen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.mainScreen.MainScreenPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragmentActivity;
import com.yilinker.expressinternal.mvp.view.joborderlist.FragmentJobListMain;
import com.yilinker.expressinternal.mvp.view.profile.FragmentProfile;
import com.yilinker.expressinternal.mvp.view.tools.FragmentTools;


import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public class ActivityMain extends BaseFragmentActivity implements IMainView, TabItemClickListener{

    private static final String KEY_CONTENT = "content";

    private ApplicationClass appClass;

    private FrameLayout flContainer;

    private MainScreenPresenter presenter;
    private MainTabAdapter adapter;

    private Fragment content;

    private BroadcastReceiver syncBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        appClass = (ApplicationClass) getApplicationContext();

        if(savedInstanceState == null){

            presenter = new MainScreenPresenter();
            presenter.bindView(this);

            initializeViews(null);
            initializeReceivers();
            setupTab();
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
            presenter.bindView(this);

            //Restore content fragment here
            content = getFragmentManager().getFragment(savedInstanceState, KEY_CONTENT);
            replaceFragment(content);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);

        //Saving current fragment instance
        getFragmentManager().putFragment(outState, KEY_CONTENT, content);
    }


    @Override
    protected void onResume() {

        presenter.bindView(this);
        presenter.setAvailableIndicators();
        registerReceiver(syncBroadcastReceiver, new IntentFilter("sync"));

        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.unbindView();
        unregisterReceiver(syncBroadcastReceiver);

    }

    @Override
    public void initializeReceivers() {

        syncBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                presenter.setAvailableIndicators();
            }
        };

    }

    @Override
    public void initializeViews(View view) {

        flContainer = (FrameLayout) findViewById(R.id.flContainer);

        RecyclerView rvTabs = (RecyclerView) findViewById(R.id.rvMainTab);
        rvTabs.setHasFixedSize(true);
        rvTabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new MainTabAdapter(R.layout.layout_main_tab_item, this);
        adapter.setEqualWidth(getWindowManager(), 4);

        rvTabs.setAdapter(adapter);

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void loadTabs(List<TabItem> tabs) {

        adapter.addAll(tabs);
    }

    @Override
    public void changeSelectedTab(TabItem previousTab, TabItem currentTab) {

        adapter.updateItem(previousTab);
        adapter.updateItem(currentTab);

    }

    @Override
    public void replaceFragment(int selectedTab) {

//        Fragment fragment = null;

        switch (selectedTab){

            case 0:

                content = new FragmentJobListMain();
                break;

            case 1:

                content = new FragmentTools();
                break;

            case 2:

                content = new Fragment();
                break;

            case 3:

                content = new FragmentProfile();
                break;

        }

        replaceFragment(content);

    }

    @Override
    public boolean syncable() {

        return appClass.hasItemsForSyncing() && !appClass.isSyncing(this);

    }

    @Override
    public void setTabIndicator(TabItem tabItem, boolean show) {

        tabItem.setIndicatorIcon(show ? R.drawable.ic_for_syncing : 0);
        adapter.updateItem(tabItem);

    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.flContainer, fragment);

        transaction.commit();
    }


    private void setupTab(){

        Resources resources = getResources();
        String[] tabTitles = resources.getStringArray(R.array.main_tab_items);

        TypedArray tabIconsArray = resources.obtainTypedArray(R.array.main_tab_icons_unselected);
        TypedArray selectedIconsArray = resources.obtainTypedArray(R.array.main_tab_icons_selected);

        int[] tabIcons = convertTypedArrayToIntArray(tabIconsArray);
        int[] selectedIcons = convertTypedArrayToIntArray(selectedIconsArray);

        tabIconsArray.recycle();
        selectedIconsArray.recycle();

        presenter.onInitializeTabs(tabTitles, tabIcons, selectedIcons);

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
    public void onTabItemClick(int position) {

        presenter.onTabSelected(position);
    }

}

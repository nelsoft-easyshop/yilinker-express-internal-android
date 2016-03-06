package com.yilinker.expressinternal.mvp.view.mainScreen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.mainScreen.MainScreenPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;
import com.yilinker.expressinternal.mvp.view.joborderlist.FragmentJobListMain;


import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public class ActivityMain extends BaseFragmentActivity implements IMainView, TabItemClickListener{

    private FrameLayout flContainer;

    private MainScreenPresenter presenter;
    private MainTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){

            presenter = new MainScreenPresenter();
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }


        setContentView(R.layout.activity_main_screen);
        initializeViews(null);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);

        setupTab();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.unbindView();

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

        Fragment fragment = null;

        switch (selectedTab){

            case 0:

                fragment = new FragmentJobListMain();
                break;

            case 1:

                fragment = new Fragment();
                break;

            case 2:

                fragment = new Fragment();
                break;

            case 3:

                fragment = new Fragment();
                break;

        }

        replaceFragment(fragment);

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

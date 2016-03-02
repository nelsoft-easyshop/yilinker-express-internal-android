package com.yilinker.expressinternal.mvp.view.mainScreen;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.model.MainTab;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.login.LoginPresenter;
import com.yilinker.expressinternal.mvp.presenter.mainScreen.MainScreenPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public class ActivityMain extends BaseFragmentActivity implements IMainView{

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
        initializeViews();

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
    public void initializeViews() {

        RecyclerView rvTabs = (RecyclerView) findViewById(R.id.rvMainTab);
        rvTabs.setHasFixedSize(true);
        rvTabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new MainTabAdapter(R.layout.layout_main_tab_item);
        adapter.setEqualWidth(getWindowManager(), 4);

        rvTabs.setAdapter(adapter);

    }

    @Override
    public void loadTabs(List<MainTab> tabs) {

        adapter.addAll(tabs);
    }

    @Override
    public void changeSelectedTab(MainTab previousTab, MainTab currentTab) {

    }

    @Override
    public void replaceFragment(int selectedTab) {


    }

    private void setupTab(){

        Resources resources = getResources();
        String[] tabTitles = resources.getStringArray(R.array.main_tab_items);
        int[] tabIcons = {R.drawable.ic_back_arrow, R.drawable.ic_back_arrow, R.drawable.ic_back_arrow, R.drawable.ic_back_arrow};
        int[] selectedIcons = {R.drawable.ic_back_arrow, R.drawable.ic_back_arrow, R.drawable.ic_back_arrow, R.drawable.ic_back_arrow};

        presenter.onInitializeTabs(tabTitles, tabIcons, selectedIcons);

    }
}

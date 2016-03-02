package com.yilinker.expressinternal.mvp.view.mainScreen;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/1/2016.
 */
public class ActivityMain extends Activity implements IMainView, TabItemClickListener{

    private RecyclerView rvMainTab;

    private List<MainTab> mainTabs;
    private MainScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if(savedInstanceState == null){
            presenter = new MainScreenPresenter();
        }
        else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        initViews();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    @Override
    public void initViews() {

        rvMainTab = (RecyclerView) findViewById(R.id.rvMainTab);
        setUpTabs();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, fragment);
        transaction.commit();

    }

    @Override
    public void setUpTabs() {
        //TODO setUp adapter here

        String[] tabTitles = getResources().getStringArray(R.array.main_tab_items);
        TypedArray tabIcons = getResources().obtainTypedArray(R.array.main_tab_icons_unselected);
        presenter.setUpMainTabs(tabTitles, tabIcons);
    }

    @Override
    public void loadTabs(List<MainTab> tabs) {
        //TODO call update tabs from  tabPresenter

    }

    @Override
    public void updateTabs() {
        //TODO notify the adapter
        //TODO call replaceFragment
    }

    @Override
    public void onTabItemClick(int position) {
        presenter.setSelectedTab(position);
    }
}

package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobListMainPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.FragmentJobList;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.IJobListView;
import com.yilinker.expressinternal.mvp.view.joborderlist.map.FragmentJobListMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class FragmentJobListMain extends BaseFragment implements IJobListMainView, View.OnClickListener, TabItemClickListener{

    private static final int VIEW_LIST = 0;
    private static final int VIEW_MAP = 1;

    private JobListMainPresenter presenter;

    private TextView tvItemCount;
    private EditText etSearch;
    private LinearLayout llFilterContainer;
    private LinearLayout llJobTypeContainer;

    private JobsTabAdapter tabAdapter;

    private IJobListView currentFragmentView;
    private int currentView = VIEW_LIST;

    private TextWatcher searchTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            presenter.onSearchWaybill(s.toString());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new JobListMainPresenter();

        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        presenter.bindView(this);

        initializeViews(view);

        setupTab();

        presenter.onViewCreated();
    }


    @Override
    public void onResume() {
        super.onResume();

        presenter.onResume("Open");

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.ivToggleView:

                presenter.onSwitchButtonClicked();
                break;

            case R.id.tvFilter:

                toggleFilter();
                break;

            case R.id.etSearch:

                llJobTypeContainer.setVisibility(View.GONE);
                break;

        }

    }

    @Override
    public void initializeViews(View parent) {

        ImageView ivToggle = (ImageView) parent.findViewById(R.id.ivToggleView);
        RecyclerView rvTabs = (RecyclerView) parent.findViewById(R.id.rvJobOrderTabs);
        TextView tvFilter = (TextView) parent.findViewById(R.id.tvFilter);
        tvItemCount = (TextView) parent.findViewById(R.id.tvItemCount);
        llFilterContainer = (LinearLayout) parent.findViewById(R.id.llFilterContainer);
        llJobTypeContainer = (LinearLayout) parent.findViewById(R.id.llJobTypesContainer);
        etSearch = (EditText) parent.findViewById(R.id.etSearch);


        //For tabs
        rvTabs.setHasFixedSize(true);
        rvTabs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        tabAdapter = new JobsTabAdapter(R.layout.layout_jobs_tab, this);
        tabAdapter.setEqualWidth(getActivity().getWindowManager(), 2);
        rvTabs.setAdapter(tabAdapter);


        ivToggle.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
        etSearch.setOnClickListener(this);

        etSearch.addTextChangedListener(searchTextWatcher);
    }

    @Override
    public void reloadList(List<JobOrder> jobOrders) {

        currentFragmentView.loadJobOrderList(jobOrders);

    }

    @Override
    public void setResultCountText(String text) {

        tvItemCount.setText(text);

    }

    @Override
    public void showLoader(boolean isVisible) {


    }

    @Override
    public void loadTabs(List<TabItem> tabs) {

        tabAdapter.addAll(tabs);

    }

    @Override
    public void changeSelectedTab(TabItem previousTab, TabItem currentTab) {

        tabAdapter.updateItem(previousTab);
        tabAdapter.updateItem(currentTab);

    }

    @Override
    public void addRequest(Request request) {

        addRequestToQueue(request);
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.flContainer, fragment);

        transaction.commit();
    }

    @Override
    public void switchView(List<JobOrder> jobOrders){

        if(currentView == VIEW_LIST){

            showMapView(jobOrders);
            currentView = VIEW_MAP;
        }
        else {

            showListView(jobOrders);
            currentView = VIEW_LIST;
        }


    }

    @Override
    public void onFilterClicked(int position, boolean isChecked) {



    }


    @Override
    public void showMapView(List<JobOrder> jobOrders){

        Bundle args = new Bundle();
        args.putParcelableArrayList(FragmentJobListMap.ARG_JOBS, (ArrayList<JobOrder>)jobOrders);

        Fragment fragment = new FragmentJobListMap();
        fragment.setArguments(args);

        currentFragmentView = (IJobListView) fragment;

        replaceFragment(fragment);


    }

    @Override
    public void showListView(List<JobOrder> jobOrders){

        Bundle args = new Bundle();
        args.putParcelableArrayList(FragmentJobList.ARG_JOBS, (ArrayList<JobOrder>)jobOrders);

        Fragment fragment = new FragmentJobList();
        fragment.setArguments(args);

        currentFragmentView = (IJobListView) fragment;

        replaceFragment(fragment);

    }

    @Override
    public void onTabItemClick(int position) {

        presenter.onTabItemClicked(position);
    }

    private void setupTab(){

        String[] tabTitles = getResources().getStringArray(R.array.jobs_tab_items);

        presenter.initializeTabs(tabTitles);
    }


    private void toggleFilter(){

        if(llFilterContainer.getVisibility() == View.VISIBLE){

            llFilterContainer.setVisibility(View.GONE);
        }
        else{

            llFilterContainer.setVisibility(View.VISIBLE);

        }

    }

}

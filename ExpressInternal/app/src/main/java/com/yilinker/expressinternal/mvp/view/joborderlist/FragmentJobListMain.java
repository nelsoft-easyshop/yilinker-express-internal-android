package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.qrscanner.ActivitySingleScanner;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.JobType;
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

    private static final int REQUEST_QR_CODE = 100;

    private static final int VIEW_LIST = 0;
    private static final int VIEW_MAP = 1;

    private JobListMainPresenter presenter;

    private SwipeRefreshLayout refreshLayout;
    private TextView tvItemCount;
    private TextView tvFilter;
    private EditText etSearch;
    private ImageView ivToggle;
    private LinearLayout llFilterContainer;
    private LinearLayout llJobTypeContainer;

    private JobsTabAdapter tabAdapter;
    private JobTypeAdapter typeAdapter;

    private static IJobListView currentFragmentView;
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

    private RecyclerViewClickListener<JobType> typeClickListener = new RecyclerViewClickListener<JobType>() {
        @Override
        public void onItemClick(int position, JobType object) {

            selectType(object);

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){

            presenter = new JobListMainPresenter();

        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_jobs, container, false);


        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.bindView(this);

        initializeViews(view);

        setupTab();

        setUpTypeFilter();

        presenter.onViewCreated();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){

            case REQUEST_QR_CODE:

                handleScannerResult(data);
                break;
        }

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

            case R.id.ivScanner:

                showQRCodeScanner();
                break;

        }

    }

    @Override
    public void initializeViews(View parent) {

        ivToggle = (ImageView) parent.findViewById(R.id.ivToggleView);
        ImageView ivScanner = (ImageView) parent.findViewById(R.id.ivScanner);
        RecyclerView rvTabs = (RecyclerView) parent.findViewById(R.id.rvJobOrderTabs);
        RecyclerView rvJobTypes = (RecyclerView) parent.findViewById(R.id.rvJobOrderTypes);
        tvFilter = (TextView) parent.findViewById(R.id.tvFilter);
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

        //For job types
        rvJobTypes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        typeAdapter = new JobTypeAdapter(typeClickListener);
        rvJobTypes.setAdapter(typeAdapter);

        refreshLayout = (SwipeRefreshLayout) parent.findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {


            }
        });

        ivToggle.setOnClickListener(this);
        ivScanner.setOnClickListener(this);
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

        refreshLayout.setRefreshing(isVisible);
    }

    @Override
    public void loadJobTabs(List<TabItem> tabs) {

        tabAdapter.addAll(tabs);

    }

    @Override
    public void loadJobTypes(List<JobType> jobTypes) {

        typeAdapter.addAll(jobTypes);
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

            ivToggle.setImageResource(R.drawable.ic_job_orders_in_list);
            showMapView(jobOrders);
            currentView = VIEW_MAP;
        }
        else {

            ivToggle.setImageResource(R.drawable.ic_maps_selected);
            showListView(jobOrders);
            currentView = VIEW_LIST;
        }


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

    private void setUpTypeFilter(){

        String[] filterLabels = getResources().getStringArray(R.array.jobs_type_items);

        presenter.initializeFilter(filterLabels);
    }


    private void toggleFilter(){

        if(llFilterContainer.getVisibility() == View.VISIBLE){

            llFilterContainer.setVisibility(View.GONE);
        }
        else{

            llFilterContainer.setVisibility(View.VISIBLE);

        }

    }

    private void selectType(JobType type){

        typeAdapter.updateItem(type);
        presenter.onFilterChanged(type);

    }

    private void showQRCodeScanner(){

        Intent intent = new Intent(getActivity(), ActivitySingleScanner.class);
        startActivityForResult(intent, REQUEST_QR_CODE);

    }

    private void handleScannerResult(Intent data){

        if(data != null){

            String result = data.getStringExtra(ActivitySingleScanner.ARG_TEXT);

            //TODO get result and set the text of the search field
        }

    }

}

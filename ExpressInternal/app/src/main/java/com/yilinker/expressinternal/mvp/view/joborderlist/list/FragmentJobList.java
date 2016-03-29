package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobListPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ActivityJobDetailsMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class FragmentJobList extends BaseFragment implements IJobListView, RecyclerViewClickListener<JobOrder>, SwipeRefreshLayout.OnRefreshListener{

    public static final String ARG_JOBS = "jobs";

    private JobListPresenter presenter;

    private JobsAdapter adapter;

    private SwipeRefreshLayout refreshLayout;
    private TextView tvRefresh;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobs_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new JobListPresenter();
            presenter.setModel(new ArrayList<JobOrder>());
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        initializeViews(view);

        presenter.setModel((ArrayList) getArguments().getParcelableArrayList(ARG_JOBS));
        presenter.bindView(this);


    }

    @Override
    public void onPause() {

        presenter.unbindView();

        super.onPause();
    }

    @Override
    public void loadJobOrderList(List<JobOrder> jobOrders) {

        adapter.clearAndAddAll(jobOrders);

    }

    @Override
    public void showJobOrderDetails(JobOrder joborder) {

        showJobDetails(joborder);

    }


    @Override
    public void initializeViews(View parent) {

        refreshLayout = (SwipeRefreshLayout) parent.findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(android.R.color.transparent);
//        refreshLayout.setProgressBackgroundColorSchemeColor(android.R.color.transparent);
        refreshLayout.setBackgroundResource(android.R.color.transparent);

        //For jobs
        RecyclerView rvJobOrders = (RecyclerView) parent.findViewById(R.id.rvJobOrder);
        rvJobOrders.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new JobsAdapter(R.layout.layout_job_order_list_open_2, this);
        rvJobOrders.setAdapter(adapter);


    }

    @Override
    public void showLoader(final boolean isShown) {

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {

                refreshLayout.setRefreshing(isShown);

            }
        });
    }

    private void showJobDetails(JobOrder jobOrder){

        Intent intent = new Intent(getActivity(), ActivityJobDetailsMain.class);
        intent.putExtra(ActivityJobDetailsMain.ARG_JOB, jobOrder);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }

    @Override
    public void onItemClick(int position, JobOrder object) {

        showJobOrderDetails(object);
    }

    @Override
    public void onRefresh() {

        SwipeRefreshLayout.OnRefreshListener listener = (SwipeRefreshLayout.OnRefreshListener)getParentFragment();

        listener.onRefresh();
    }

}

package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobListPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class FragmentJobList extends BaseFragment implements IJobListView {

    public static final String ARG_JOBS = "jobs";

    private JobListPresenter presenter;

    private JobsAdapter adapter;


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
    public void loadJobOrderList(List<JobOrder> jobOrders) {

        adapter.clearAndAddAll(jobOrders);


    }

    @Override
    public void showJobOrderDetails(JobOrder joborder) {

    }


    @Override
    public void initializeViews(View parent) {

        //For jobs
        RecyclerView rvJobOrders = (RecyclerView) parent.findViewById(R.id.rvJobOrder);
        rvJobOrders.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new JobsAdapter(R.layout.layout_job_order_list_open_2);
        rvJobOrders.setAdapter(adapter);


    }
}

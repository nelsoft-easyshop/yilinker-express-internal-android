package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobListMainPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class FragmentJobListMain extends BaseFragment implements IJobListMainView{

    private JobListMainPresenter presenter;

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


        initializeViews();

    }


    @Override
    public void onResume() {
        super.onResume();

        presenter.bindView(this);

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    public void initializeViews() {



    }

    @Override
    public void reloadList(List<JobOrder> jobOrders) {


    }

    @Override
    public void setResultCountText(String text) {


    }

    @Override
    public void showLoader(boolean isVisible) {


    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.flContainer, fragment);

        transaction.commit();
    }

}

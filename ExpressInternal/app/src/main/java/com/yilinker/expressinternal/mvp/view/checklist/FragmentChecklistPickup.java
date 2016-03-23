package com.yilinker.expressinternal.mvp.view.checklist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.*;
import com.yilinker.expressinternal.mvp.view.BaseFragment;

import java.util.List;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class FragmentChecklistPickup extends BaseFragment implements IChecklistPickupView, RecyclerViewClickListener<ChecklistItem>{

    private static final String ARG_JOB_ORDER = "joborder";


    public static FragmentChecklistPickup createInstance(JobOrder jobOrder){

        FragmentChecklistPickup fragment = new FragmentChecklistPickup();

        Bundle args = new Bundle();
        args.putParcelable(ARG_JOB_ORDER, jobOrder);

        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){


        }
        else{


        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    public void initializeViews(View parent) {

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void showConfirmPackageScreen(com.yilinker.expressinternal.mvp.model.Package selectedPackage) {

    }

    @Override
    public void goToMainScreen() {

    }

    @Override
    public void loadChecklistItems(List<ChecklistItem> items) {

    }

    @Override
    public void updateItem(ChecklistItem item) {

    }

    @Override
    public void enableCompleteButton(boolean isEnabled) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onItemClick(int position, ChecklistItem object) {

    }
}

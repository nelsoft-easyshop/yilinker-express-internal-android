package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.constants.JobOrderConstant;

/**
 * Created by J.Bautista
 */
public class ActivityProblematic extends BaseActivity {

    public static final String ARG_JOB_ORDER = "jobOrder";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problematic);

        initViews();

        showOptions();
    }

    @Override
    public void onBackPressed() {

        FragmentManager manager = getFragmentManager();

        if(manager.getBackStackEntryCount() > 0){

            manager.popBackStack();
        }
        else{

            super.onBackPressed();
        }


    }

    private void initViews(){

        //For Action Bar
        setTitle(getString(R.string.actionbar_title_problematic));
        setActionBarBackgroundColor(R.color.marigold);
    }


    private void replaceFragment(Fragment fragment, boolean addToBackStack){

        FragmentManager manager = getFragmentManager();

        FragmentTransaction transaction =  manager.beginTransaction();

        transaction.replace(R.id.flFragment, fragment);

        if(addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }


    private void showOptions(){

        Fragment fragment = FragmentProblematicOptions.createInstance(new Bundle());
        replaceFragment(fragment, false);

    }

    private void showReportForm(int type){

        Bundle bundle = new Bundle();
        bundle.putInt(FragmentReportForm.ARG_TYPE, type);

        FragmentReportForm fragment = FragmentReportForm.createInstance(bundle);

        replaceFragment(fragment, true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnDamage:

                showReportForm(JobOrderConstant.PROBLEMATIC_DAMAGED);
                break;

            case R.id.btnNoPayment:

                showReportForm(JobOrderConstant.PROBLEMATIC_UNABLE_TO_PAY);
                break;

            case R.id.btnNotFound:

                showReportForm(JobOrderConstant.PROBLEMATIC_RECIPIENT_NOT_FOUND);
                break;

            case R.id.btnRejected:

                showReportForm(JobOrderConstant.PROBLEMATIC_REJECTED);
                break;

        }
    }
}

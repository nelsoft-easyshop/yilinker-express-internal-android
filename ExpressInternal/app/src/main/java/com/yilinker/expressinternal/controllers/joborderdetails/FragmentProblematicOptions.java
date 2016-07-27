package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yilinker.core.base.BaseFragment;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;

/**
 * Created by J.Bautista
 */
public class FragmentProblematicOptions extends BaseFragment implements View.OnClickListener {

    private View.OnClickListener clickListener;

    private Button btnDamage;
    private Button btnNoPayment;
    private Button btnNotFound;
    private Button btnRejected;


    public static FragmentProblematicOptions createInstance(Bundle bundle){

        FragmentProblematicOptions fragment = new FragmentProblematicOptions();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_problematic_options, null);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        clickListener = (View.OnClickListener) activity;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {

        btnDamage = (Button) view.findViewById(R.id.btnOthers);
        btnNoPayment = (Button) view.findViewById(R.id.btnNoPayment);
        btnNotFound = (Button) view.findViewById(R.id.btnNotFound);
        btnRejected = (Button) view.findViewById(R.id.btnRejected);

        btnDamage.setOnClickListener(this);
        btnNoPayment.setOnClickListener(this);
        btnNotFound.setOnClickListener(this);
        btnRejected.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        clickListener.onClick(v);

    }
}

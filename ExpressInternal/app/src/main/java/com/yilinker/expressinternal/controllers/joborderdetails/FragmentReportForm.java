package com.yilinker.expressinternal.controllers.joborderdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.core.base.BaseFragment;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;

/**
 * Created by J.Bautista
 */
public class FragmentReportForm extends BaseFragment {

    public static final String ARG_TYPE = "type";

    private TextView tvTitle;

    private int type;

    public static FragmentReportForm createInstance(Bundle bundle){

        FragmentReportForm fragment = new FragmentReportForm();

        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_problematic_reportform, null);

        return view;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        String title = null;

        switch (type){

            case JobOrderConstant.PROBLEMATIC_DAMAGED:

                title = getString(R.string.damaged_upon_delivery);
                break;

            case JobOrderConstant.PROBLEMATIC_RECIPIENT_NOT_FOUND:

                title = getString(R.string.recipient_not_found);
                break;

            case JobOrderConstant.PROBLEMATIC_REJECTED:

                title = getString(R.string.delivery_rejected);
                break;

            case JobOrderConstant.PROBLEMATIC_UNABLE_TO_PAY:

                title = getString(R.string.unable_to_pay);
                break;

        }

        tvTitle.setText(title);

    }

    private void getData(){

        Bundle bundle = getArguments();

        type = bundle.getInt(ARG_TYPE);

    }

}

package com.yilinker.expressinternal.controllers.checklist;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;

/**
 * Created by J.Bautista
 */
public class FragmentDialogUpdateStatus extends DialogFragment implements View.OnClickListener{

    public static final String ARG_NEW_STATUS = "newStatus";

    private static final String ARG_REQUEST_CODE = "requestCode";

    private Button btnDelivery;
    private Button btnDropoff;

    private DialogDismissListener listener;

    private int requestCode;

    private String newStatus;

    public static FragmentDialogUpdateStatus createInstance(int requestCode){

        FragmentDialogUpdateStatus fragment = new FragmentDialogUpdateStatus();

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestCode = getArguments().getInt(ARG_REQUEST_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_update_status, container, false);

        Window window =  getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        listener = (DialogDismissListener) activity;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if(newStatus != null) {

            Bundle args = new Bundle();
            args.putString(ARG_NEW_STATUS, newStatus);

            listener.onDialogDismiss(requestCode, args);
        }

        super.onDismiss(dialog);

    }

    private void initViews(View view){

        btnDelivery = (Button) view.findViewById(R.id.btnNewDelivery);
        btnDropoff = (Button) view.findViewById(R.id.btnNewDropoff);

        btnDelivery.setOnClickListener(this);
        btnDropoff.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnNewDelivery:

                //TODO Check if type is Delivery. If not, show error message

                newStatus = JobOrderConstant.JO_CURRENT_DELIVERY;
                break;

            case R.id.btnNewDropoff:

                newStatus = JobOrderConstant.JO_CURRENT_DROPOFF;
                break;
        }

        dismiss();
    }
    
}

package com.yilinker.expressinternal.mvp.view.accreditation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by J.Bautista on 3/30/16.
 */
public class FragmentDialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_REQUEST_CODE = "requestCode";

    private DatePickerDialog.OnDateSetListener listener;

    public static FragmentDialogDatePicker createInstance(int requestCode){

        FragmentDialogDatePicker fragment = new FragmentDialogDatePicker();

        Bundle bundle = new Bundle();

        bundle.putInt(ARG_REQUEST_CODE, requestCode);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        listener = (DatePickerDialog.OnDateSetListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        listener.onDateSet(view, year, monthOfYear, dayOfMonth);

    }
}

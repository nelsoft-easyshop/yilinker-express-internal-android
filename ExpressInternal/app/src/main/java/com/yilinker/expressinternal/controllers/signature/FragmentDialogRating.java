package com.yilinker.expressinternal.controllers.signature;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;

/**
 * Created by J.Bautista
 */
public class FragmentDialogRating extends DialogFragment implements RatingBar.OnRatingBarChangeListener {

    private static final String ARG_REQUEST_CODE = "requestCode";
    public static final String ARG_RATING = "rating";

    private RatingBar ratingBar;

    private int requestCode;

    private DialogDismissListener listener;

    private int rating;

    public static FragmentDialogRating createInstance(int requestCode, int rating){

        FragmentDialogRating fragment = new FragmentDialogRating();

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE , requestCode);
        args.putInt(ARG_RATING, rating);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        requestCode = args.getInt(ARG_REQUEST_CODE);
        rating = args.getInt(ARG_RATING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_rating, container, false);

        Window window =  getDialog().getWindow();


        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(0));

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

    private void initViews(View view){

        ratingBar = (RatingBar) view.findViewById(R.id.ratingJob);

        ratingBar.setRating((float)rating);

        ratingBar.setOnRatingBarChangeListener(this);
    }


    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        Bundle bundle = new Bundle();
        bundle.putFloat(ARG_RATING, rating);

        listener.onDialogDismiss(requestCode, bundle);
        dismiss();
    }
}

package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.model.MapLegendItem;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.MapLegendPresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.map.IMapLegendView;
import com.yilinker.expressinternal.mvp.view.joborderlist.map.MapLegendAdapter;

import java.util.List;

/**
 * Created by J.Bautista on 4/7/16.
 */
public class FragmentDialogMapLegend extends DialogFragment implements IMapLegendView {

    private static final String ARG_ANCHORY = "anchorY";

    private MapLegendPresenter presenter;

    private MapLegendAdapter adapter;

    private int anchorY;

    public static FragmentDialogMapLegend createInstance(int anchorY){

        FragmentDialogMapLegend fragment = new FragmentDialogMapLegend();

        Bundle args = new Bundle();
        args.putInt(ARG_ANCHORY, anchorY);

        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        anchorY = getArguments().getInt(ARG_ANCHORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_maplegend, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(savedInstanceState == null){

            presenter = new MapLegendPresenter();

            initializeViews(view);
            presenter.bindView(this);

            presenter.onViewCreated(getTitles(), getResourceId());

        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
            presenter.bindView(this);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();

        if(dialog != null){

            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();

            window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;

            params.height = height;
            params.width = width;

            params.y = anchorY;

            window.setAttributes(params);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void loadItems(List<MapLegendItem> items) {

        adapter.clearAndAddAll(items);
    }

    private void initializeViews(View parent){

        RecyclerView rvItems = (RecyclerView) parent.findViewById(R.id.rvItems);
        ImageView ivClose = (ImageView) parent.findViewById(R.id.ivClose);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        setAdapter(rvItems);
    }

    private void setAdapter(RecyclerView rvItems){

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setAutoMeasureEnabled(true);

        adapter = new MapLegendAdapter();

        rvItems.setLayoutManager(layoutManager);
        rvItems.setAdapter(adapter);

    }

    private String[] getTitles(){

        String[] titles = getActivity().getResources().getStringArray(R.array.maplegend_title);

        return titles;
    }

    private int[] getResourceId(){

        TypedArray typedArray = getActivity().getResources().obtainTypedArray(R.array.maplegend_icons);

        int[] resourceIds = convertTypedArrayToIntArray(typedArray);

        return resourceIds;
    }

    private int[] convertTypedArrayToIntArray(TypedArray array){

        int[] intArray = new int[array.length()];

        for(int i =0; i < array.length(); i++){

            intArray[i] = array.getResourceId(i, 0);

        }

        return intArray;
    }
}

package com.yilinker.expressinternal.mvp.view.bulkcheckin;

import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private MapLegendPresenter presenter;

    private MapLegendAdapter adapter;

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void loadItems(List<MapLegendItem> items) {


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

        MapLegendAdapter adapter = new MapLegendAdapter();

        rvItems.setLayoutManager(layoutManager);
        rvItems.setAdapter(adapter);

    }

    private String[] getTitles(){

        String[] titles = getActivity().getResources().getStringArray(R.array.jobs_type_items);

        return titles;
    }

    private int[] getResourceId(){

        TypedArray typedArray = getActivity().getResources().obtainTypedArray(R.array.main_tab_icons_selected);

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

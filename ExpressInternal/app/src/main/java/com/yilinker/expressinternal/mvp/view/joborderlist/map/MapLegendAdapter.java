package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.MapLegendItem;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.MapLegendItemPresenter;

/**
 * Created by J.Bautista on 4/7/16.
 */
public class MapLegendAdapter extends ListRecyclerViewAdapter<MapLegendItem, MapLegendItemPresenter, MapLegendItemViewHolder> {

    @NonNull
    @Override
    protected MapLegendItemPresenter createPresenter(@NonNull MapLegendItem model) {

        MapLegendItemPresenter presenter = new MapLegendItemPresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull MapLegendItem model) {

        return model.getId();
    }

    @Override
    public MapLegendItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_maplegend_item, parent, false);

        return new MapLegendItemViewHolder(view);
    }

}

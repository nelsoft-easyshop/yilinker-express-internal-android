package com.yilinker.expressinternal.mvp.view.bulkcheckin;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.BulkCheckinItem;
import com.yilinker.expressinternal.mvp.presenter.bulkcheckin.BulkCheckinItemPresenter;

/**
 * Created by J.Bautista on 4/6/16.
 */
public class BulkCheckinAdapter extends ListRecyclerViewAdapter<BulkCheckinItem, BulkCheckinItemPresenter, BulkCheckinViewHolder> {

    @NonNull
    @Override
    protected BulkCheckinItemPresenter createPresenter(@NonNull BulkCheckinItem model) {

        BulkCheckinItemPresenter presenter = new BulkCheckinItemPresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull BulkCheckinItem model) {

        return model.getId();
    }

    @Override
    public BulkCheckinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bulkcheckin_item, parent, false);

        return new BulkCheckinViewHolder(view);
    }

}

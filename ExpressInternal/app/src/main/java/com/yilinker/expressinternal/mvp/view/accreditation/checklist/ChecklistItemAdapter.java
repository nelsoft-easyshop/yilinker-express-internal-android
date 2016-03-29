package com.yilinker.expressinternal.mvp.view.accreditation.checklist;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ChecklistItemPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.OnDataUpdateListener;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ChecklistItemAdapter extends ListRecyclerViewAdapter<AccreditationRequirementData, ChecklistItemPresenter, ChecklistItemViewHolder> {

    private OnDataUpdateListener listener;

    public ChecklistItemAdapter(OnDataUpdateListener listener){

        this.listener = listener;
    }

    @NonNull
    @Override
    protected ChecklistItemPresenter createPresenter(@NonNull AccreditationRequirementData model) {

        ChecklistItemPresenter presenter = new ChecklistItemPresenter();

        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull AccreditationRequirementData model) {

        return model.getId();
    }

    @Override
    public ChecklistItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_accreditation_item_checklist_row, parent, false);
        ChecklistItemViewHolder viewHolder = new ChecklistItemViewHolder(view, listener);

        return viewHolder;
    }
}

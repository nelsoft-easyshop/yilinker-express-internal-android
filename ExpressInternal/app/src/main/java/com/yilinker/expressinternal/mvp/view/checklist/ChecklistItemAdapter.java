package com.yilinker.expressinternal.mvp.view.checklist;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.presenter.checklist.ChecklistItemPresenter;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ChecklistItemAdapter extends ListRecyclerViewAdapter<ChecklistItem, ChecklistItemPresenter, ChecklistItemViewHolder> {

    private RecyclerViewClickListener listener;

    public ChecklistItemAdapter(RecyclerViewClickListener listener){

        this.listener = listener;
    }

    @NonNull
    @Override
    protected ChecklistItemPresenter createPresenter(@NonNull ChecklistItem model) {

        ChecklistItemPresenter presenter = new ChecklistItemPresenter();

        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull ChecklistItem model) {

        return model.getId();
    }

    @Override
    public ChecklistItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_checklist_item2
                , parent, false);
        ChecklistItemViewHolder viewHolder = new ChecklistItemViewHolder(view, listener);

        return viewHolder;
    }
}

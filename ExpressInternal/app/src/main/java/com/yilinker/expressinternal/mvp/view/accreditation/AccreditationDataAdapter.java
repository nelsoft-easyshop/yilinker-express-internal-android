package com.yilinker.expressinternal.mvp.view.accreditation;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.accreditation.AccreditationDataPresenter;

/**
 * Created by J.Bautista on 3/17/16.
 */
public class AccreditationDataAdapter extends ListRecyclerViewAdapter<AccreditationRequirementData, AccreditationDataPresenter, AccreditationDataViewHolder> {

    private RecyclerViewClickListener listener;

    public AccreditationDataAdapter(RecyclerViewClickListener listener){

        this.listener = listener;
    }

    @NonNull
    @Override
    protected AccreditationDataPresenter createPresenter(@NonNull AccreditationRequirementData model) {

        AccreditationDataPresenter presenter = new AccreditationDataPresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull AccreditationRequirementData model) {

        return model.getId();
    }

    @Override
    public AccreditationDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false);

        return new AccreditationDataViewHolder(view, listener);
    }
}

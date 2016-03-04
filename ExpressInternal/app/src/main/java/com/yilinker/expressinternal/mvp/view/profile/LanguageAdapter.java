package com.yilinker.expressinternal.mvp.view.profile;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.Languages;
import com.yilinker.expressinternal.mvp.presenter.profile.LanguagePresenter;

/**
 * Created by rlcoronado on 03/03/2016.
 */
public class LanguageAdapter extends ListRecyclerViewAdapter<Languages, LanguagePresenter, LanguageViewHolder> {

    private RecyclerViewClickListener listener;

    public LanguageAdapter(RecyclerViewClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    protected LanguagePresenter createPresenter(@NonNull Languages model) {

        LanguagePresenter presenter = new LanguagePresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull Languages model) {
        return model.getId();
    }


    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LanguageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_language, parent, false), listener);
    }
}

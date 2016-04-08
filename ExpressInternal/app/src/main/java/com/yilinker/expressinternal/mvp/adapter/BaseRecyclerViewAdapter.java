package com.yilinker.expressinternal.mvp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by J.Bautista on 2/22/16.
 * From https://github.com/remind101/android-arch-sample
 *
 * Base class adapter for recycler views
 */
public abstract class BaseRecyclerViewAdapter <M, P extends BasePresenter, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    protected final Map<Object, P> presenters;

    public BaseRecyclerViewAdapter() {
        presenters = new HashMap<>();
    }

    @NonNull protected P getPresenter(@NonNull M model) {
        System.err.println("Getting presenter for item " + getModelId(model));
        return presenters.get(getModelId(model));
    }

    @NonNull protected abstract P createPresenter(@NonNull M model);

    @NonNull protected abstract Object getModelId(@NonNull M model);


    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);

        holder.unbindPresenter();
    }

    @Override
    public boolean onFailedToRecycleView(VH holder) {
        // Sometimes, if animations are running on the itemView's children, the RecyclerView won't
        // be able to recycle the view. We should still unbind the presenter.
        holder.unbindPresenter();

        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindPresenter(getPresenter(getItem(position)));
    }

    protected abstract M getItem(int position);
}

package com.yilinker.expressinternal.mvp.adapter;

import com.yilinker.expressinternal.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by J.Bautista on 2/22/16.
 * From https://github.com/remind101/android-arch-sample
 *
 * Base class for recycler view with List data source
 */
public abstract class ListRecyclerViewAdapter <M, P extends BasePresenter, VH extends BaseViewHolder<P>> extends BaseRecyclerViewAdapter<M, P, VH> {

    private final List<M> models;

    public ListRecyclerViewAdapter() {
        models = new ArrayList<>();
    }

    public void clearAndAddAll(Collection<M> data) {
        models.clear();
        presenters.clear();

        for (M item : data) {
            addInternal(item);
        }

        notifyDataSetChanged();
    }

    public void addAll(Collection<M> data) {
        for (M item : data) {
            addInternal(item);
        }

        int addedSize = data.size();
        int oldSize = models.size() - addedSize;
        notifyItemRangeInserted(oldSize, addedSize);
    }

    public void addItem(M item) {
        addInternal(item);
        notifyItemInserted(models.size());
    }

    public void updateItem(M item) {
        Object modelId = getModelId(item);

        // Swap the model
        int position = getItemPosition(item);
        if (position >= 0) {
            models.remove(position);
            models.add(position, item);
        }

        // Swap the presenter
        P existingPresenter = presenters.get(modelId);
        if (existingPresenter != null) {
            existingPresenter.setModel(item);
        }

        if (position >= 0) {
            notifyItemChanged(position);
        }
    }

    public void removeItem(M item) {
        int position = getItemPosition(item);
        if (position >= 0) {
            models.remove(item);
        }
        presenters.remove(getModelId(item));

        if (position >= 0) {
            notifyItemRemoved(position);
        }
    }

    private int getItemPosition(M item) {
        Object modelId = getModelId(item);

        int position = -1;
        for (int i = 0; i < models.size(); i++) {
            M model = models.get(i);
            if (getModelId(model).equals(modelId)) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void addInternal(M item) {
        System.err.println("Adding item " + getModelId(item));
        models.add(item);
        presenters.put(getModelId(item), createPresenter(item));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    protected M getItem(int position) {
        return models.get(position);
    }
}

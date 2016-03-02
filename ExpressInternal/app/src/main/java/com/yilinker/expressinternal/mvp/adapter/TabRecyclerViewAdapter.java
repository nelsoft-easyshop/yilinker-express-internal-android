package com.yilinker.expressinternal.mvp.adapter;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yilinker.expressinternal.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public abstract class TabRecyclerViewAdapter<M, P extends BasePresenter, VH extends BaseViewHolder<P>> extends BaseRecyclerViewAdapter<M, P, VH> {

    private final List<M> models;
    private int width;
    private int resourceId;

    public TabRecyclerViewAdapter(int resourceId){

        this.models = new ArrayList<>();
        this.resourceId = resourceId;
    }


    public void setEqualWidth(WindowManager windowManager, int countTab){

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;

        this.width = size.x / countTab;
    }

    public void addAll(Collection<M> data) {
        for (M item : data) {
            addInternal(item);
        }

        int addedSize = data.size();
        int oldSize = models.size() - addedSize;
        notifyItemRangeInserted(oldSize, addedSize);
    }

    private void addInternal(M item) {
        System.err.println("Adding item " + getModelId(item));
        models.add(item);
        presenters.put(getModelId(item), createPresenter(item));
    }

    @NonNull
    @Override
    protected P createPresenter(@NonNull M model) {
        return null;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull M model) {
        return null;
    }

    @Override
    protected M getItem(int position) {

        return models.get(position);
    }


    @Override
    public int getItemCount() {

        return models.size();
    }

    public int getWidth() {
        return width;
    }

    public int getResourceId() {
        return resourceId;

    }
}

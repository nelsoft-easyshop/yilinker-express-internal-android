package com.yilinker.expressinternal.mvp.adapter;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;

/**
 * Created by J.Bautista on 3/2/16.
 */
public abstract class TabRecyclerViewAdapter<M, P extends BasePresenter, VH extends BaseViewHolder<P>> extends ListRecyclerViewAdapter<M, P, VH> {

    private int width;
    private int resourceId;

    public TabRecyclerViewAdapter(int resourceId){
        super();

        this.resourceId = resourceId;
    }

    public void setEqualWidth(WindowManager windowManager, int countTab){

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;

        this.width = size.x / countTab;
    }

    public int getWidth() {
        return width;
    }

    public int getResourceId() {
        return resourceId;

    }
}

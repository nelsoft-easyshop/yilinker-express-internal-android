package com.yilinker.expressinternal.mvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yilinker.expressinternal.mvp.presenter.BasePresenter;

/**
 * Created by J.Bautista on 2/22/16.
 * From https://github.com/remind101/android-arch-sample
 *
 * Base class for view holders to be used in recycler views
 */
public class BaseViewHolder <P extends BasePresenter> extends RecyclerView.ViewHolder {

    protected P presenter;


    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void bindPresenter(P presenter) {
        this.presenter = presenter;
        presenter.bindView(this);
    }

    public void unbindPresenter() {
        presenter = null;
    }
}

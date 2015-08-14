package com.yilinker.expressinternal.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;

/**
 * Created by J.Bautista
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{

    private RecyclerViewClickListener<T> listener;

    public BaseViewHolder(View view){
        super(view);

        view.setOnClickListener(this);
    }

    public BaseViewHolder(View view, RecyclerViewClickListener<T> listener){
        super(view);

        this.listener = listener;
        view.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        this.listener.onItemClick(getAdapterPosition(), getObject());
    }

    public abstract T getObject();

    /**
     * Binds an object with the ViewHolder
     * @param object
     */
    public abstract void setViews(T object);

    }


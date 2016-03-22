package com.yilinker.expressinternal.mvp.view.confirmpackage;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.PackageType;
import com.yilinker.expressinternal.mvp.presenter.confirmpackage.PackageTypePresenter;

/**
 * Created by J.Bautista on 3/21/16.
 */
public class PackageTypeAdapter extends ListRecyclerViewAdapter<PackageType, PackageTypePresenter, PackageTypeViewHolder>{

    private RecyclerViewClickListener listener;

    public PackageTypeAdapter(RecyclerViewClickListener listener){

        this.listener = listener;
    }

    @NonNull
    @Override
    protected PackageTypePresenter createPresenter(@NonNull PackageType model) {

        PackageTypePresenter presenter = new PackageTypePresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull PackageType model) {

        return model.getId();
    }

    @Override
    public PackageTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_package_item2, parent, false);
        return new PackageTypeViewHolder(view, listener);
    }
}

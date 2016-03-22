package com.yilinker.expressinternal.mvp.view.confirmpackage;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.PackageSize;
import com.yilinker.expressinternal.mvp.model.PackageType;
import com.yilinker.expressinternal.mvp.presenter.confirmpackage.PackageSizePresenter;
import com.yilinker.expressinternal.mvp.presenter.confirmpackage.PackageTypePresenter;

/**
 * Created by J.Bautista on 3/21/16.
 */
public class PackageSizeAdapter extends ListRecyclerViewAdapter<PackageSize, PackageSizePresenter, PackageSizeViewHolder> {

    private RecyclerViewClickListener listener;

    public PackageSizeAdapter(RecyclerViewClickListener listener) {

        this.listener = listener;
    }

    @NonNull
    @Override
    protected PackageSizePresenter createPresenter(@NonNull PackageSize model) {

        PackageSizePresenter presenter = new PackageSizePresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull PackageSize model) {

        return model.getId();
    }

    @Override
    public PackageSizeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_package_item2, parent, false);
        return new PackageSizeViewHolder(view, listener);
    }
}
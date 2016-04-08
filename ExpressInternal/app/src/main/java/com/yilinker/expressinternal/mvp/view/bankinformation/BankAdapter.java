package com.yilinker.expressinternal.mvp.view.bankinformation;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.BankInformation;
import com.yilinker.expressinternal.mvp.presenter.bankinformation.BankPresenter;

/**
 * Created by patrick-villanueva on 4/6/2016.
 */
public class BankAdapter extends ListRecyclerViewAdapter<BankInformation, BankPresenter, BankViewHolder> {


    @NonNull
    @Override
    protected BankPresenter createPresenter(@NonNull BankInformation model) {
        BankPresenter presenter = new BankPresenter();

        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull BankInformation model) {
        return model.getId();
    }

    @Override
    public BankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bank_item, parent, false);

        return new BankViewHolder(view);
    }


}

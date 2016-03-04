package com.yilinker.expressinternal.mvp.view.cashManagement;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.CashHistory;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.presenter.cashManagement.CashHistoryPresenter;
import com.yilinker.expressinternal.mvp.view.mainScreen.MainTabViewHolder;

/**
 * Created by Patrick on 3/3/2016.
 */
public class CashHistoryAdapter extends ListRecyclerViewAdapter<CashHistory, CashHistoryPresenter, CashHistoryViewHolder> {


    @NonNull
    @Override
    protected CashHistoryPresenter createPresenter(@NonNull CashHistory model) {

        CashHistoryPresenter presenter = new CashHistoryPresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull CashHistory model) {
        return model.getId();
    }

    @Override
    public CashHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cashhistory_item_2, parent, false);

        return new CashHistoryViewHolder(view);
    }
}

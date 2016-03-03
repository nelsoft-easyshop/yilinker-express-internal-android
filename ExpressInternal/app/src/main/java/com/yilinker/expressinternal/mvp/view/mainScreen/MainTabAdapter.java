package com.yilinker.expressinternal.mvp.view.mainScreen;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.adapter.TabRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.MainTab;
import com.yilinker.expressinternal.mvp.presenter.mainScreen.MainTabPresenter;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class MainTabAdapter extends TabRecyclerViewAdapter<MainTab, MainTabPresenter, MainTabViewHolder> {

    private TabItemClickListener clickListener;

    public MainTabAdapter(int resourceId, TabItemClickListener clickListener) {
        super(resourceId);

        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    protected MainTabPresenter createPresenter(@NonNull MainTab model) {

        MainTabPresenter presenter = new MainTabPresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull MainTab model) {

        return model.getId();
    }

    @Override
    public MainTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(getResourceId(), parent, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(getWidth(), ViewGroup.LayoutParams.MATCH_PARENT, 1));

        return new MainTabViewHolder(view, clickListener);
    }
}

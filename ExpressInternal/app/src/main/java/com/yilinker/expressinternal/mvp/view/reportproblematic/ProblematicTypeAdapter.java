package com.yilinker.expressinternal.mvp.view.reportproblematic;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.reportproblematic.ProblematicTypePresenter;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public class ProblematicTypeAdapter extends ListRecyclerViewAdapter<ProblematicType, ProblematicTypePresenter, ProblematicTypeViewHolder> {

    private IReportProblematicClickListener listener;

    public ProblematicTypeAdapter(IReportProblematicClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ProblematicTypePresenter createPresenter(@NonNull ProblematicType model) {
        ProblematicTypePresenter presenter = new ProblematicTypePresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull ProblematicType model) {
        return model.getId();
    }

    @Override
    public ProblematicTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_problematic_type_item, parent, false);

        return new ProblematicTypeViewHolder(view, listener);
    }
}

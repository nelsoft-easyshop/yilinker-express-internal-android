package com.yilinker.expressinternal.mvp.view.accreditation.checklist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.customviews.CustomLinearLayoutManager;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ChecklistRequirementPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationRequirementView;
import com.yilinker.expressinternal.mvp.view.accreditation.OnDataUpdateListener;

import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ChecklistRequirementViewHolder extends AccreditationRequirementView<ChecklistRequirementPresenter> implements OnDataUpdateListener {

    private OnDataUpdateListener listener;

    private TextView tvLabel;

    private ChecklistItemAdapter adapter;

    public ChecklistRequirementViewHolder(View itemView, OnDataUpdateListener listener) {
        super(itemView, listener);

        this.listener = listener;

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);

        initList((ViewGroup) itemView);
    }

    @Override
    public void setLabelText(String label) {

        tvLabel.setText(label);
    }

    public void setChecklistContent(List<AccreditationRequirementData> list){

        adapter.clearAndAddAll(list);
    }

    private void initList(ViewGroup parent){

        RecyclerView rvItems = (RecyclerView) parent.findViewById(R.id.rvItems);

        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(parent.getContext(), CustomLinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(layoutManager);

        adapter = new ChecklistItemAdapter(this);
        rvItems.setAdapter(adapter);

    }

    @Override
    public void onDataUpdate(Object item) {

        AccreditationRequirementData data = (AccreditationRequirementData)item;
        adapter.updateItem(data);
    }
}

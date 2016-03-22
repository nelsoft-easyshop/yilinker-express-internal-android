package com.yilinker.expressinternal.mvp.view.accreditation;

import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.accreditation.AccreditationDataPresenter;

/**
 * Created by J.Bautista on 3/17/16.
 */
public class AccreditationDataViewHolder extends BaseViewHolder<AccreditationDataPresenter> implements View.OnClickListener {

    private RecyclerViewClickListener listener;

    private TextView tvMenuItem;

    public AccreditationDataViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        this.listener = listener;

        tvMenuItem = (TextView) itemView.findViewById(R.id.tvMenuItem);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        presenter.onClick();
    }

    public void setLabel(String label){

        tvMenuItem.setText(label);
    }

    public void selectItem(AccreditationRequirementData data){

        listener.onItemClick(data.getId(), data);
    }
}

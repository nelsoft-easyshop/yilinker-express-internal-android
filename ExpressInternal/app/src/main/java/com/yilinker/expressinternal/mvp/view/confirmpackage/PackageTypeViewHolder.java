package com.yilinker.expressinternal.mvp.view.confirmpackage;

import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.PackageType;
import com.yilinker.expressinternal.mvp.presenter.confirmpackage.PackageTypePresenter;

/**
 * Created by J.Bautista on 3/21/16.
 */
public class PackageTypeViewHolder extends BaseViewHolder<PackageTypePresenter> implements View.OnClickListener {

    private RecyclerViewClickListener listener;

    private TextView tvLabel;

    public PackageTypeViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        this.listener = listener;

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        presenter.onClick();
    }

    public void selectItem(PackageType item){

        listener.onItemClick(item.getId(), item);
    }

    public void setLabelText(String label){

        tvLabel.setText(label);
    }
}

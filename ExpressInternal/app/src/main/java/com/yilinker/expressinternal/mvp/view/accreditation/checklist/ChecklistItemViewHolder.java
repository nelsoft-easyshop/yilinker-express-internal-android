package com.yilinker.expressinternal.mvp.view.accreditation.checklist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.accreditation.CheckboxRequirementPresenter;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ChecklistItemPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.OnDataUpdateListener;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ChecklistItemViewHolder extends BaseViewHolder<ChecklistItemPresenter> implements View.OnClickListener {

    private OnDataUpdateListener listener;

    private TextView tvLabel;
    private ImageView ivImage;

    public ChecklistItemViewHolder(View itemView, OnDataUpdateListener listener) {
        super(itemView);

        this.listener = listener;

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

        itemView.setOnClickListener(this);
    }

    public void setLabelText(String label) {

        tvLabel.setText(label);
    }

    @Override
    public void onClick(View v) {

        presenter.onClick();
    }

    public void setCheckDrawable(boolean isChecked){

        int drawable = 0;

        if(isChecked){

            drawable = R.drawable.ic_accreditation_checked;
        }
        else{

            drawable = R.drawable.ic_accreditation_unchecked;
        }

        ivImage.setImageResource(drawable);
    }

    public void notifyDataChanged(AccreditationRequirementData data){

        listener.onDataUpdate(data);
    }
}

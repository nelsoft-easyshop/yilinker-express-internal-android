package com.yilinker.expressinternal.mvp.view.accreditation.button;

import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.ButtonRequirementImage;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ButtonImagePresenter;

/**
 * Created by J.Bautista on 3/29/16.
 */
public class ButtonImageViewHolder extends BaseViewHolder<ButtonImagePresenter> implements View.OnClickListener {

    private IButtonRequirementListener listener;

    private TextView tvLabel;
    private TextView tvView;
    private TextView tvDelete;

    public ButtonImageViewHolder(View itemView, IButtonRequirementListener listener) {
        super(itemView);

        this.listener = listener;

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
        tvView = (TextView) itemView.findViewById(R.id.tvView);
        tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);

        tvView.setOnClickListener(this);
        tvDelete.setOnClickListener(this);

    }

    public void setLabelText(String label){

        tvLabel.setText(label);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.tvView:

                presenter.onViewClick();
                break;

            case R.id.tvDelete:

                presenter.onDeleteClick();
                break;
        }

    }

    public void viewImage(String filePath){

        listener.onView(filePath);
    }

    public void deleteItem(ButtonRequirementImage item){

        listener.onDelete(item);
    }
}

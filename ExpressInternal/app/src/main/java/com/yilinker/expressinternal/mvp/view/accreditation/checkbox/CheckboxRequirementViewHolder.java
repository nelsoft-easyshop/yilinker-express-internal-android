package com.yilinker.expressinternal.mvp.view.accreditation.checkbox;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.accreditation.CheckboxRequirementPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationRequirementView;
import com.yilinker.expressinternal.mvp.view.accreditation.OnDataUpdateListener;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class CheckboxRequirementViewHolder extends AccreditationRequirementView<CheckboxRequirementPresenter> implements View.OnClickListener {

    private TextView tvLabel;
    private ImageView ivImage;

    public CheckboxRequirementViewHolder(View itemView, OnDataUpdateListener listener) {
        super(itemView, listener);

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

        itemView.setOnClickListener(this);
    }

    @Override
    public void setLabelText(String label) {

        tvLabel.setText(label);
    }

    @Override
    public void onClick(View v) {

        presenter.onClick();
    }

    public void setCheckDrawable(boolean isChecked){

        int drawable = 0;
        int background = 0;

        if(isChecked){

            drawable = R.drawable.ic_accreditation_unchecked;
            background = R.drawable.bg_rounded_green_nopadding;
        }
        else{

            drawable = 0;
            background = R.drawable.bg_rounded_outline_gray4;
        }

        ivImage.setBackgroundResource(background);
        ivImage.setImageResource(drawable);
    }
}

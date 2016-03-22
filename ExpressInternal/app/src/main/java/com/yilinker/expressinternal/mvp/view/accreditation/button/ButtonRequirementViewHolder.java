package com.yilinker.expressinternal.mvp.view.accreditation.button;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ButtonRequirementPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationRequirementView;
import com.yilinker.expressinternal.mvp.view.accreditation.OnDataUpdateListener;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ButtonRequirementViewHolder extends AccreditationRequirementView<ButtonRequirementPresenter> implements View.OnClickListener {

    private TextView tvLabel;


    public ButtonRequirementViewHolder(View itemView, OnDataUpdateListener listener) {
        super(itemView, listener);

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
        Button btnUpload = (Button) itemView.findViewById(R.id.btnUpload);

        btnUpload.setOnClickListener(this);
    }

    @Override
    public void setLabelText(String label) {

        tvLabel.setText(label);
    }


    @Override
    public void onClick(View v) {

    }

    private void showUploadOptions(){

        Context context = itemView.getContext();

    }
}

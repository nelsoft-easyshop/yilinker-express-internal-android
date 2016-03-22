package com.yilinker.expressinternal.mvp.view.accreditation.inputtext;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.accreditation.InputTextRequirementPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationRequirementView;
import com.yilinker.expressinternal.mvp.view.accreditation.OnDataUpdateListener;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class InputTextRequirementViewHolder extends AccreditationRequirementView<InputTextRequirementPresenter> implements View.OnFocusChangeListener {


    private TextView tvLabel;
    private EditText etValue;

    private boolean isUpdating;

    public InputTextRequirementViewHolder(View itemView, OnDataUpdateListener listener) {
        super(itemView, listener);


        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
        etValue = (EditText) itemView.findViewById(R.id.etValue);

        etValue.setOnFocusChangeListener(this);
    }


    @Override
    public void setLabelText(String label) {

        tvLabel.setText(label);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus){

            isUpdating = true;
        }
        else {

            if(isUpdating){

                presenter.onValueChanged(etValue.getText());
            }

            isUpdating = false;
        }
    }
}

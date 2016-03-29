package com.yilinker.expressinternal.mvp.view.reportproblematic;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.reportproblematic.ProblematicTypePresenter;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public class ProblematicTypeViewHolder extends BaseViewHolder<ProblematicTypePresenter> implements IProblematicTypeView {

    private Button btnType;
    private IReportProblematicClickListener listener;

    public ProblematicTypeViewHolder(View itemView, final IReportProblematicClickListener listener) {
        super(itemView);

        this.listener = listener;
        btnType = (Button) itemView.findViewById(R.id.btnType);

        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onProblematicItemClick();
            }
        });
    }

    @Override
    public void setProblematicType(String type) {
        btnType.setText(type);
    }

    @Override
    public void onProblematicItemClick(ProblematicType type) {
        listener.onProblematicTypeClick(type);
    }
}

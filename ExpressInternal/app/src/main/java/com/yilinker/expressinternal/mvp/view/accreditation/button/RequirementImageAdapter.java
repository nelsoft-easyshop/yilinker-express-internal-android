package com.yilinker.expressinternal.mvp.view.accreditation.button;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.ButtonRequirementImage;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ButtonImagePresenter;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ButtonRequirementPresenter;

/**
 * Created by J.Bautista on 3/29/16.
 */
public class RequirementImageAdapter extends ListRecyclerViewAdapter<ButtonRequirementImage, ButtonImagePresenter, ButtonImageViewHolder> {

    private IButtonRequirementListener listener;

    public RequirementImageAdapter(IButtonRequirementListener listener){

        this.listener = listener;
    }

    @NonNull
    @Override
    protected ButtonImagePresenter createPresenter(@NonNull ButtonRequirementImage model) {

        ButtonImagePresenter presenter = new ButtonImagePresenter();
        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull ButtonRequirementImage model) {

        return model.getId();
    }

    @Override
    public ButtonImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_accreditation_item_button_image, parent, false);

        return new ButtonImageViewHolder(view, listener);
    }
}

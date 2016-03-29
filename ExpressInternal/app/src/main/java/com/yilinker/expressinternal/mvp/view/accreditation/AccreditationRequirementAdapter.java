package com.yilinker.expressinternal.mvp.view.accreditation;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.AccreditationConstant;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.ListRecyclerViewAdapter;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.presenter.accreditation.AccreditationRequirementPresenter;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ButtonRequirementPresenter;
import com.yilinker.expressinternal.mvp.presenter.accreditation.CheckboxRequirementPresenter;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ChecklistRequirementPresenter;
import com.yilinker.expressinternal.mvp.presenter.accreditation.DropdownRequirementPresenter;
import com.yilinker.expressinternal.mvp.presenter.accreditation.InputTextRequirementPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.button.ButtonRequirementViewHolder;
import com.yilinker.expressinternal.mvp.view.accreditation.button.IButtonRequirementListener;
import com.yilinker.expressinternal.mvp.view.accreditation.checkbox.CheckboxRequirementViewHolder;
import com.yilinker.expressinternal.mvp.view.accreditation.checklist.ChecklistRequirementViewHolder;
import com.yilinker.expressinternal.mvp.view.accreditation.dropdown.DropdownRequirementViewHolder;
import com.yilinker.expressinternal.mvp.view.accreditation.inputtext.InputTextRequirementViewHolder;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class AccreditationRequirementAdapter extends ListRecyclerViewAdapter<AccreditationRequirement, AccreditationRequirementPresenter, AccreditationRequirementView<AccreditationRequirementPresenter>> {

    private OnDataUpdateListener listener;
    private RecyclerViewClickListener recyclerViewClickListener;
    private IButtonRequirementListener innerButtonListener;

    public AccreditationRequirementAdapter(OnDataUpdateListener listener, RecyclerViewClickListener recyclerViewClickListener){

        this.recyclerViewClickListener = recyclerViewClickListener;
        this.listener = listener;
    }

    public void setInnerButtonListener(IButtonRequirementListener innerButtonListener){

        this.innerButtonListener = innerButtonListener;
    }

    @NonNull
    @Override
    protected AccreditationRequirementPresenter createPresenter(@NonNull AccreditationRequirement model) {

        AccreditationRequirementPresenter presenter = null;
        int type = model.getType();

        switch (type){

            case AccreditationConstant.REQUIREMENT_TYPE_INPUTTEXT:

                presenter = new InputTextRequirementPresenter();
                break;

            case AccreditationConstant.REQUIREMENT_TYPE_BUTTON:

                presenter = new ButtonRequirementPresenter();
                break;

            case AccreditationConstant.REQUIREMENT_TYPE_CHECKBOX:

                presenter = new CheckboxRequirementPresenter();
                break;

            case AccreditationConstant.REQUIREMENT_TYPE_CHECKLIST:

                presenter = new ChecklistRequirementPresenter();
                break;

            case AccreditationConstant.REQUIREMENT_TYPE_DROPDOWN:

                presenter = new DropdownRequirementPresenter();
                break;

        }

        presenter.setModel(model);

        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull AccreditationRequirement model) {

        return model.getId();
    }

    @Override
    public AccreditationRequirementView onCreateViewHolder(ViewGroup parent, int viewType) {

        AccreditationRequirementView viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        switch (viewType){

            case R.layout.layout_accreditation_item_button:

                viewHolder = new ButtonRequirementViewHolder(view, listener, recyclerViewClickListener, innerButtonListener);
                break;

            case R.layout.layout_accreditation_item_checkbox:

                viewHolder = new CheckboxRequirementViewHolder(view, listener);
                break;

            case R.layout.layout_accreditation_item_dropdown:

                viewHolder = new DropdownRequirementViewHolder(view, listener);
                break;

            case R.layout.layout_accreditation_item_inputtext:

                viewHolder = new InputTextRequirementViewHolder(view, listener);
                break;

            case R.layout.layout_accreditation_item_checklist:

                viewHolder = new ChecklistRequirementViewHolder(view,listener);
                break;

        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {

        AccreditationRequirement model = getItem(position);
        int type = model.getType();
        int layout = 0;

        switch (type){

            case AccreditationConstant.REQUIREMENT_TYPE_INPUTTEXT:

                layout = R.layout.layout_accreditation_item_inputtext;
                break;

            case AccreditationConstant.REQUIREMENT_TYPE_BUTTON:

                layout = R.layout.layout_accreditation_item_button;
                break;

            case AccreditationConstant.REQUIREMENT_TYPE_CHECKBOX:

                layout = R.layout.layout_accreditation_item_checkbox;
                break;

            case AccreditationConstant.REQUIREMENT_TYPE_CHECKLIST:

                //TODO Change this
                layout = R.layout.layout_accreditation_item_checklist;
                break;

            case AccreditationConstant.REQUIREMENT_TYPE_DROPDOWN:

                layout = R.layout.layout_accreditation_item_dropdown;
                break;

        }

        return layout;

    }
}

package com.yilinker.expressinternal.mvp.view.accreditation.button;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.model.ButtonRequirementImage;
import com.yilinker.expressinternal.mvp.presenter.accreditation.ButtonRequirementPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationRequirementView;
import com.yilinker.expressinternal.mvp.view.accreditation.OnDataUpdateListener;

import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ButtonRequirementViewHolder extends AccreditationRequirementView<ButtonRequirementPresenter> implements IButtonRequirementView, View.OnClickListener, IButtonRequirementListener {

    private TextView tvLabel;
    private RecyclerView rvItems;
    private RecyclerViewClickListener recyclerViewClickListener;
    private IButtonRequirementListener innerButtonListener;

    private RequirementImageAdapter adapter;


    public ButtonRequirementViewHolder(View itemView, OnDataUpdateListener listener, RecyclerViewClickListener recyclerViewClickListener, IButtonRequirementListener innerButtonListener) {
        super(itemView, listener);

        this.recyclerViewClickListener = recyclerViewClickListener;
        this.innerButtonListener = innerButtonListener;

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
        Button btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
        rvItems = (RecyclerView) itemView.findViewById(R.id.rvItems);

        btnUpload.setOnClickListener(this);

        setUpItemAdapter();
    }

    @Override
    public void setLabelText(String label) {

        tvLabel.setText(label);
    }


    @Override
    public void onClick(View v) {

        presenter.OnUploadButtonClick();
    }

    @Override
    public void addImageItem(ButtonRequirementImage item) {

        adapter.addItem(item);
    }

    @Override
    public void deleteImageItem(ButtonRequirementImage item) {

        adapter.removeItem(item);
    }

    @Override
    public void triggerUploadButton(AccreditationRequirement item) {

        recyclerViewClickListener.onItemClick(item.getId(), item);

    }

    @Override
    public void loadImageData(List<ButtonRequirementImage> items) {

        adapter.clearAndAddAll(items);
    }

    private void setUpItemAdapter(){

        adapter = new RequirementImageAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);

        rvItems.setHasFixedSize(false);
        rvItems.setNestedScrollingEnabled(false);
        rvItems.setLayoutManager(layoutManager);

        rvItems.setAdapter(adapter);
    }

    @Override
    public void onView(String filePath) {

        innerButtonListener.onView(filePath);
    }

    @Override
    public void onDelete(ButtonRequirementImage item) {

        adapter.removeItem(item);
        presenter.onDeleteData(item);

    }
}

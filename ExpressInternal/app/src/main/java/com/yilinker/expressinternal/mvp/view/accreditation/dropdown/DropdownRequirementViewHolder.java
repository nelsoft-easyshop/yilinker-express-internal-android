package com.yilinker.expressinternal.mvp.view.accreditation.dropdown;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.accreditation.DropdownRequirementPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationDataAdapter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationRequirementView;
import com.yilinker.expressinternal.mvp.view.accreditation.OnDataUpdateListener;

import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class DropdownRequirementViewHolder extends AccreditationRequirementView<DropdownRequirementPresenter> implements View.OnClickListener, RecyclerViewClickListener<AccreditationRequirementData> {

    private TextView tvLabel;
    private Button btnSelector;
    private RecyclerView rvItems;
    private View popupView;
    private PopupWindow menu;

    private AccreditationDataAdapter adapter;

    public DropdownRequirementViewHolder(View itemView, OnDataUpdateListener listener) {
        super(itemView, listener);

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
        btnSelector = (Button) itemView.findViewById(R.id.btnSelector);

        btnSelector.setOnClickListener(this);
        btnSelector.setFocusableInTouchMode(true);

        setUpPopupMenu(itemView.getContext(), (ViewGroup)itemView);
    }

    @Override
    public void setLabelText(String label) {

        tvLabel.setText(label);
    }

    public void setSelectorButtonLabel(String label){

        btnSelector.setText(label);
    }

    @Override
    public void onClick(View v) {

        presenter.onClick();
    }

    private void setUpPopupMenu(Context context, ViewGroup parent){

        popupView = LayoutInflater.from(context).inflate(R.layout.layout_recyclerview_popup, null);
        rvItems = (RecyclerView) popupView.findViewById(R.id.rvItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(layoutManager);

        adapter = new AccreditationDataAdapter(this);
        rvItems.setAdapter(adapter);

        menu = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ViewGroup.LayoutParams params = popupView.getLayoutParams();
    }

    public void setupList(List<AccreditationRequirementData> options){

        adapter.clearAndAddAll(options);
    }

    public void showPopup(){

        menu.showAsDropDown(btnSelector);
    }

    public void dismissPopup(){

        menu.dismiss();
    }

    @Override
    public void onItemClick(int position, AccreditationRequirementData object) {

        presenter.onValueChanged(object);
    }

    @Override
    public void notifyDataChanged(AccreditationRequirement item) {
        super.notifyDataChanged(item);

        dismissPopup();
    }
}

package com.yilinker.expressinternal.controllers.checklist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseViewHolder;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.ChecklistItem;

import java.util.List;

/**
 * Created by J.Bautista
 */
public class AdapterChecklist extends RecyclerView.Adapter<BaseViewHolder<ChecklistItem>> {

    private List<ChecklistItem> objects;
    private RecyclerViewClickListener<ChecklistItem> listener;

    public AdapterChecklist(List<ChecklistItem> objects, RecyclerViewClickListener<ChecklistItem> listener){

        this.objects = objects;
        this.listener = listener;
    }

    @Override
    public BaseViewHolder<ChecklistItem> onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_checklist_item, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ChecklistItem> holder, int position) {

        ChecklistItem item = objects.get(position);

        holder.setViews(item);

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    //ViewHolder
    protected class ViewHolder extends BaseViewHolder<ChecklistItem> {

        private TextView tvTitle;

        public ViewHolder(View view, RecyclerViewClickListener<ChecklistItem> listener) {
            super(view, listener);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }

        @Override
        public ChecklistItem getObject() {

            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(ChecklistItem object) {

            tvTitle.setText(object.getTitle());

            int checkView = R.drawable.check;

            if(object.isChecked()){

                checkView = R.drawable.check_selected;
            }

            tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, checkView, 0);
        }

        @Override
        public void onClick(View v) {

            super.onClick(v);
        }
    }

}

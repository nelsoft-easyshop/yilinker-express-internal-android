package com.yilinker.expressinternal.controllers.signature;

import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseViewHolder;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by patVillanueva on 7/18/2016.
 */
public class AdapterReceivedBy extends RecyclerView.Adapter<AdapterReceivedBy.ViewHolder> {

    private RecyclerViewClickListener listener;
    private List<String> objects;

    public AdapterReceivedBy(List<String> object,RecyclerViewClickListener<String> listener){

        this.listener = listener;
        this.objects = object;
    }

    @Override
    public AdapterReceivedBy.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_spinner_item, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(AdapterReceivedBy.ViewHolder holder, int position) {
        holder.setViews(objects.get(position));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends BaseViewHolder<String> {

        private TextView tvItem;
        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView, listener);

            tvItem = (TextView) itemView.findViewById(R.id.tvItem);

            tvItem.setOnClickListener(this);
        }

        @Override
        public String getObject() {
            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(String object) {
            tvItem.setText(object);
        }
    }
}

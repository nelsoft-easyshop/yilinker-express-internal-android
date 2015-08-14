package com.yilinker.expressinternal.controllers.joborderlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.MenuItemClickListener;

import java.util.List;

/**
 * Created by J.Bautista
 */
public class AdapterJobOrderMenu extends RecyclerView.Adapter<AdapterJobOrderMenu.ViewHolder> {

    private List<String> objects;
    MenuItemClickListener listener;

    public AdapterJobOrderMenu(List<String> objects, MenuItemClickListener listener){

        this.objects = objects;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_menu_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvMenuItem.setText(objects.get(position));

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvMenuItem;

        public ViewHolder(View view){
            super(view);

            tvMenuItem = (TextView) view.findViewById(R.id.tvMenuItem);

            view.setOnClickListener(this);
        }

        public TextView getTvMenuItem() {
            return tvMenuItem;
        }

        public void setTvMenuItem(TextView tvMenuItem) {
            this.tvMenuItem = tvMenuItem;
        }

        @Override
        public void onClick(View v) {

            listener.onMenuItemClick(getAdapterPosition());
        }
    }
}

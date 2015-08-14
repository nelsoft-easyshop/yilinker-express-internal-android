package com.yilinker.expressinternal.controllers.joborderlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.TabModel;

import java.util.List;

/**
 * Created by J.Bautista
 */
public class AdapterJobOrderTab extends RecyclerView.Adapter<AdapterJobOrderTab.ViewHolder> {

    private int resId;
    private List<TabModel> objects;
    private TabItemClickListener listener;
    private int currentTab = 0;

    public AdapterJobOrderTab(int resId, List<TabModel> objects, TabItemClickListener listener) {

        this.resId = resId;
        this.objects = objects;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(resId, parent, false);

        return new ViewHolder(view);

    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {

        //Clear selected tab
        TabModel previous = objects.get(this.currentTab);
        previous.setIsSelected(false);
        notifyItemChanged(this.currentTab);

        //Set the current tab to selected
        TabModel current = objects.get(currentTab);
        current.setIsSelected(true);
        notifyItemChanged(currentTab);

        this.currentTab = currentTab;

    }

    public TabModel getObject(int position){

        return objects.get(position);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        TabModel obj = objects.get(position);

        holder.tvTitle.setText(obj.getTitle());

        int indicatorVisibility = View.INVISIBLE;
        if(obj.isSelected()){
            indicatorVisibility = View.VISIBLE;
        }

        holder.viewIndicator.setVisibility(indicatorVisibility);
        holder.setListener(listener);

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    //View holder for the adapter
    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTitle;
        private View viewIndicator;
        private TabItemClickListener listener;

        public ViewHolder(View view){
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            viewIndicator = view.findViewById(R.id.viewIndicator);

            view.setOnClickListener(this);
        }

        public TabItemClickListener getListener() {
            return this.listener;
        }

        public void setListener(TabItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {

            setCurrentTab(getAdapterPosition());
            this.listener.onTabItemClick(getAdapterPosition());

        }
    }


}

package com.yilinker.expressinternal.controllers.dashboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;

import java.util.List;

/**
 * Created by rlcoronado on 20/01/2016.
 */
public class AdapterNavigationDrawerItem extends ArrayAdapter {

    private ApplicationClass appClass = (ApplicationClass) this.getContext();

    public AdapterNavigationDrawerItem(Context context, int resource) {
        super(context, resource);
    }

    public AdapterNavigationDrawerItem(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public AdapterNavigationDrawerItem(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
    }

    public AdapterNavigationDrawerItem(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public AdapterNavigationDrawerItem(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    public AdapterNavigationDrawerItem(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == 2) {
            TextView tvItem = new TextView(this.getContext());
            tvItem.setText(String.valueOf(this.getItem(position)));
            tvItem.setPadding(100, 20, 0, 20);

            if (appClass.hasItemsForSyncing()) {

                tvItem.setTextColor(getContext().getResources().getColor(R.color.orange_red));
                tvItem.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_refresh), null);

            } else {

                tvItem.setTextColor(getContext().getResources().getColor(R.color.gray));
            }

            return tvItem;

        } else {

            return super.getView(position, convertView, parent);

        }
    }

    @Override
    public boolean isEnabled(int position) {

        if (position == 2) {
            if (appClass.hasItemsForSyncing()) { //enable depending on sync state
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

}

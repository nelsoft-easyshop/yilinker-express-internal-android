package com.yilinker.expressinternal.controllers.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
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
public class AdapterNavigationDrawerItem extends ArrayAdapter<String> {

    private ApplicationClass appClass;
    private LayoutInflater inflater;
    private int resourceId;
    private String[] objects;

    public AdapterNavigationDrawerItem(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);

        this.appClass = (ApplicationClass) context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resourceId = resource;
        this.objects = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = inflater.inflate(resourceId, null);
            holder = new ViewHolder();

            holder.tvItem = (TextView) convertView.findViewById(R.id.tvNavDrawerItem);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }


        //Set values of views
        String text = objects[position];

        holder.tvItem.setText(text);

        if (position == 2) {

            if (appClass.hasItemsForSyncing()) {

                holder.tvItem.setTextColor(getContext().getResources().getColor(R.color.orange_red));

            } else {

                holder.tvItem.setTextColor(getContext().getResources().getColor(R.color.gray));
            }


        }

        return convertView;
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


    public class ViewHolder {

        private TextView tvItem;

    }
}

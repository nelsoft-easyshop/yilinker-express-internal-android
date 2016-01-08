package com.yilinker.expressinternal.controllers.confirmpackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yilinker.core.model.express.internal.PackageType;
import com.yilinker.expressinternal.R;

import java.util.List;

/**
 * Created by rlcoronado on 07/01/2016.
 */
public class AdapterPackageTypes extends BaseAdapter {


    private List<PackageType> packageList;
    private Context context;

    public AdapterPackageTypes(Context context, List<PackageType> packageList) {

        this.context = context;
        this.packageList = packageList;

    }

    @Override
    public int getCount() {
        return packageList.size();
    }

    @Override
    public Object getItem(int position) {
        return packageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.layout_package_item, parent, false);

            viewHolder.tvReason = (TextView) convertView.findViewById(R.id.tvType);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }


        viewHolder.tvReason.setText(packageList.get(position).getName());


        return convertView;
    }

    class ViewHolder {

        public TextView tvReason;

    }

}

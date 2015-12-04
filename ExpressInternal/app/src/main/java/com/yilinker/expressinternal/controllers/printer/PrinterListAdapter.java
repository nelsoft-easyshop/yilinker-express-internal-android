package com.yilinker.expressinternal.controllers.printer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseViewHolder;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.BluetoothPrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class PrinterListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<BluetoothPrinter> objects;
    private RecyclerViewClickListener listener;

    public PrinterListAdapter(List<BluetoothPrinter> objects,  RecyclerViewClickListener<BluetoothPrinter> listener){

        this.objects = objects;
        this.listener = listener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_printerlist_item, null);
        BaseViewHolder viewHolder = new ViewHolder(view , listener);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        BluetoothPrinter object = objects.get(position);
        holder.setViews(object);

    }

    @Override
    public int getItemCount() {

        return objects.size();

    }


    protected class ViewHolder extends BaseViewHolder<BluetoothPrinter>{

        private TextView tvPrinterName;
        private TextView tvPrinterAddress;

        private RecyclerViewClickListener<BluetoothPrinter> listener;

        public ViewHolder(View view, RecyclerViewClickListener<BluetoothPrinter> listener) {
            super(view, listener);

            this.listener = listener;

            tvPrinterName = (TextView) view.findViewById(R.id.tvPrinterName);
            tvPrinterAddress = (TextView) view.findViewById(R.id.tvPrinterAddress);

        }

        @Override
        public BluetoothPrinter getObject() {

            return objects.get(getAdapterPosition());

        }

        @Override
        public void setViews(BluetoothPrinter object) {

            tvPrinterName.setText(object.getName());
            tvPrinterAddress.setText(object.getAddress());

        }

    }
}

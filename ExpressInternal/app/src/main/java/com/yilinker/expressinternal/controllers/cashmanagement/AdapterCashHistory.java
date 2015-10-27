package com.yilinker.expressinternal.controllers.cashmanagement;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseViewHolder;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.CashHistory;

import java.util.HashMap;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class AdapterCashHistory extends RecyclerView.Adapter<AdapterCashHistory.ViewHolder> {

    private static final String DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";

    private List<CashHistory> objects;
    private RecyclerViewClickListener<CashHistory> listener;

    public AdapterCashHistory(List<CashHistory> objects){

        this.objects = objects;
    }

    public AdapterCashHistory(List<CashHistory> objects, RecyclerViewClickListener<CashHistory> listener){

        this.objects = objects;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_cashhistory_item, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setViews(objects.get(position));

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    protected class ViewHolder extends BaseViewHolder<CashHistory>{

        private TextView tvJobOrderNo;
        private TextView tvAction;
        private TextView tvAmount;

        public ViewHolder(View view, RecyclerViewClickListener<CashHistory> listener) {
            super(view, listener);

//            tvJobOrderNo = (TextView) view.findViewById(R.id.tvJobOrderNo);
            tvAction = (TextView) view.findViewById(R.id.tvAction);
            tvAmount = (TextView) view.findViewById(R.id.tvAmount);

        }

        @Override
        public CashHistory getObject() {

            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(CashHistory object) {

//            tvAction.setText(object.getAction());
            tvAction.setText(DateUtility.convertDateToString(object.getDate(), DATE_FORMAT));
            tvAmount.setText(String.format("%.02f PHP", object.getAmount()));
//            tvJobOrderNo.setText(object.getJobOrderNo());

        }

    }

}

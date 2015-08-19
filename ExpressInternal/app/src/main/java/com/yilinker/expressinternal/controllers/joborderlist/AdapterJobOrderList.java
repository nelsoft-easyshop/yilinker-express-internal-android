package com.yilinker.expressinternal.controllers.joborderlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseViewHolder;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.interfaces.TimerTickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.JobOrderPickup;
import com.yilinker.expressinternal.model.TabModel;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.os.Handler;

/**
 * Created by J.Bautista
 */
public class AdapterJobOrderList<T extends  JobOrder> extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int TYPE_OPEN = 100;
    public static final int TYPE_CURRENT = 101;
    public static final int TYPE_COMPLETE =102;
    public static final int TYPE_PROBLEMATIC = 103;

    private List<T> objects;
    private int type;
    private RecyclerViewClickListener listener;

    //For counter
    private boolean isCounterActive;
    private HashMap<Integer, TextView> counterList;
    private Handler timerHandler;

    public AdapterJobOrderList(List<T> objects, int type){

        this.objects = objects;
        this.type = type;
        counterList = new HashMap<Integer, TextView>();
    }

    public AdapterJobOrderList(List<T> objects, int type, RecyclerViewClickListener<T> listener){

        this.objects = objects;
        this.type = type;
        this.listener = listener;
        counterList = new HashMap<Integer, TextView>();

    }

    //For Timer
    private final Runnable mRunnable = new Runnable() {

        public void run() {
            JobOrder jobOrder;
            TextView textView;

            // if counters are active
            if (isCounterActive) {
                if (counterList != null && objects != null) {

                    for (int i=0; i < objects.size(); i++) {
                        jobOrder = objects.get(i);
                        textView = counterList.get(i);
                        if (textView != null) {


                            Calendar calendar = Calendar.getInstance();;

                            long difference =  jobOrder.getEstimatedTimeOfArrival().getTime() - calendar.getTimeInMillis();
                            Calendar newDate = Calendar.getInstance();
                            newDate.setTimeInMillis(difference);

                            textView.setText(String.format("%02d:%02d:%02d", newDate.get(Calendar.HOUR), newDate.get(Calendar.MINUTE), newDate.get(Calendar.SECOND)));

                        }
                    }
                }
                // update every second
                timerHandler.postDelayed(this, 1000);
            }
        }
    };


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int resId = 0;
        View view = null;
        BaseViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (type){

                case TYPE_OPEN:

                    resId = R.layout.layout_job_order_list_open;
                    view = inflater.inflate(resId, parent, false);
                    viewHolder = new ViewHolderOpen(view, listener);
                    break;

                case TYPE_COMPLETE:

                    resId = R.layout.layout_job_order_list_complete;
                    view = inflater.inflate(resId, parent, false);
                    viewHolder = new ViewHolderComplete(view, listener);
                    break;

                case TYPE_CURRENT:

                    resId = R.layout.layout_job_order_list_current;
                    view = inflater.inflate(resId, parent, false);
                    viewHolder = new ViewHolderCurrent(view, listener);
                    break;

                case TYPE_PROBLEMATIC:

                    resId = R.layout.layout_job_order_list_open;
                    view = inflater.inflate(resId, parent, false);
                    viewHolder = new ViewHolderOpen(view, listener);
                    break;

            }

            return viewHolder;

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        holder.setViews(objects.get(position));

        //For Current Job Orders, add the TextView in counter list to update timer
        if(type == TYPE_CURRENT){

            counterList.put(position, ((ViewHolderCurrent)holder).tvTimer);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public void startTimer(){

        isCounterActive = true;
        timerHandler = new Handler();
        timerHandler.postDelayed(mRunnable, 0);
    }

    public void stopTimer(){

        isCounterActive = false;
        timerHandler = null;
    }



    //Class for Open JO view holder
    protected class ViewHolderOpen extends BaseViewHolder<JobOrder>{

        private TextView tvJobOrderNo;
        private TextView tvAddress;
        private TextView tvBranchName;

        public ViewHolderOpen(View view, RecyclerViewClickListener<JobOrder> listener){
            super(view, listener);

            tvJobOrderNo = (TextView) view.findViewById(R.id.tvJobOrderNo);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            tvBranchName = (TextView) view.findViewById(R.id.tvBranch);

        }


        @Override
        public JobOrder getObject() {

            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(JobOrder object) {


            tvJobOrderNo.setText(object.getJobOrderNo());
            tvAddress.setText("Sample Address");
        }

    }


    //Class for Current JO view holder
    protected class ViewHolderCurrent extends BaseViewHolder<JobOrder>{

        private TextView tvJobOrderNo;
        private TextView tvAddress;
        private TextView tvBranchName;
        private TextView tvTimer;

        public ViewHolderCurrent(View view, RecyclerViewClickListener<JobOrder> listener){
            super(view, listener);

            tvJobOrderNo = (TextView) view.findViewById(R.id.tvJobOrderNo);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            tvTimer = (TextView) view.findViewById(R.id.tvTimer);

        }


        @Override
        public JobOrder getObject() {

            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(JobOrder object) {


            tvJobOrderNo.setText(object.getJobOrderNo());
        }



    }

    //Class for Complete JO view holder
    protected class ViewHolderComplete extends BaseViewHolder<JobOrder>{

        private TextView tvJobOrderNo;
        private TextView tvAddress;
        private TextView tvBranchName;

        public ViewHolderComplete(View view, RecyclerViewClickListener<JobOrder> listener){
            super(view, listener);

            tvJobOrderNo = (TextView) view.findViewById(R.id.tvJobOrderNo);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);

        }




        @Override
        public JobOrder getObject() {

            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(JobOrder object) {


            tvJobOrderNo.setText(object.getJobOrderNo());
        }

    }


}

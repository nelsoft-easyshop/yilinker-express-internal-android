package com.yilinker.expressinternal.controllers.joborderlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseViewHolder;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.JobOrder;

import java.util.Calendar;
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

    private static final String OPEN_DATE_FORMAT = "hh:mm aa";
    private static final String CURRENT_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";

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

                    resId = R.layout.layout_job_order_list_current;
                    view = inflater.inflate(resId, parent, false);
                    viewHolder = new ViewHolderCurrent(view, listener);
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

    private static int getBackgoundByType(String type){

        int resId = 0;
        if(type.equalsIgnoreCase("pickup")){

            resId = R.drawable.tv_rounded_corner_marigold;
        }
        else {

            resId = R.drawable.tv_rounded_corner_orange_yellow;
        }

        return resId;
    }


    //Class for Open JO view holder
    protected class ViewHolderOpen extends BaseViewHolder<JobOrder>{

        private TextView tvJobOrderNo;
        private TextView tvAddress;
        private TextView tvBranchName;
        private TextView tvDistance;
        private TextView tvSize;
        private TextView tvEarning;
        private TextView tvETA;
        private TextView tvType;

        public ViewHolderOpen(View view, RecyclerViewClickListener<JobOrder> listener){
            super(view, listener);

            tvJobOrderNo = (TextView) view.findViewById(R.id.tvJobOrderNo);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            tvBranchName = (TextView) view.findViewById(R.id.tvBranch);
            tvDistance = (TextView) view.findViewById(R.id.tvDistance);
            tvSize = (TextView) view.findViewById(R.id.tvSize);
            tvEarning = (TextView) view.findViewById(R.id.tvEarning);
            tvETA = (TextView) view.findViewById(R.id.tvETA);
            tvType = (TextView) view.findViewById(R.id.tvType);
        }


        @Override
        public JobOrder getObject() {

            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(JobOrder object) {


            tvJobOrderNo.setText(object.getJobOrderNo());
            tvAddress.setText("Sample Address");
            tvBranchName.setText(object.getBranchName());
            tvDistance.setText(String.format("%.02f KM", object.getDistance() / 1000f));
            tvSize.setText(object.getSize());
            tvEarning.setText(String.format("₱%.02f", object.getEarning()));
            tvType.setText(object.getType());

            if(object.getEstimatedTimeOfArrival() != null){

                tvETA.setText(String.format("ETA %s", DateUtility.convertDateToString(object.getEstimatedTimeOfArrival(), OPEN_DATE_FORMAT)));

            }

            String address = null;
            if(object.getType().equalsIgnoreCase("pickup")){

                address = object.getPickupAddress();
            }
            else{

                address = object.getDeliveryAddress();
            }

            tvAddress.setText(address);

            tvType.setBackgroundResource(getBackgoundByType(object.getType()));

        }

    }


    //Class for Current JO view holder
    protected class ViewHolderCurrent extends BaseViewHolder<JobOrder>{

        private TextView tvJobOrderNo;
        private TextView tvAddress;
        private TextView tvBranchName;
        private TextView tvTimer;
        private TextView tvStatus;

        public ViewHolderCurrent(View view, RecyclerViewClickListener<JobOrder> listener){
            super(view, listener);

            tvJobOrderNo = (TextView) view.findViewById(R.id.tvJobOrderNo);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            tvBranchName = (TextView) view.findViewById(R.id.tvBranch);
            tvTimer = (TextView) view.findViewById(R.id.tvTimer);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        }


        @Override
        public JobOrder getObject() {

            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(JobOrder object) {


            tvJobOrderNo.setText(object.getJobOrderNo());
            tvBranchName.setText(object.getBranchName());
            tvStatus.setText(object.getStatus());

            String address = null;
            if(object.getType().equalsIgnoreCase("pickup")){

                address = object.getPickupAddress();
            }
            else{

                address = object.getDeliveryAddress();
            }

            tvAddress.setText(address);

            if(object.getStatus().equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)){
                tvStatus.setBackgroundResource(R.drawable.tv_rounded_corner_orangered);
            }
            else {
                tvStatus.setBackgroundResource(getBackgoundByType(object.getType()));
            }

        }



    }

    //Class for Complete JO view holder
    protected class ViewHolderComplete extends BaseViewHolder<JobOrder>{

        private TextView tvJobOrderNo;
        private TextView tvAddress;
        private TextView tvTimeDelivered;
        private RatingBar ratingJob;
        private TextView tvType;
        private TextView tvEarning;

        public ViewHolderComplete(View view, RecyclerViewClickListener<JobOrder> listener){
            super(view, listener);

            tvJobOrderNo = (TextView) view.findViewById(R.id.tvJobOrderNo);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            tvTimeDelivered = (TextView) view.findViewById(R.id.tvTimeDelivered);
            ratingJob = (RatingBar) view.findViewById(R.id.ratingJob);
            tvType = (TextView) view.findViewById(R.id.tvType);
            tvEarning = (TextView) view.findViewById(R.id.tvEarning);

        }


        @Override
        public JobOrder getObject() {

            return objects.get(getAdapterPosition());
        }

        @Override
        public void setViews(JobOrder object) {
            
            tvJobOrderNo.setText(object.getJobOrderNo());
            tvTimeDelivered.setText(DateUtility.convertDateToString(object.getTimeDelivered(), CURRENT_DATE_FORMAT));
            ratingJob.setRating((float) object.getRating());
            tvType.setText(object.getType());
            tvEarning.setText(String.format("+%.02f", object.getEarning()));

            String address = null;
            if(object.getType().equalsIgnoreCase("pickup")){

                address = object.getPickupAddress();
            }
            else{

                address = object.getDeliveryAddress();
            }

            tvAddress.setText(address);

            tvType.setBackgroundResource(getBackgoundByType(object.getType()));

        }

    }


}

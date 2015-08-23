package com.yilinker.expressinternal.controllers.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.controllers.signature.ActivitySignature;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.ChecklistItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class ActivityChecklist extends BaseActivity implements RecyclerViewClickListener<ChecklistItem>, ResponseHandler{

    private static final int REQUEST_SIGNATURE = 1000;

    private Button btnConfirm;

    private RecyclerView rvChecklist;
    private AdapterChecklist adapter;
    private List<ChecklistItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        initViews();

    }

    @Override
    public void onItemClick(int position, ChecklistItem object) {

        //temp
        int last = items.size() - 1;
        if(position == last ){

            if(!items.get(last).isChecked())
                showSignature();

            return;
        }

        object.setIsChecked(!object.isChecked());
        adapter.notifyItemChanged(position);

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_SIGNATURE && resultCode == RESULT_OK){

            int position = items.size() - 1;
            items.get(position).setIsChecked(true);
            adapter.notifyItemChanged(position);
        }

    }

    private void initViews(){

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        rvChecklist = (RecyclerView) findViewById(R.id.rvChecklist);

        btnConfirm.setEnabled(false);

        //For Action Bar
        setTitle("For Pickup");
        setActionBarBackgroundColor(R.color.marigold);

        btnConfirm.setOnClickListener(this);

        setAdapter();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

    }

    @Override
    public void onFailed(int requestCode, String message) {

    }

    private void setAdapter(){

        //Temp
        items = new ArrayList<>();
        ChecklistItem item = null;
        for(int i = 0; i < 4; i++){

            item = new ChecklistItem();
            item.setTitle("Checklist " + i);

            items.add(item);
        }

        adapter = new AdapterChecklist(items, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChecklist.setLayoutManager(layoutManager);
        rvChecklist.setAdapter(adapter);

    }

    private boolean isComplete(){

        boolean isComplete = true;
        for(ChecklistItem item : items){

            if(!item.isChecked()){
                isComplete = false;
                break;
            }

        }

        return isComplete;
    }

    private void setConfirmButton(boolean isEnabled){

        int color = R.color.white_gray;

        if(isEnabled){

            color = R.color.orange_yellow;
        }

        btnConfirm.setBackgroundResource(color);
        btnConfirm.setEnabled(isEnabled);
    }


    private void showSignature(){

        Intent intent = new Intent(ActivityChecklist.this, ActivitySignature.class);
        startActivityForResult(intent, REQUEST_SIGNATURE);

    }

}
